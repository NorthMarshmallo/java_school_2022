package ru.croc.task19.pojo;

import ru.croc.task17.pojo.Order;

import java.sql.Timestamp;

public class OrderTime {

    private Timestamp time;
    private Courier courier;
    private Order order;

    public OrderTime(Order order, Timestamp time, Courier courier){

        this.order = order;
        this.time = time;
        this.courier = courier;

    }

    public Timestamp getTime() {
        return time;
    }

    public Courier getCourier() {
        return courier;
    }

    public Integer getOrderNum(){
        return order.getOrderNum();
    }

    public String getCustomerName(){
        return order.getCustomerName();
    }
}
