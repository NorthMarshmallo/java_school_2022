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
        orders.values().forEach(order -> getOrdersDbInfo(order));

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

            String art = product.getArt();
            String sql = "UPDATE PRODUCT SET (art,title,price) = (" + product + ") WHERE art = '" + art + "'";
            statement.executeUpdate(sql);
            updatedProduct = findProduct(art);

            /*в сохраненной базе также нужно обновить данные о продукте,
            но возвращать его так, чтобы пользователь мог изменить не хотим,
            поэтому возвращаем полученный выше объект, обновляем отдельно*/
            if (updatedProduct != null) {
                Product productInBase = products.get(art);
                productInBase.setPrice(updatedProduct.getPrice());
                productInBase.setTitle(updatedProduct.getTitle());
                productInBase.setId(updatedProduct.getId());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return updatedProduct;

    }

    @Override
    public void deleteProduct(String productCode) {

        products.remove(productCode);
        try (Statement statement = connection.createStatement()) {

            String sql = "SELECT * FROM ORDERSPRODUCTS record" +
                    " WHERE record.art = '" + productCode + "'";

            /* P.s.: упоминания о заказе удаляются, стоимость уже выданных заказов
            и потраченная пользователем сумма остаются, тк деньги уже оплачены, а
            удалить требуют только упоминания о товарах; аналогично при обновлении
            информации о товаре ранее выданные заказы остаются с той же стоимостью */

            Order orderToChange = null;
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                int orderNum = rs.getInt("orderNum");
                orderToChange = orders.get(orderNum);
                List<Product> products = orderToChange.getProducts();
                products.remove(productCode);
                orderToChange.getStrsInOP().remove(Integer.valueOf(rs.getInt("strInOP")));
                if (products.isEmpty())
                    orders.remove(orderNum);
            }


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

        Order createdOrder = null;
        try (Statement statement = connection.createStatement()) {

            int orderNum = Collections.max(orders.keySet()) + 1;
            Customer customer = customers.get(userLogin);
            createdOrder = new Order(orderNum,customer,products);
            createdOrder.updateCost();
            customer.updateSpent();
            orders.put(orderNum,createdOrder);
            statement.executeUpdate("INSERT INTO ORDERS (orderNum,customerName,orderCost) VALUES " +
                    "(" + orderNum + ",'" + userLogin  + "'," + createdOrder.getCost() + ");" +
                    "UPDATE CUSTOMER SET spent = " + customer.getSpent() + " WHERE name = '" + userLogin + "'");
            for (Product product: products)
                statement.executeUpdate("INSERT INTO ORDERSPRODUCTS (orderNum,art) VALUES (" + orderNum + ",'" +
                        product.getArt() + "')");

            getOrdersDbInfo(createdOrder);

        } catch (NullPointerException ne){
            throw ne;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return createdOrder;

    }

    private void getOrdersDbInfo(Order order){
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT o.strInOrders FROM ORDERS o" +
                    " WHERE o.orderNum = " + order.getOrderNum());
            if (rs.next())
                order.setStrInOrders(rs.getInt("strInOrders"));
            rs = statement.executeQuery("SELECT op.strInOP FROM ORDERSPRODUCTS op" +
                    " WHERE op.orderNum = " + order.getOrderNum());
            List<Integer> strList = new ArrayList<>();
            while (rs.next())
                strList.add(rs.getInt("strInOP"));
            order.setStrsInOp(strList);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Map<String, Product> getProducts() {
        return new HashMap<>(products);
    }
}
