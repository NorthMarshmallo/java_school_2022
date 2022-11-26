package ru.croc.task13;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class MovieRecommendation {
    private static HashMap<String, Movie> serviceMovies = new HashMap<>();
    //ключи - идентификаторы фильмов, значения - объекты-фильмы
    private static Set<String> viewedByNewUser = new HashSet<>();
    //ид фильмов которые новый пользователь смотрел
    private static int totalNumberOfNewUserViews; //сколько всего пользователь посмотрел фильмов

    public static void main(String[] args) {
        getListOfMovies("C:\\Users\\Asus\\JavaSchool2022\\src\\ru\\croc\\task132\\Movies.txt");
        //для первого примера предлагаю ввести "4,15", рекомендацией будет Дюна, поскольку, несмотря на общее число
        //просмотров - 6 (у Хатико 9), коэффициент схожести для Дюны самый высокий
        //(ее смотрели больше человек, которые смотрели как 4, так и 15)
        Scanner in = new Scanner(System.in);
        totalNumberOfNewUserViews = 0;
        for (String id : in.nextLine().split(",")) {
            totalNumberOfNewUserViews++;
            //увеличиваем на 1 количество просмотров новым пользователем фильма по ид
            serviceMovies.get(id).updateNewUserViewsNumber(1);
            //пополняем список фильмов которые он смотрел
            viewedByNewUser.add(id);
        }
        //получаем рекомендацию после сбора информации о новом пользователе
        recommendMovie();
    }

    private static void getListOfMovies(String fileName) {
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

    //метод посчитает все коэффициенты схожести предпочтений пользователей из истории с предпочтениями нового
    //пользователя и обновит рекомендательные коэффициенты для каждого фильма
    private static void processViewHistory(String fileName) {
        /* Coeff для подсчета схожести предпочтений пользователя из истории и нового. */
        int similarityCoeff, prevUserIdViews;
        List prevUserViewsList;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                //множество фильмов, которые пользователь из истории смотрел, а текущий нет
                Set<String> newUserNotViewed = new HashSet<>();
                similarityCoeff = 0;
                prevUserViewsList = Arrays.asList(line.split(","));
                Set<String> prevUserViewsSet = new HashSet<String>(prevUserViewsList);
                for (String id : prevUserViewsSet) {
                    //сколько прошлый пользователь посмотрел фильм с данным ид
                    prevUserIdViews = Collections.frequency(prevUserViewsList, id);
                    serviceMovies.get(id).updateHistoryViewsNumber(prevUserIdViews);
                    if (viewedByNewUser.contains(id)) {
                        //если посмотрел данный фильм меньше раз, чем новый пользователь, то совпадение
                        //только на это число раз, если больше или равно, то по данному фильму полное совпадение
                        //будет иметь значение не большее, чем полное количество просмотров у нового пользователя, но приведение
                        //к виду от 0 до 1 выполнится в конце, тк с Integer проще работать
                        similarityCoeff += Math.min(serviceMovies.get(id).getNewUserViews(), prevUserIdViews);
                    } else {
                        newUserNotViewed.add(id);
                    }
                }
                if ((double) similarityCoeff / totalNumberOfNewUserViews >= 0.5) {
                    for (String id : newUserNotViewed) {
                        // чем больше пользователей с похожими предпочтениями посмотрели фильм тем больше
                        // в итоге будет для него рекомендательный коэффициент
                        serviceMovies.get(id).updateRecommendationCoefficient(similarityCoeff);
                    }
                }
            }
        } catch(Exception e) {
            System.out.println("Something is wrong with file ViewHistory.txt");
        }
    }
    private static void recommendMovie(){
        processViewHistory("C:\\Users\\Asus\\JavaSchool2022\\src\\ru\\croc\\task132\\ViewHistory.txt");
        double maxScore = 0;
        double potentialRecommendScore, recommendationCoeff;
        String recommendedMovieId = null;
        for (String idOfPotentialRecommendation: serviceMovies.keySet()){
            if (!viewedByNewUser.contains(idOfPotentialRecommendation)) {
                //привести каждый сложенный коэффициент к виду от 0 до 1 на случай, если числа будут очень большими
                recommendationCoeff = (double) serviceMovies.get(idOfPotentialRecommendation).getRc() / totalNumberOfNewUserViews;
                //System.out.println(idOfPotentialRecommendation + " " + recommendationCoeff);
                potentialRecommendScore = serviceMovies.get(idOfPotentialRecommendation).getHistoryViews() * recommendationCoeff;
                //System.out.println(potentialRecommendScore);
                if (potentialRecommendScore > maxScore) {
                    maxScore = potentialRecommendScore;
                    recommendedMovieId = idOfPotentialRecommendation;
                }
            }
        }
        System.out.println(serviceMovies.get(recommendedMovieId).getName());
    }
}

