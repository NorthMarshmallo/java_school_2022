package ru.croc.task16.enums;

import java.util.*;
import java.util.stream.Collectors;

public enum Facility {

    BABY_CHAIR("Детское кресло"),
    NEW_CAR("Новый автомобиль"),
    EXPERIENCED_DRIVER("Водитель со стажем"),
    CHEAP_FOR_CATEGORY("Недорого для категории"),
    DISABLE_SUITABLE("Подходит для людей с ограниченными возможностями");

    private String fasilityStr;
    private static Map<String, Facility> FACILITIES_MAP = Arrays.stream(Facility.values())
            .collect(Collectors.toMap(Facility::getFasilityStr, e -> e));

    Facility(String fasilityStr){
        this.fasilityStr = fasilityStr;
    }

    public static List<Facility> pickRandomList(){
        int n = new Random().nextInt(Facility.values().length);
        List<Facility> copy = Arrays.asList(values());
        Collections.shuffle(copy);
        return n > copy.size() ? copy.subList(0, copy.size()) : copy.subList(0, n);
    }

    public static Facility strToFacility(String fasilityStr){
        if (FACILITIES_MAP.get(fasilityStr) == null)
            throw new RuntimeException("Следующих удобств нет в наших автомобилях: '" + fasilityStr + "'");
        return FACILITIES_MAP.get(fasilityStr);
    }

    public String getFasilityStr(){
        return fasilityStr;
    }

}
