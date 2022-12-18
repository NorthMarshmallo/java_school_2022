package ru.croc.project.threads;

import ru.croc.project.Achievement;
import ru.croc.project.database.dao.AchievementDao;
import ru.croc.project.database.dao.PointsDao;
import ru.croc.project.database.dao.UserMetricDao;
import ru.croc.project.enums.AchievementType;
import ru.croc.project.enums.ActionType;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

//методы и поля protected на случай, если кто-то захочет переопределить
public class AchievementsSenderThread implements Runnable{

    volatile protected Socket socket;
    protected UserMetricDao userMetricDao;
    protected Connection baseConnection;
    protected AchievementDao achievementDao;
    protected PointsDao pointsDao;
    protected final long serverStartTime;
    protected Integer serverDayNumber;
    protected String username;
    protected Integer id;
    protected Integer points;
    protected ObjectInputStream objectInputStream;
    protected ObjectOutputStream objectOutputStream;

    //thread будет работать с одним пользователем и информация из баз данных понадобится об одной строке, поэтому
    //работа идет с мапами, а по окончании принятия действий от пользователя, все записывается в таблицы бд
    //колонки метрик - счетчик метрик
    protected Map<String, Integer> userMetricMap = Arrays.stream(ActionType.values()).collect(
            Collectors.toMap(ActionType::getColumnForUpdate, v->0));
    //колонки достижений - достижения
    protected Map<String, Achievement> achievementMap = Arrays.stream(AchievementType.values()).collect(
            Collectors.toMap(AchievementType::getNameInBase,v->new Achievement(v,0)));

    protected List<Achievement> returnList;

    public AchievementsSenderThread(Socket socket, String baseFile, long serverStartTime){

        this.socket = socket;
        this.serverStartTime = serverStartTime;
        this.points = 0;

        try{

            Connection baseConnection = DriverManager.getConnection(baseFile, "sa","");
            this.baseConnection = baseConnection;
            userMetricDao = new UserMetricDao(baseConnection);
            achievementDao = new AchievementDao(baseConnection);
            pointsDao = new PointsDao(baseConnection);

        }
        catch (Exception e) {
            System.out.println("Connecting to base for updating failed.");
            e.printStackTrace();
            closeEverything();
            return;
        }

        try{

            objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
            objectInputStream = new ObjectInputStream(this.socket.getInputStream());

        } catch (IOException e){
            System.out.println("Streams setting failed");
            e.printStackTrace();
            closeEverything();
        }

    }

    protected static final Object lock = new Object();

    @Override
    public void run() {

        //начинаем работать с сокетом, сообщаем, что он подключен и откуда
        System.out.println ("Incoming connection from: "
                + socket.getRemoteSocketAddress());

        //добавим пользователя, если его в базе еще нет, и вышлем приветственное достижение,
        //или же получим всю уже имеющуюся информацию о нем и также вышлем
        try {
            getUserDatabaseInfo();
        } catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            closeEverything();
            return;
        }


        ActionType action;

