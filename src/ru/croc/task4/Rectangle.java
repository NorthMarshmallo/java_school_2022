package ru.croc.task4;

public class Rectangle extends Figure {
    public String getName() {
        return "Rectangle";
    }

    private Point leftDownPoint, rightUpPoint;

    public Rectangle(Point prm1, Point prm2) {
        this.leftDownPoint = prm1;
        this.rightUpPoint = prm2;
    }

    @Override
    public String getParams() {
        return this.leftDownPoint.view() + ", " + this.rightUpPoint.view();
    }

    @Override
    public Boolean checkPointIn(double x, double y) {

        if ((x - this.leftDownPoint.getX() >= 0) && (x - this.rightUpPoint.getX() <= 0) &&
                (y - this.leftDownPoint.getY() >= 0) && (y - this.rightUpPoint.getY() <= 0)) {
            // проверяем на нахождение внутри границ
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void move(double dx, double dy) {
        this.leftDownPoint = new Point(this.leftDownPoint.getX() + dx, this.leftDownPoint.getY() + dy);
        this.rightUpPoint = new Point(this.rightUpPoint.getX() + dx, this.rightUpPoint.getY() + dy);
    }
}
