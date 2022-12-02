package ru.croc.task13;

import java.util.*;

public class MovieRecommendation {

    public static void main(String[] args) {
        ViewHistory.addListOfMovies("src\\ru\\croc\\task13\\Movies.txt");
        //для первого примера предлагаю ввести "4,15", рекомендацией будет Дюна, поскольку, несмотря на общее число
        //просмотров - 6 (у Хатико 9), коэффициент схожести для Дюны самый высокий
        //(ее смотрели больше человек, которые смотрели как 4, так и 15)
        ViewHistory.addViewHistory("src\\ru\\croc\\task13\\ViewHistory.txt");

        System.out.println("To get recommendation please enter in format 1,2,3,4 your history of views:");
        Scanner in = new Scanner(System.in);
        String line;
        while (!(line = in.nextLine()).equals("")) {
            try {
                List<String> newUserViewsList = Arrays.asList(line.split(","));
                if (!ViewHistory.getMapOfServiceMovies().keySet().containsAll(newUserViewsList))
                    throw new Exception();
                User newUser = new User(newUserViewsList);
                //получаем рекомендацию после сбора информации о новом пользователе
                System.out.println("I think this is the most suitable movie for you:");
                recommendMovie(newUser);
                ViewHistory.getReadyForAnotherNewUser(newUser);
            } catch(Exception e){
                System.out.println("You entered something wrong, try again");
            }
            System.out.println("If you wish to get another one, please use input again:");
        }
    }

    private static void recommendMovie(User newUser){
        HashMap<String, Integer> recommendCoeffMap;
        recommendCoeffMap = ViewHistory.processViewHistory(newUser);
        double maxScore = 0;
        double potentialRecommendScore, recommendationCoeff;
        double scale = Math.pow(10,2);
        String recommendedMovieId = null;
        HashMap<String,Movie> serviceMovies = ViewHistory.getMapOfServiceMovies();
        int totalNumberOfNewUserViews = newUser.getUserViewHistory().size();
        for (String idOfPotentialRecommendation: serviceMovies.keySet()){
            if (!newUser.getUserViewHistorySet().contains(idOfPotentialRecommendation)) {
                //привести каждый сложенный коэффициент к виду от 0 до 1 на случай, если числа будут очень большими
                recommendationCoeff = (double) recommendCoeffMap.get(idOfPotentialRecommendation) / totalNumberOfNewUserViews;
                recommendationCoeff = Math.ceil(recommendationCoeff * scale) / scale;
                //System.out.println(idOfPotentialRecommendation + " " + recommendationCoeff);
                potentialRecommendScore = serviceMovies.get(idOfPotentialRecommendation).getHistoryViews() * recommendationCoeff;
                potentialRecommendScore = Math.ceil(potentialRecommendScore * scale) / scale;
                //System.out.println(potentialRecommendScore);
                if (potentialRecommendScore >= maxScore) {
                    maxScore = potentialRecommendScore;
                    recommendedMovieId = idOfPotentialRecommendation;
                }
            }
        }
        System.out.println(serviceMovies.get(recommendedMovieId).getName());
    }
}