        try {

            Object endOfSign;
            //будем получать от клиента действия пока после одного захода сокет не закроется
            //или пользователь не пришлет триггер окончания потока null
            while ((!socket.isClosed()) && (endOfSign = objectInputStream.readObject()) != null) {

                //чтобы одновременно обрабатывалось одно действие и все они шли в нужном порядке
                synchronized (lock) {

                    //для накопления достиженией, на случай, если присланное действие обновит не одно
                    returnList = new ArrayList<>();
                    action = (ActionType) endOfSign;
                    //вывод на сервере будет состоять из действия клиента и дня его совершения, а также метрики
                    //6 секунд - 1 день
                    Integer currentServerDayNumber = (int) Math.floor((System.currentTimeMillis() - serverStartTime) / 1000F / 6);
                    //обновится счетчик соответствующей action метрики и, если обновится
                    //метрика ежедневной активности, то вернется true
                    boolean newDay = updateMetrics(action, currentServerDayNumber);
                    System.out.println("Day " + currentServerDayNumber + ", [" + username + "]: " + action
                    + " count: " + userMetricMap.get(action.getColumnForUpdate()));

                    //если сменился день, то проверим достижение на количество дней активности
                    //и добавим в возвращаемый лист, если да
                    Achievement achievementToAdd;
                    if (newDay) {
                        addIfNotNull(returnList, updateAchievements(ActionType.DAY_SIGN));
                    }

                    //проверим не повысился ли уровень достижения, связанного с присланным действием
                    achievementToAdd = updateAchievements(action);
                    addIfNotNull(returnList, achievementToAdd);

                    if (!returnList.isEmpty())
                        sendToSocket(returnList);
                    //добавим очки за полученные достижения
                    this.points += returnList.stream().mapToInt(Achievement::getPoints).sum();

                }
            }

            //все пользователи записываются в разные строки таблицы и один пользователь совершает действия с
            //каким-то временным промежутком, поэтому вне synchronized
            setResultsInDatabase();
            closeEverything();

        } catch (IOException | ClassNotFoundException ioe) {
            ioe.printStackTrace();
            closeEverything();
        } catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            closeEverything();
        }
    }


    protected void addIfNotNull(List<Achievement> list, Achievement achievement){

        if (achievement != null)
            list.add(achievement.clone());

    }

    protected void getUserDatabaseInfo() throws Exception{

        //принимаем ник от клиента пользователя
        try {

            this.username = (String) objectInputStream.readObject();

        } catch (Exception e){
            //в случае неполадок будем закрывать соединение
            throw new Exception("Server failed to receive username on connection " + socket.getRemoteSocketAddress());

        }

        boolean userIsInBase;
        try {
            userIsInBase = userMetricDao.baseContainsUser(username);
        } catch (SQLException e){
            throw new Exception("Failed to know if user in base");
        }

        //если пользователя с таким ником еще нет в базе, то добавляем, иначе заполним мапы предыдущей информацией
        if (!userIsInBase) {

            try {

                Integer id = userMetricDao.addNewUser(username);
                pointsDao.addNewUser(id);

                //пошлем полученное из базы id, список возможных достижений, а также
                //приветственное достижение, предварительно обновив его статус в мапе
                if (id != null) {
                    this.id = id;
                    sendNewbieAchievement();
                }

            } catch (SQLException se) {
                throw new Exception("Adding User to base failed.");
            }

        } else {

            try (PreparedStatement statement = baseConnection.prepareStatement(
                    "SELECT * FROM USER_METRIC user_metrics, ACHIEVEMENT achievements, USER_POINTS userpoints" +
                            " WHERE username = ? AND user_metrics.id = achievements.id " +
                            " AND userpoints.id = user_metrics.id ")){
                statement.setString(1,username);
                statement.executeQuery();

                //пошлем полученное из базы id и список прошлых достижений
                setAndSendPreviousData(statement.getResultSet());

            } catch(SQLException se){
                throw new Exception("Getting User previous data failed.");
            } catch (Exception e){
                throw e;
            }
        }

    }

    protected void setAndSendPreviousData(ResultSet previousData) throws Exception {

        Map<String,String> achievementToAction = ActionType.getConnectedAchievementColumns();

        try {

            if (previousData.next()) {

                //предыдущие id и заработанные ранее очки
                this.id = previousData.getInt(1);
                this.points = previousData.getInt("points");
                sendToSocket("Already in base. Your server id is " + id + ". Current points: " + points);

                //предыдущие значения метрик
                for (String metric : userMetricMap.keySet()) {
                    userMetricMap.put(metric, previousData.getInt(metric));
                }

                //прошлые достижения
                for (String achievement : achievementMap.keySet()) {
                    achievementMap.get(achievement).setLevelAndMetric(previousData.getInt(achievement),
                            userMetricMap.get(achievementToAction.get(achievement)));
                }
                sendToSocket(achievementMap.values().stream().map(Achievement::clone).toList());

            }

        } catch (SQLException se){
            throw new Exception("Setting User previous data failed.");
        }
    }

    //используется только для посыла, используя заданную по дефолту в конструкторах информацию
    protected void sendNewbieAchievement() throws Exception{

        try {

            //полученные из базы id и дефолтные (при создании thread) points = 0
            sendToSocket("Your server id is " + id + ". Current points: " + points);

            achievementDao.addNewUserAchievements(id);
            //сначала принимается текущий заполненный в конструкторе начальными значениями прогресс
            sendToSocket(achievementMap.values().stream().map(Achievement::clone).toList());
            achievementMap.get("newbie").setLevel(0);
            this.points += achievementMap.get("newbie").getPoints();
            sendToSocket(Arrays.asList(achievementMap.get("newbie").clone()));


        } catch (SQLException se){
            throw new Exception("Sending newbie failed.");
        } catch (Exception e){
            throw e;
        }

    }


    //увеличит на 1 значение метрики для поданного действия и проверит метрику дневной активности
    protected boolean updateMetrics(ActionType action, Integer currentDay){

        userMetricMap.put(action.getColumnForUpdate(), userMetricMap.get(action.getColumnForUpdate()) + 1);

        if (!currentDay.equals(serverDayNumber)){
            this.serverDayNumber = currentDay;
            userMetricMap.put("days_active", userMetricMap.get("days_active") + 1);
            return true;
        }

        return false;

    }

    //вернет достижение, если его уровень обновился, иначе null
    protected Achievement updateAchievements(ActionType action){

        //возьмет связанное с действием достижение
        AchievementType achievementType = action.getAchievementType();
        //возьмет для достижения мапу с порогами-уровнями и по ближайшему снизу к значению
        //метрики (после всех уже проведенных обновлений) ключу-порогу сопоставит уровень
        Integer currentLevel = achievementType.getLevelThresholds().floorEntry(
                userMetricMap.get(action.getColumnForUpdate())).getValue();

        Achievement achievementToUpdate = achievementMap.get(achievementType.getNameInBase());

        if (!currentLevel.equals(achievementToUpdate.getLevel())) {

            achievementToUpdate.setLevelAndMetric(currentLevel,
                    userMetricMap.get(action.getColumnForUpdate()));
            //проверит уникальное внеуровневое достижение (если никакое достижение не обновилось, то проверять
            //нет смысла), добавит его в заданный полем returnList в случае удачи
            checkForWanderer();
            return achievementToUpdate;

        }

        return null;

    }

    protected void checkForWanderer(){

        //если не осталось достижений с уровнем -1 (отсутствие)
        if (achievementMap.get("wanderer").getLevel()<0 &&
        !achievementMap.values().stream().map(Achievement::getLevel).collect(Collectors.toSet()).contains(-1)){
            achievementMap.get("wanderer").setLevel(0);
            returnList.add(achievementMap.get("wanderer"));
        }

    }

    //контроль над тем, чтобы значения посылаемых объектов для самого thread не менялись, осуществляется за
    //пределами метода (в основном clone())
    protected void sendToSocket(Object message) throws Exception{

        try {

            objectOutputStream.reset();
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();

        } catch (Exception e){
            throw new Exception("Failed to send objects to the client socket.");
        }

    }

    protected void setResultsInDatabase(){

        try {
            achievementDao.updateAllAchievements(achievementMap,id);
        } catch (SQLException se) {
            System.out.println("Setting results to achievements failed.");
            se.printStackTrace();
            closeEverything();
        }

        try{
            userMetricDao.updateAllMetrics(userMetricMap,id);
        } catch (SQLException se) {
            System.out.println("Setting results to metrics failed.");
            se.printStackTrace();
            closeEverything();
        }

        try{
            pointsDao.updatePoints(points,id);
        } catch (SQLException se) {
            System.out.println("Setting results to points failed.");
            se.printStackTrace();
            closeEverything();
        }

    }

    protected void closeEverything(){
        try{
            System.out.println("Client " + username + " disconnected.");
            if (socket != null)
                socket.close();
            if (objectInputStream != null)
                objectInputStream.close();
            if (baseConnection != null)
                baseConnection.close();
            if (objectOutputStream != null)
                objectOutputStream.close();
        }
        catch(Exception e) {
            System.out.println("Server failed to safely close connection to the user " + this.username);
        }
    }

}
