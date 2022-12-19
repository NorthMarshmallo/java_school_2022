package ru.croc.task18.dao;

import ru.croc.task17.pojo.Order;
import ru.croc.task17.pojo.Product;

import java.util.List;

public interface OrderDao {

    /**
     * Creates every needed records for an order in database.
     * There will be a new order in ORDERS and a new list of
     * products in ORDERSPRODUCTS.
     * @param customerName name of customer who orders it
     * @param products list of ordered products
     */
    Order createOrder(String customerName, List<Product> products);

}
