package ru.croc.task14;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class BlackListFilter<T> implements BlackListFilterInterface<T>{

    Predicate<T> filterCondition;

    public BlackListFilter(Predicate<T> filterCondition){
        this.filterCondition = filterCondition;
    }

    public List<T> filterComments(Collection<T> comments) {
        return filterComments(comments,filterCondition);
    }
}
