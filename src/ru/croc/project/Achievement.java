package ru.croc.project;

import ru.croc.project.enums.AchievementType;

import java.io.Serial;
import java.io.Serializable;

public class Achievement implements Serializable,Cloneable {

    @Serial
    private static final long serialVersionUID = 1L;
    private final AchievementType type;
    private final String name;
    private final String trigger;
    private Integer level;
    private Integer points;
    private Integer metric;

    public Achievement(AchievementType type, Integer points){

        this.type = type;
        this.points = points;
        this.trigger = type.getTriggerAction();
        //поставим самый низкий для данного достижения уровень
        this.level = type.getLevelThresholds().firstEntry().getValue();
        this.name = type.getStrName();

    }

    public void setLevel(Integer level) {
        this.level = level;
        this.points = type.getLevelsPoints().get(level);
    }

    public void setMetric(Integer metric){
        this.metric = metric;
    }

    public void setLevelAndMetric(Integer level, Integer metric){
        setMetric(metric);
        setLevel(level);
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getPoints() {
        return points;
    }

    public String getMessage(){
        if (level == 0)
            return "Congratulations! You " + trigger + " and hit the '" + name
                    + "' achievement. Your award: " + points;
        else
            return "Congratulations! You " + trigger.replaceAll("\\?",metric.toString()) + " and reached level " + level +
                    " of '" + name + "' achievement. Your award: " + points;
    }

    @Override
    public String toString() {
        if (level == 0)
            return "'" + name + "' is achieved";
        else if (level > 0)
            return "'" + name + "' " + level + " lvl";
        else return "'" + name + "' is not achieved yet";
    }

    //объект будет посылаться с сервера, не хотелось бы, чтобы во время обработки на сервере, какой-то клиент мог
    //изменить значения полей
    @Override
    public Achievement clone() {
        try {
            Achievement clone = (Achievement) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            // (all mutable)
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
