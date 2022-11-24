package ru.croc.task12;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SocialService {
    private static List<String> comments = Arrays.asList("Hello, bitches;sup", "Girls", "Shit, this bitches got me", "Believe me, it's true",
    "sfep[sfp kesplf !!!))", "come on, it's SHIt anf u kniw ir");
    private static Set<String> blackList = new HashSet<>(Arrays.asList("bitches", "shit"));

    public static void main(String[] args) {
        BlackListFilter blf = new BlackListFilter();
        blf.filterComments(comments,blackList);
        System.out.println(comments.toString());
    }
}
