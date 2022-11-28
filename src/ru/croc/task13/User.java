package ru.croc.task13;

import java.util.*;

public class User {
    private List<String> userViewHistory;
    private HashMap<String, Integer> idViews = new HashMap<>();

    public User(List<String> userViewHistory) {
        this.userViewHistory = userViewHistory;
        calculateIdViews();
    }

    public List<String> getUserViewHistory() {
        return this.userViewHistory;
    }
    public Set<String> getUserViewHistorySet(){
        return new HashSet<>(this.userViewHistory);
    }

    public void calculateIdViews() {
        Set<String> userViewsSet = this.getUserViewHistorySet();
        for (String id : userViewsSet) {
            //сколько прошлый пользователь посмотрел фильм с данным ид
            this.idViews.put(id, Collections.frequency(this.userViewHistory, id));
        }
    }
    public Integer getIdViews(String id){
        return this.idViews.get(id);
    }
}
