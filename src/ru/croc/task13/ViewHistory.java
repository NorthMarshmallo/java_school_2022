package ru.croc.task13;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class ViewHistory {
    private static HashMap<String, Movie> serviceMovies = new HashMap<>();
    //ключи - идентификаторы фильмов, значения - объекты-фильмы
    private static Set<User> history = new HashSet<>();

    public static HashMap<String, Movie> getMapOfServiceMovies() {
        return serviceMovies;
    }

    public static void addUser(User user) {
        history.add(user);
        for (String id : user.getUserViewHistorySet()) {
            //сколько прошлый пользователь посмотрел фильм с данным ид
            serviceMovies.get(id).updateHistoryViewsNumber(user.getIdViews(id));
        }
    }

    public static void updateRcById(String id, Integer similarityCoeff) {
        serviceMovies.get(id).updateRecommendationCoefficient(similarityCoeff);
    }

    public static List<Object> compareViews(User newUser, User prevUser) {
        Integer similarityCoeff;
        Double threshouldCheck = 0.0;
        //множество фильмов, которые пользователь из истории смотрел, а текущий нет
        Set<String> newUserNotViewed = new HashSet<>();
        similarityCoeff = 0;
        Set<String> prevUserViewsSet = prevUser.getUserViewHistorySet();
        double threshouldAdd = 1.0/newUser.getUserViewHistorySet().size();
        for (String id : prevUserViewsSet) {
            if (newUser.getUserViewHistorySet().contains(id)) {
                //если посмотрел данный фильм меньше раз, чем новый пользователь, то совпадение
                //только на это число раз, если больше или равно, то по данному фильму полное совпадение
                //будет иметь значение не большее, чем полное количество просмотров у нового пользователя, но приведение
                //к виду от 0 до 1 выполнится в конце, тк с Integer проще работать
                similarityCoeff += Math.min(newUser.getIdViews(id), prevUser.getIdViews(id));
                //иногда прошлый пользователь смотрел больше половины фильмов нового, но количество просмотров для
                //каждого фильма у них сильно отличается, чтобы все равно учитывать этого пользователя, но
                //не терять влияние схожести в количестве просмотров введена эта переменная
                threshouldCheck += threshouldAdd;
            } else {
                newUserNotViewed.add(id);
            }
        }
        List<Object> result = new ArrayList();
        result.add(similarityCoeff);
        result.add(newUserNotViewed);
        result.add(threshouldCheck);
        return result;
    }

    public static void addListOfMovies(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            String[] parsedLine;
            while ((line = br.readLine()) != null) {
                parsedLine = line.split(",");
                serviceMovies.put(parsedLine[0], new Movie(parsedLine[0], parsedLine[1]));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
    public static void addViewHistory(String fileName){
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                //все подсчеты просмотров для пользователя установятся в конструкторе
                User prevUser = new User(Arrays.asList(line.split(",")));
                //добавятся просмотры для каждого фильма в истории
                ViewHistory.addUser(prevUser);
            }
        } catch(Exception e) {
            System.out.println("Something is wrong with file ViewHistory.txt");
            e.printStackTrace();
        }
    }

    public static void getReadyForAnotherNewUser(User newUser) {
        ViewHistory.addUser(newUser);
        for (String id : serviceMovies.keySet()) {
            if (!newUser.getUserViewHistorySet().contains(id)){
                serviceMovies.get(id).setRcZero();
            }
        }
    }

    public static void processViewHistory(User newUser) {
        Integer similarityCoeff;
        int totalNumberOfNewUserViews = newUser.getUserViewHistory().size();
        for (User prevUser : history) {
            List<Object> compareResults = ViewHistory.compareViews(newUser, prevUser);
            similarityCoeff = (Integer) compareResults.get(0);
            Set<String> newUserNotViewed = (HashSet<String>) compareResults.get(1);
            if ((Double) compareResults.get(2) >= 0.5) {
                for (String id : newUserNotViewed) {
                    // чем больше пользователей с похожими предпочтениями посмотрели фильм тем больше
                    // в итоге будет для него рекомендательный коэффициент
                    ViewHistory.updateRcById(id, similarityCoeff);
                }
            }
        }
    }
}
