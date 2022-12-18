package ru.croc.project.enums;

import java.util.Map;
import java.util.TreeMap;

public enum AchievementType {


    //для текущей версии предусмотрено для упрощения задачи 5 уровней, далее достижения не начисляются
    NEWBIE("Newbie", "newbie","signed in app",
            Map.of(0,100),new TreeMap<>(Map.of(0,-1))),
    WANDERER("Wanderer", "wanderer","tried all app's functions",
            Map.of(0,400), new TreeMap<>(Map.of(0,-2))),
    HARDWORKING("Hardworking", "hardworking", "stayed active for ? days",
            AchievementType.getCommonPoint(),
            new TreeMap<>(Map.of(0,-1,7,1,30,2,50,3,100,4,200,5))),
    TRUTH_SEEKER("Truth seeker","truth_seeker", "learned ? words",
            AchievementType.getCommonPoint(),
            new TreeMap<>(Map.of(0,-1,10,1,50,2,100,3,500,4,1000,5))),
    TEST_SLAYER("Test slayer","test_slayer", "successfully passed ? tests",
            AchievementType.getCommonPoint(),
            new TreeMap<>(Map.of(0,-1,5,1,15,2,50,3,100,4,200,5))),
    BOOKWORM("Bookworm","bookworm","read ? books for now",
            AchievementType.getCommonPoint(),
            new TreeMap<>(Map.of(0,-1,5,1,10,2,50,3,100,4,200,5))),
    CINEMADDICT("Cinemaddict","cinemaddict", "watched ? movies",
            AchievementType.getCommonPoint(),
            new TreeMap<>(Map.of(0,-1,5,1,15,2,50,3,100,4,200,5)));

    private final String strName;
    private final String nameInBase;
    private final String triggerAction;
    private final Map<Integer,Integer> levelsPoints; //очки по уровню
    private final TreeMap<Integer,Integer> levelThresholds; //уровень по порогу
    AchievementType(String strName, String nameInBase, String triggerAction, Map<Integer,Integer> levelsPoints,
                    TreeMap<Integer,Integer> levelThresholds){

        this.strName = strName;
        this.nameInBase = nameInBase;
        this.triggerAction = triggerAction;
        this.levelsPoints = levelsPoints;
        this.levelThresholds = levelThresholds;

    }

    public static Map<Integer,Integer> getCommonPoint(){
        return Map.of(1,200,2,500,3,1000,4,1500,5,3000);
    }

    public String getStrName(){
        return this.strName;
    }

    public String getNameInBase() {
        return nameInBase;
    }

    public String getTriggerAction() {
        return triggerAction;
    }

    //иммутабельная коллекция
    public Map<Integer, Integer> getLevelsPoints() {
        return levelsPoints;
    }

    public TreeMap<Integer, Integer> getLevelThresholds() {
        return levelThresholds;
    }

}
