package ru.croc.task19.pojo;

import ru.croc.task17.pojo.Order;

import java.time.LocalDateTime;

public class OrderTime {

    private LocalDateTime time;
    private Courier courier;
    private Order order;

    public OrderTime(Order order, LocalDateTime time, Courier courier){

        this.order = order;
        this.time = time;
        this.courier = courier;

    }

    public LocalDateTime getTime() {
        return time;
    }

    public Integer getCourierId() {
        return courier.getId();
    }

    public void setTime(String LocalDateTime) {
        this.time = time;
    }

    public void setCourierId(Courier courier) {
        this.courier = courier;
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
