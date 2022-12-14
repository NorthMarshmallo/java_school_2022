package ru.croc.task16.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public enum ComfortClass {

    ULTRA_ECONOM("Ультра-Эконом"),
    ECONOM("Эконом"),
    COMFORT("Комфорт"),
    ULTRA_COMFORT("Ультра-Комфорт");

    private String classStr;

    private static Map<String, ComfortClass> COMFORT_CLASS_MAP = Arrays.stream(ComfortClass.values())
            .collect(Collectors.toMap(ComfortClass::getComfortClassStr, e -> e));

    ComfortClass(String classStr){
        this.classStr = classStr;
    }

    public static ComfortClass randomClass(){
        return ComfortClass.values()[new Random().nextInt(ComfortClass.values().length)];
    }

    public static ComfortClass strToComfortClass(String classStr){
        if (COMFORT_CLASS_MAP.get(classStr) == null)
            throw new RuntimeException("Приложение не предоставляет автомобили класса '" + classStr + "'");
        else return COMFORT_CLASS_MAP.get(classStr);
    }

    public String getComfortClassStr(){
        return classStr;
    }

}
