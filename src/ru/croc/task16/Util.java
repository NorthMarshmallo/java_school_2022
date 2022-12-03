package ru.croc.task16;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Util {
    public static List<String> pickNRandom(List<String> lst, int n) {
        List<String> copy = new ArrayList<String>(lst);
        Collections.shuffle(copy);
        return n > copy.size() ? copy.subList(0, copy.size()) : copy.subList(0, n);
    }

    public static int numberContained(HashSet<String> requirements, HashSet<String> facilities){
        int count = 0;
        for (String req : requirements){
            if (facilities.contains(req))
                count++;
        }
        return count;
    }
}
