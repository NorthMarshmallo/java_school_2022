package ru.croc.task16;

import ru.croc.task16.enums.ComfortClass;
import ru.croc.task16.enums.Facility;

import java.util.Set;

public class Driver {

    private String id;
    private Coordinates coordinates;
    private ComfortClass comfortClass;
    private Set<Facility> facilities;

    public Driver(String id, Coordinates coordinates, ComfortClass comfortClass, Set<Facility> facilities){
        this.id = id;
        this.comfortClass = comfortClass;
        this.coordinates = coordinates;
        this.facilities = facilities;
    }

    public String getId() {
        return id;
    }

    public Set<Facility> getFacilities() {
        return facilities;
    }

    public ComfortClass getComfortClass() {
        return comfortClass;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }
}
