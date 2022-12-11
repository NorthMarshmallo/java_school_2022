package ru.croc.task18;

import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import ru.croc.task17.DatabaseCreator;
import ru.croc.task17.pojo.Customer;
import ru.croc.task17.pojo.Order;
import ru.croc.task17.pojo.Product;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class WebStoreDao implements WebStoreDaoImpl {

    private Connection connection;
    //продукты по артикулам
    private Map<String, Product> products;
    //заказчики по ид
    private Map<String, Customer> customers;
    //заказы по номеру
    private Map<Integer,Order> orders;

    public WebStoreDao(Connection connection, DatabaseCreator dbc){

        this.connection = connection;
        this.products = dbc.getProducts();
        this.customers = dbc.getCustomers();
        this.orders = dbc.getOrders();

    }

    @Override
    public Product findProduct(String productCode) {

        try (Statement statement = connection.createStatement()) {

            String sql = "SELECT * FROM PRODUCT p " +
                    "WHERE p.art = '" + productCode + "'";
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                Product foundProduct = new Product(rs.getString("art"), rs.getString("title"),
                        rs.getInt("price"));
                foundProduct.setId(rs.getInt("id"));
                return foundProduct;
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public Product createProduct(Product product) throws JdbcSQLIntegrityConstraintViolationException{

        Product createdProduct = null;
        try (Statement statement = connection.createStatement()) {

            String sql = "INSERT INTO PRODUCT (art,title,price) VALUES (" + product + ")";
            statement.executeUpdate(sql);
            createdProduct = findProduct(product.getArt());
            this.products.put(product.getArt(),product);

        } catch (JdbcSQLIntegrityConstraintViolationException doubleKey){
            throw doubleKey;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return createdProduct;

    }

    @Override
    public Product updateProduct(Product product) {

        Product updatedProduct = null;
        try (Statement statement = connection.createStatement()) {

            String sql = "UPDATE PRODUCT SET (art,title,price) = (" + product + ") WHERE art = '" + product.getArt() + "'";
            statement.executeUpdate(sql);
            updatedProduct = findProduct(product.getArt());

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return updatedProduct;

    }

    @Override
    public void deleteProduct(String productCode) {

        try (Statement statement = connection.createStatement()) {

            String sql = "SELECT record.orderNum FROM ORDERSPRODUCTS record" +
                    " WHERE record.art = '" + productCode + "'";

            ResultSet rs = statement.executeQuery(sql);
            Set<Integer> ordersToChange = new HashSet<>(Arrays.as(rs.getArray("orderNum")));

            /* удалит запись из таблицы продуктов, из таблицы связи продуктов с заказами
            все удалится благодаря on delete cascade, заданном при создании;
            в завершении удалит запись из таблицы заказов, если продуктов в
            заказе не останется после всех удалений */
            sql = "DELETE FROM PRODUCT WHERE art = '" + productCode + "'; " +
                    "DELETE FROM ORDERS " +
                    "WHERE orderNum NOT IN " +
                    "(SELECT record.orderNum FROM ORDERSPRODUCTS record)";
            statement.executeUpdate(sql);

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public Order createOrder(String userLogin, List<Product> products) {
        return null;
    }
}
