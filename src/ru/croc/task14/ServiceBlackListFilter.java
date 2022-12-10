package ru.croc.task14;

import java.util.List;
import java.util.function.Predicate;

public class ServiceBlackListFilter<T> implements BlackListFilter<T> {

    private Predicate<T> filterCondition;

    public ServiceBlackListFilter(Predicate<T> filterCondition){
        this.filterCondition = filterCondition;
    }

    public List<T> filterComments(Iterable<T> comments) {
        return filterComments(comments,filterCondition);
    }
}
