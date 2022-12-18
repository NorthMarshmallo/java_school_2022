package ru.croc.project.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public enum ActionType {

    DAY_SIGN("days_active",AchievementType.HARDWORKING),
    PASS_TEST("tests_passed",AchievementType.TEST_SLAYER),
    LEARN_WORD("words_learned",AchievementType.TRUTH_SEEKER),
    WATCH_FILM("films_watched",AchievementType.CINEMADDICT),
    READ_TEXT("texts_read",AchievementType.BOOKWORM);

    //столбцы метрик по столбцам достижений
    private static final Map<String,String> connectedAchievementColumns =
            Map.of("hardworking","days_active","truth_seeker","words_learned","test_slayer",
                    "tests_passed","bookworm","books_read","cinemaddict","films_watched");
    private final String columnInBase;
    private final AchievementType achievementType;
    ActionType(String columnInBase, AchievementType achievementType){
        this.columnInBase = columnInBase;
        this.achievementType = achievementType;
    }

    public String getColumnForUpdate(){
        return this.columnInBase;
    }

    public AchievementType getAchievementType() {
        return achievementType;
    }

    //иммутабельная коллекция
    public static Map<String, String> getConnectedAchievementColumns() {
        return connectedAchievementColumns;
    }

    public static ActionType getRandomAction(){
        List<ActionType> actionTypeList = Arrays.stream(ActionType.values()).
                filter(x -> !x.equals(DAY_SIGN)).toList();
        return actionTypeList.get(new Random().nextInt(ActionType.values().length-1));
    }

}
