package ru.croc.task4;

public abstract class Figure implements Movable {
    public abstract String getName();
    public abstract String getParams();

    public abstract Boolean checkPointIn(double x,double y);

}