package ru.croc.task17.pojo;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private final Integer orderNum;
    private List<Product> products;

    private final String customerName;

    public Order(Integer orderNum,String customerName,Product product){

        this.customerName = customerName;
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
        return customerName;
    }
}
