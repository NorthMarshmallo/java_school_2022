package ru.croc.task19.pojo;

import java.util.Objects;

public class Courier {

    private Integer id = null;
    private String number;
    private String name;
    private String surname;

    public Courier(String name, String surname, String number) {

        this.number = number;
        this.surname = surname;
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getNumber() {
        return number;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Courier courier))
            return false;

        return Objects.equals(name, courier.name) && Objects.equals(surname, courier.surname) && Objects.equals(number, courier.number);

    }

    @Override
    public int hashCode() {
        return Objects.hash(name,surname,number);
    }

    public String getFullName() {
        return name + " " + surname;
    }

}
