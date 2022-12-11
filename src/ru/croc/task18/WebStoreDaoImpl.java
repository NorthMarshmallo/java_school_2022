package ru.croc.task18;

import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import ru.croc.task17.pojo.Order;
import ru.croc.task17.pojo.Product;

import java.util.List;

public interface WebStoreDaoImpl {

    /**
     * From the given iterable collection of comments removes ones
     * by filterCondition predicate and returns result as a list of filter's type.
     *
     * @param comments  iterable collection of comments; every comment
     *                  is a sequence of words, separated
     *                  by spaces, punctuation or line breaks
     * @param filterCondition predicate-condition to detect your blacklist words
     */

    /**
     * Finds the product with given productCode(art) in database.
     * Returns null in case there are no such product.
     * @param productCode string that is an article
     */
    Product findProduct(String productCode);

    /**
     * Creates new product in database. Returns Exception
     * in case product with provided article already exists.
     * @param product item of Product class to create in database
     */
    Product createProduct(Product product) throws JdbcSQLIntegrityConstraintViolationException;

    /**
     * Changes info related to a product with the same article as
     * the provided one has. Sets provided title and price.
     * @param product item of Product class to get title and price from
     */
    Product updateProduct(Product product);

    /**
     * Deletes the product and mentions of it in orders tables.
     * @param productCode the article of product to delete
     */
    void deleteProduct(String productCode);

    /**
     * Creates every needed records for an order in database.
     * There will be a new order in ORDERS and a new list of
     * products in ORDERSPRODUCTS.
     * @param userLogin name of customer who orders it
     * @param products list of ordered products
     */
    Order createOrder(String userLogin, List<Product> products);

}

