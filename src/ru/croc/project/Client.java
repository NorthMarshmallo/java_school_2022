package ru.croc.project;

import ru.croc.project.enums.ActionType;
import ru.croc.project.threads.AchievementsGetterThread;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client {

    private AchievementsGetterThread achievementsGetterThread;
    private Socket socket;
    private String username;
    private List<Achievement> achievementList;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public Client(Socket socket, String username){

        try{

            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            this.socket = socket;
            this.username = username;

        } catch (IOException e){
            System.out.println("Connection to server failed.");
            e.printStackTrace();
            closeEverything();
            System.exit(0);
        }

    }

    public static void main(String[] args){

        String input;
        System.out.println("Welcome to the Learn It app! To model behavior of already" +
                " registered client enter previous login, otherwise new one will be created: ");
        try (Scanner in = new Scanner(System.in)){
            input = in.nextLine();}
        int port = 9000;
        try (Socket socket = new Socket("127.0.0.1", port)) {

            /* создание клиента (заполнение полей, настройка подключений) */
            Client client = new Client(socket, input);
            /* отправит логин клиента и получит первую информацию */
            client.getConnectionInfo();
            /* сервер запомнит имя клиента, выдаст ему уникальный id, и в следующий раз, введя то же имя,
            можно будет получить список достижений пользователя */
            client.gettingAchievements();

        } catch (Exception e) {
            System.out.println("Creation of sockets failed.");
            e.printStackTrace();
            System.exit(0);
        }

    }


    public void getConnectionInfo(){

        try {

            //отправим ник и получим информацию о нашем подключении к серверу (id в базе и информацию
            //о достижениях, если она присутствует, иначе приветственное достижение)
            objectOutputStream.writeObject(username);
            objectOutputStream.flush();

            System.out.println(objectInputStream.readObject());
            achievementList = (List<Achievement>) objectInputStream.readObject();
            System.out.println("Your progress: " + achievementList);

        } catch (ClassNotFoundException | IOException e){
            System.out.println("Getting first info from server failed.");
            closeEverything();
            System.exit(0);
        }

    }
    public void gettingAchievements(){
        //поток для получения уведомлений о достижениях
        achievementsGetterThread = new AchievementsGetterThread(socket,objectInputStream);
        Thread achievementsGetting = new Thread(achievementsGetterThread);
        achievementsGetting.start();
        modelRandomBehavior();
        closeEverything();
    }


    //для простоты за один заход выполняется не более 10 действий, заходов не более 30 для одного запуска клиента
    //(один запуск метода смоделирует заходы в течение нескольких дней, чтобы каждый раз программу заново не запускать)
    //выделенный сервером поток закончит слушать сообщения о действиях при посыле null

    public void modelRandomBehavior(){

        Random random = new Random();

        try {
            for (int i = 0; i <= random.nextInt(30); i++) {

                for (int j = 0; j <= random.nextInt(10); j++) {
                    objectOutputStream.writeObject(ActionType.getRandomAction());
                    objectOutputStream.flush();
                    //действия выполняются с промежутком не более 200 милисек
                    TimeUnit.MILLISECONDS.sleep(random.nextInt(200));
                }

                //следующий заход не позднее чем через пол дня (3 сек)
                TimeUnit.MILLISECONDS.sleep(random.nextInt(3000));

            }
            objectOutputStream.writeObject(null);
            objectOutputStream.flush();

        } catch (Exception e){
        System.out.println("Can't send action to the server");
        e.printStackTrace();
        closeEverything();
        System.exit(0);
    }

    }

    public void closeEverything(){
        try {
            if (achievementsGetterThread != null)
                achievementsGetterThread.closeEverything();
            if (socket != null)
                socket.close();
            if (objectOutputStream != null)
                objectOutputStream.close();
            if (objectInputStream != null)
                objectInputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
