package ru.croc.task4;
import ru.croc.task4.Figure;

public class Annotation {
    private final String label;
    private final Figure figure;

    Annotation(Figure figure, String label){
        this.label = label;
        this.figure = figure;
    }

    @Override
    public String toString(){
        return this.figure.getName() + " " + this.figure.getParams() + ": " + this.label;
    }
}
abstract class Figure {
    public abstract String getName();
    public abstract String getParams();

}

class Circle extends Figure {

    public String getName() {
        return "Circle";
    }
    private final Point center;
    private final double radius;

    Circle(Point prm1, double prm2){
        this.center = prm1;
        this.radius = prm2;
    }

    @Override
    public String getParams() {
        return this.center.view() + ", " + this.radius;
    }
}
class Rectangle extends Figure{
    public String getName() {
        return "Rectangle";
    }
    private final Point leftDownPoint, rightUpPoint;

    Rectangle(Point prm1, Point prm2){
        this.leftDownPoint = prm1;
        this.rightUpPoint = prm2;
    }

    @Override
    public String getParams() {
        return this.leftDownPoint.view() + ", " + this.rightUpPoint.view();
    }
}

class Point {
    private final double x, y;

    Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    public String view(){
        return "(" + this.x + ", " + this.y + ")";
    }

}
