package ru.croc.task14;

import ru.croc.task11.EchoClientThread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public interface BlackListFilterInterface<T>  {

    /**
     * From the given list of comments removes ones
     * that contain words from the black list.
     *
     * @param comments  list of comments; every comment
     *                  is a sequence of words, separated
     *                  by spaces, punctuation or line breaks
     * @param filterCondition predicate-condition to detect your blacklist words
     */
    default List<T> filterComments(Collection<T> comments, Predicate<T> filterCondition) {
        List<T> commentsCopy = new ArrayList<>();
        for (T comment: comments) {
            if (!filterCondition.test(comment)){
                commentsCopy.add(comment);
            }
        }
        return commentsCopy;
    }
}
