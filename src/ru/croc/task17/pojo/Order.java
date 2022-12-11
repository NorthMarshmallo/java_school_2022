package ru.croc.task17.pojo;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private final Integer orderNum;
    private List<Product> products;
    private int cost;

    private final Customer customer;

    public Order(Integer orderNum,Customer customer,Product product){

        this.customer = customer;
        this.orderNum = orderNum;
        this.products = new ArrayList<>();

        updateProducts(product);

    }

    public void updateProducts(Product product){

        this.products.add(product);

    }

    public List<Product> getProducts(){
        return new ArrayList<>(this.products);
    }

    public String getCustomerName() {
        return customer.getName();
    }

    public int getCost(){
        this.cost = products.stream().mapToInt(Product::getPrice).sum();
        return cost;
    }
}
