package ru.croc.task13;

public class Movie {
    private final String id;
    private final String name;
    private Integer historyViews;
    private Integer recommendationCoefficient = 0; /**будет складываться из коэффициентов схожести предпочтений пользователей,
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

    public Integer getRc() {
        return this.recommendationCoefficient;
    }

    public void updateHistoryViewsNumber(int addedNumber){
        if (this.historyViews == null) {
            this.historyViews = 0;
        }
        this.historyViews += addedNumber;
    }
    public void updateRecommendationCoefficient(int addedNumber){
        this.recommendationCoefficient += addedNumber;
    }
    //метод после выдачи рекомендации для нового пользователя, чтобы подготовиться к следующему
    public void setRcZero() {
        //если у кового пользователя нет просмотров для этого фильма, то он мог быть рекомендован, значит надо обновить рекомендательный
        //коэффициент
        this.recommendationCoefficient = 0;
    }

}
