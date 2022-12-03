package ru.croc.task14;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class SocialService{
    private static List<String> comments = Arrays.asList("Hello, bitches;sup", "Girls", "Shit, this bitches got me", "Believe me, it's true",
    "sfep[sfp kesplf !!!))", "come on, it's SHIt anf u kniw ir");
    private static Set<String> blackList = new HashSet<>(Arrays.asList("bitches", "shit"));
    private static Predicate<String> filterCondition = (String comment) ->
    {for (String word: comment.split("[\\p{Punct}\\p{Space}\\n]+")) {
        if (blackList.contains(word.toLowerCase()))
            return true;
    }
    return false;
    };

    public static void main(String[] args) {
        //сервис создает новый фильтр с нужным ему условием фильтрации
        BlackListFilter<String> blf = new BlackListFilter<>(filterCondition);
        //фильтр уже вызовет дефолтный метод для очистки комментариев и передаст ему сервисный предикат
        List<String> filtredComments = blf.filterComments(comments);
        System.out.println(filtredComments.toString());
    }
}
