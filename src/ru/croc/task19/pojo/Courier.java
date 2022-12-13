package ru.croc.task19.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Courier {

    private Integer id = null;
    private String number;
    private String name;
    private String surname;
    private List<OrderTime> orders;

    public Courier(String number, String name, String surname) {

        this.number = number;
        this.surname = surname;
        this.name = name;
        this.orders = new ArrayList<>();

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void addOrder(OrderTime order){
        orders.add(order);
    }

    public List<OrderTime> getOrders() {
        return orders;
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
