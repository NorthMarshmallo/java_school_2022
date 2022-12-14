package ru.croc.task4;

public class Point {
    private final double x, y;

    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String view(){
        return "(" + this.x + ", " + this.y + ")";
    }

}
