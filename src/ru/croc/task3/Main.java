package ru.croc.task3;
import java.util.Scanner;

public class Main {
    static class Point {
        double x, y;
    }

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        Point a = new Point();
        a.x = in.nextDouble();
        a.y = in.nextDouble();

        Point b = new Point();
        b.x = in.nextDouble();
        b.y = in.nextDouble();

        Point c = new Point();
        c.x = in.nextDouble();
        c.y = in.nextDouble();

        // использовано векторное произведение векторов на ab и ac, на которых строится треугольник
        // третья координата взята равной нулю для каждого вектора
        // итого в произведении остается только одно слагаемое с определителем
        double det = (b.x - a.x) * (c.y - a.y) - (c.x - a.x) * (b.y - a.y);
        double area = 0.5 * Math.sqrt( Math.pow(det, 2) );

        System.out.println("Area of the triangle: " + area);
        in.close();
    }
}
