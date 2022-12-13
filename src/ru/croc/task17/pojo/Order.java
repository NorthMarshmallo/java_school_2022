package ru.croc.task17.pojo;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private final Integer orderNum;
    private List<Product> products;
    private int cost;
    private final Customer customer;
    private int strInOrders;
    private List<Integer> strsInOp;

    public Order(Integer orderNum,Customer customer,Product product){

        this.customer = customer;
        this.orderNum = orderNum;
        this.products = new ArrayList<>();

        updateProducts(product);

    }
    public Order(Integer orderNum,Customer customer,List<Product> products){

        this.customer = customer;
        this.orderNum = orderNum;
        this.products = products;

    }

    public void updateProducts(Product product){

        this.products.add(product);

    }

    public List<Product> getProducts(){
        return new ArrayList<>(this.products);
    }

    public Customer getCustomer() {
        return customer;
    }

    public Integer getCustomerId() {
        return customer.getId();
    }

    public String getCustomerName() {
        return customer.getName();
    }

    public void updateCost(){

        this.cost = products.stream().mapToInt(Product::getPrice).sum();

    }

    public int getCost(){

        return cost;

    }

    public int getStrInOrders() {
        return strInOrders;
    }

    public void setStrInOrders(int strInOrders) {
        this.strInOrders = strInOrders;
    }

    public List<Integer> getStrsInOP() {
        return strsInOp;
    }

    public void setStrsInOp(List<Integer> strsInOp) {
        this.strsInOp = strsInOp;
    }

    public Integer getOrderNum() {
        return orderNum;
    }
}
