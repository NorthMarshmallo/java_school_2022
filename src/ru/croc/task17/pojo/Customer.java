package ru.croc.task17.pojo;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private final String name;
    private List<Order> orders;
    private int spent;

    public Customer(String name){

        this.name = name;
        this.orders = new ArrayList<>();

    }

    public void updateOrders(Order order){

        this.orders.add(order);

    }
    public String getName(){
        return name;
    }

    public int getSpent() {
        //считается здесь, чтобы не контролировать при каждом добавлении продукта в заказ
        this.spent = orders.stream().mapToInt(Order::getCost).sum();
        return spent;
    }

    @Override
    public String toString() { getSpent();

        return "'" + name + "'," + spent;
    }

}
