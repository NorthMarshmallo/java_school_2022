package ru.croc.task17.pojo;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private final String name;
    private List<Order> orders;
    private Integer id;
    private int spent;

    public Customer(Integer id, String name){

        this.name = name;
        this.id = id;
        this.orders = new ArrayList<>();

    }

    public void updateOrders(Order order){

        this.orders.add(order);

    }
    public Integer getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public void updateSpent(){

        this.spent = orders.stream().mapToInt(Order::getCost).sum();

    }
    public int getSpent() {

        return spent;
    }

    @Override
    public String toString() { return  id + ",'" + name + "'," + spent;
    }

    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }
}
