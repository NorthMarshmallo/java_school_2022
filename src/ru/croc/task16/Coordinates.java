package ru.croc.task16;

public class Coordinates {

    private double latitude, longtitude;

    public Coordinates(double latitude, double longtitude){
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public Coordinates(String[] coordinatesList){
        this.latitude = Double.parseDouble(coordinatesList[0]);
        this.longtitude = Double.parseDouble(coordinatesList[1]);
    }

    @Override
    public String toString() {
        return this.latitude + ", " + this.longtitude;
    }

    public static double compare(Coordinates c1, Coordinates c2){
        return Math.sqrt(Math.pow(c1.latitude - c2.latitude,2) + Math.pow(c1.longtitude - c2.longtitude,2));
    }
}
