package ru.croc.task16;

import java.util.HashSet;

public class Driver {

    private String id;
    private Coordinates coordinates;
    private String comfortClass;
    private HashSet<String> facilities;

    public Driver(String id, Coordinates coordinates, String comfortClass, HashSet<String> facilities){
        this.id = id;
        this.comfortClass = comfortClass;
        this.coordinates = coordinates;
        this.facilities = facilities;
    }

    public String getId() {
        return id;
    }

    public HashSet<String> getFacilities() {
        return facilities;
    }

    public String getComfortClass() {
        return comfortClass;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }
}
