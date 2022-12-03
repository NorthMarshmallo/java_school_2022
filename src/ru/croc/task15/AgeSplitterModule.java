package ru.croc.task15;

import java.util.*;

public class AgeSplitterModule {

    private static TreeMap<AgeRange, TreeSet<Respondent>> groups = new TreeMap<>(Comparator.comparingInt(AgeRange::getMinAge).reversed());

    public static void main(String[] args1) {
        String[] args = "18 25 35 45 60 80 100".split(" ");
        System.out.println("Модуль предоставит распределение списка респондентов по возрастным группам.");
        System.out.println("Пожалуйста, введите список в формате строк <ФИО> <Возраст>. В конце списка должна быть строка END.");
        setGroups(args);
        Scanner in = new Scanner(System.in);
        String line, respondents;
        AgeRange range;
        String[] parsedLine;
        while (!(line = in.nextLine()).equals("END")){
            parsedLine = line.split(" (?!.* )");
            updateGroups(new Respondent(parsedLine[0], Integer.parseInt(parsedLine[1])));
        }
        for (Map.Entry<AgeRange,TreeSet<Respondent>> group: groups.entrySet()){
            range = group.getKey();
            respondents = group.getValue().toString().replaceAll("[\\[\\]]", "");
            if (!respondents.equals(""))
                System.out.println(range + ": " + respondents);
        }
    }

    public static void setGroups(String[] maxAges){
        int minAge = 0;
        int maxAge;
        for (String maxAgeStr: maxAges){
            maxAge = Integer.parseInt(maxAgeStr);
            groups.put(new AgeRange(minAge, maxAge), new TreeSet<Respondent>());
            minAge = maxAge + 1;
        }
        groups.put(new AgeRange(minAge, 123), new TreeSet<Respondent>());
    }

    public static void updateGroups(Respondent respondent){
        for (AgeRange range: groups.keySet()){
            if (respondent.getAge() >= range.getMinAge()){
                groups.get(range).add(respondent);
                break;
            }
        }
    }

}
