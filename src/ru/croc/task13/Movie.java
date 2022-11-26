package ru.croc.task13;

public class Movie {
    private final String id;
    private final String name;
    private Integer historyViews;
    private Integer newUserViews;
    private Integer recommendationCoefficient; /**будет складываться из коэффициентов схожести предпочтений пользователей,
     которые данный фильм смотрели, с предпочтениями нового пользователя*/
    public Movie(String id, String name){
        this.id = id;
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public Integer getHistoryViews(){
        return this.historyViews;
    }
    public Integer getNewUserViews(){
        return this.newUserViews;
    }

    public Integer getRc() {
        return this.recommendationCoefficient;
    }

    public void updateHistoryViewsNumber(int addedNumber){
        if (this.historyViews == null) {
            this.historyViews = 0;
        }
        this.historyViews += addedNumber;
    }
    public void updateNewUserViewsNumber(int addedNumber){
        if (this.newUserViews == null) {
            this.newUserViews = 0;
        }
        this.newUserViews += addedNumber;
    }
    public void updateRecommendationCoefficient(int addedNumber){
        if (this.recommendationCoefficient == null) {
            this.recommendationCoefficient = 0;
        }
        this.recommendationCoefficient += addedNumber;
    }
}
