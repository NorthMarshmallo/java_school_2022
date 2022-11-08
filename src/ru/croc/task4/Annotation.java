package ru.croc.task4;
import ru.croc.task4.Figure;

public class Annotation {
    private final String label;
    private final Figure figure;

    public Annotation(Figure figure, String label){
        this.label = label;
        this.figure = figure;
    }

    @Override
    public String toString(){
        return this.figure.getName() + " " + this.figure.getParams() + ": " + this.label;
    }

    public boolean checkInFigure(double x, double y) {
        return this.figure.checkPointIn(x, y);
    }

    public boolean checkInLabel(String str){
        return this.label.contains(str);
    }

    public void moveFigure(double dx, double dy){
        this.figure.move(dx, dy);
    }
}

