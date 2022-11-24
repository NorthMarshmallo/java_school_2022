package ru.croc.task12;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlackListFilter implements BlackListFilterInterface {

    private List<String> comments; //предполагается, что фильтру подается список еще не обработанных комментариев
    private static Set<String> blackList = new HashSet<>(); //множество запрещенных слов может пополняться с каждой подачей комментариев
    private String[] parsedComment;

    @Override
    public void filterComments(List<String> comments, Set<String> newToBlackList) {
        this.comments = comments;
        blackList.addAll(newToBlackList);
        int c = 0;
        for (String comment: comments){
            parsedComment = comment.split("[\\p{Punct}\\p{Space}\\n]+");
            for (String word: parsedComment){
                if (blackList.contains(word.toLowerCase())){
                    comment = comment.replaceAll(word, "*".repeat(word.length()));
                }
            }
            comments.set(c,comment);
            c++;
        }
    }
}
