package ru.croc.task13;

public class Movie {
    private final String id;
    private final String name;
    private Integer historyViews;
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

    public void updateHistoryViewsNumber(int addedNumber){
        if (this.historyViews == null) {
            this.historyViews = 0;
        }
        this.historyViews += addedNumber;
    }

}
