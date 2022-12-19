package ru.croc.task18.dao;

import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import ru.croc.task17.pojo.Product;

public interface ProductDao {

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
     * @param article the article of product to delete
     */
    void deleteProduct(String article);

}
