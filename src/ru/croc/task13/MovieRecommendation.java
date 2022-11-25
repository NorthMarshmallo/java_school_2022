package ru.croc.task13;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class MovieRecommendation {
    private static HashMap<String,String> movieIdentificator = new HashMap<>();
    //ключи - идентификаторы фильмов, значения - названия
    private static HashMap<String,Integer> historyViewsNumber = new HashMap<>();
    //для подсчета общего количества просмотров фильма на сервисе по его ид
    private static HashMap<String,Integer> userViewsNumber = new HashMap<>();
    //ля подсчета просмотра пользователем каждого фильма по ид
    private static HashMap<String,Integer> similarityRecommendationCoeff = new HashMap<>();
    //для учета вклада схожести с историей просмотра
    //других пользователей
    private static int totalUserViews; //сколько всего пользователь посмотрел фильмов

    public static void main(String[] args){
        getMovieIdentificators("C:\\Users\\Asus\\JavaSchool2022\\src\\ru\\croc\\task13\\Movies.txt");
        //для первого примера предлагаю ввести "4,15", рекомендацией будет Дюна, поскольку, несмотря на общее число
        //просмотров - 6 (у Хатико 9), коэффициент схожести для Дюны самый высокий
        //(ее смотрели больше человек, которые смотрели как 4, так и 15)
        Scanner in = new Scanner(System.in);
        totalUserViews = 0;
        String[] userViewed = in.nextLine().split(",");
        for (String movieId: userViewed){
            totalUserViews++;
            updateMap(movieId, userViewsNumber,1);
        }
        recommendMovie();
    }

    public static void getMovieIdentificators(String fileName) {
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))){
            String line;
            String[] parsedLine;
            while ((line = br.readLine()) != null) {
                parsedLine = line.split(",");
                movieIdentificator.put(parsedLine[0], parsedLine[1]);
            }
        } catch(Exception e){
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void processViewHistory(String fileName){
        Set<String> userViewed = userViewsNumber.keySet();
        /* схожесть предпочтений пользователя из истории и текущего. */
        int similarityCoeff;
        int n, eachCount = 1; //n для размера массивов
        //eachCount для подсчета количества просмотров конкретного фильма
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))){
            String id, line;
            String[] parsedLine;
            while ((line = br.readLine()) != null) {
                //множество фильмов, которые пользователь из истории смотрел, а текущий нет
                Set<String> userNotViewed = new HashSet<>();
                similarityCoeff = 0;
                parsedLine = line.split(",");
                Arrays.sort(parsedLine); //чтобы не прибавлялись лишние коэффициенты, если просмотр фильма был с перерывом
                n = parsedLine.length;
                for (int i = 0; i < n; i++) {
                    if ((i < n-1) && (parsedLine[i].equals(parsedLine[i+1]))){
                        eachCount += 1;
                    } else {
                        id = parsedLine[i];
                        updateMap(id, historyViewsNumber, eachCount);
                        if (userViewed.contains(id)) {
                            //если посмотрел данный фильм меньше раз, чем пользователь, то совпадение
                            //только на это число раз, если больше или равно, то по данному фильму полное совпадение
                            //будет иметь значение не большее, чем полное количество просмотров у пользователя, но приведение
                            //к виду от 0 до 1 выполнится в конце, тк с Integer проще работать
                            similarityCoeff += Math.min(userViewsNumber.get(id), eachCount);
                        } else {
                            userNotViewed.add(id);
                        }
                        eachCount = 1;
                    }
                }
                if ((double)similarityCoeff / totalUserViews >= 0.5) {
                    for (String movieId : userNotViewed) {
                        // чем больше пользователей с похожими предпочтениями посмотрели фильм тем больше
                        // в итоге будет для него рекомендательный коэффициент
                        updateMap(movieId,similarityRecommendationCoeff,similarityCoeff);
                    }
                }
            }
        } catch(Exception e){
            System.out.println("Something is wrong with file ViewHistory.txt");
        }
    }

    public static void recommendMovie(){
        processViewHistory("C:\\Users\\Asus\\JavaSchool2022\\src\\ru\\croc\\task13\\ViewHistory.txt");
        double maxScore = 0;
        double finalScore, simCoeff;
        String recommend = null;
        for (String potentialRecommend: similarityRecommendationCoeff.keySet()){
            //привести каждый сложенный коэффициент к виду от 0 до 1 на случай, если числа будут очень большими
            simCoeff = (double)similarityRecommendationCoeff.get(potentialRecommend) / totalUserViews;
            //System.out.println(potentialRecommend + " " + simCoeff);
            finalScore = historyViewsNumber.get(potentialRecommend) * simCoeff;
            //System.out.println(finalScore);
            if (finalScore > maxScore) {
                maxScore = finalScore;
                recommend = potentialRecommend;
            }
        }
        System.out.println(movieIdentificator.get(recommend));
    }

    public static void updateMap(String movieId, HashMap<String,Integer> updateMap, int add){
        Integer count = updateMap.get(movieId);
        if (count == null) {
            count = 0;
        }
        count += add;
        updateMap.put(movieId, count);
    }
}
