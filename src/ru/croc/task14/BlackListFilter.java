package ru.croc.task14;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public interface BlackListFilter<T>  {

    /**
     * From the given iterable collection of comments removes ones
     * by filterCondition predicate and returns result as a list of filter's type.
     *
     * @param comments  iterable collection of comments; every comment
     *                  is a sequence of words, separated
     *                  by spaces, punctuation or line breaks
     * @param filterCondition predicate-condition to detect your blacklist words
     */
    default List<T> filterComments(Iterable<T> comments, Predicate<T> filterCondition) {
        List<T> commentsCopy = new ArrayList<>();
        for (T comment: comments) {
            if (!filterCondition.test(comment)){
                commentsCopy.add(comment);
            }
        }
        return commentsCopy;
    }
}
