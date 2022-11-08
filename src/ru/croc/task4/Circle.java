package ru.croc.task4;

public class Circle extends Figure {

    public String getName() {
        return "Circle";
    }
    private Point center;
    private double radius;

    public Circle(Point prm1, double prm2){
        this.center = prm1;
        this.radius = prm2;
    }

    @Override
    public String getParams() {
        return this.center.view() + ", " + this.radius;
    }

    @Override
    public Boolean checkPointIn(double x, double y) {
        if (Math.pow((x-this.center.getX()),2) + Math.pow((y-this.center.getY()),2) <= Math.pow(this.radius,2)){
            // проверяем евклидово расстояние от центра
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void move(double dx, double dy) {
        this.center = new Point(this.center.getX() + dx, this.center.getY() + dy);
    }
}
