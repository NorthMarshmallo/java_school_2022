package ru.croc.task18;

import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import ru.croc.task17.DatabaseCreator;
import ru.croc.task17.pojo.Customer;
import ru.croc.task17.pojo.Order;
import ru.croc.task17.pojo.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class WebStoreDaoImpl implements WebStoreDao {

    protected Connection connection;
    //продукты по артикулам
    protected Map<String, Product> products;
    //заказчики по ид
    protected Map<String, Customer> customers;
    //заказы по номеру
    protected Map<Integer,Order> orders;

    public WebStoreDaoImpl(Connection connection, DatabaseCreator dbc){

        this.connection = connection;
        this.products = dbc.getProducts();
        this.customers = dbc.getCustomers();
        this.orders = dbc.getOrders();
        orders.values().forEach(order -> getOrdersDbInfo(order));

    }

    @Override
    public Product findProduct(String productCode) {

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM PRODUCT p " +
                "WHERE p.art = ?")) {

            statement.setString(1,productCode);
            ResultSet rs = statement.executeQuery();
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
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO " +
                "PRODUCT (art,title,price) VALUES ( ?,?,? )")) {

            statement.setString(1,product.getArt());
            statement.setString(2,product.getTitle());
            statement.setInt(3,product.getPrice());
            statement.executeUpdate();
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
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE PRODUCT SET (art,title,price) = (" + product + ") WHERE art = ?"
        )) {

            String art = product.getArt();
            statement.setString(1,art);
            statement.executeUpdate();
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
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM ORDERSPRODUCTS record" +
                " WHERE record.art = ?")) {

            /* P.s.: упоминания о заказе удаляются, стоимость уже выданных заказов
            и потраченная пользователем сумма остаются, тк деньги уже оплачены, а
            удалить требуют только упоминания о товарах; аналогично при обновлении
            информации о товаре ранее выданные заказы остаются с той же стоимостью */

            Order orderToChange = null;
            statement.setString(1,productCode);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                int orderNum = rs.getInt("orderNum");
                orderToChange = orders.get(orderNum);
                List<Product> products = orderToChange.getProducts();
                products.remove(productCode);
                orderToChange.getStrsInOP().remove(Integer.valueOf(rs.getInt("strInOP")));
                if (products.isEmpty())
                    orders.remove(orderNum);
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM PRODUCT WHERE art = ? ; " +
                        "DELETE FROM ORDERS " +
                        "WHERE orderNum NOT IN " +
                        "(SELECT record.orderNum FROM ORDERSPRODUCTS record)"
        )){

            /* удалит запись из таблицы продуктов, из таблицы связи продуктов с заказами
            все удалится благодаря on delete cascade, заданном при создании;
            в завершении удалит запись из таблицы заказов, если продуктов в
            заказе не останется после всех удалений */

            statement.setString(1,productCode);
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Order createOrder(String userLogin, List<Product> products) {

        Order createdOrder = null;
        try (PreparedStatement orderStatement = connection.prepareStatement(
                "INSERT INTO ORDERS (orderNum,customerName,customerId,orderCost) VALUES (?,?,?,?);" +
                        "UPDATE CUSTOMER SET spent = ? WHERE name = ?" )) {

            int orderNum = Collections.max(orders.keySet()) + 1;
            Customer customer = customers.get(userLogin);
            createdOrder = new Order(orderNum,customer,products);
            createdOrder.updateCost();
            customer.updateSpent();
            orders.put(orderNum,createdOrder);
            orderStatement.setInt(1,orderNum);
            orderStatement.setString(2,userLogin);
            orderStatement.setInt(3,customer.getId());
            orderStatement.setInt(4,createdOrder.getCost());
            orderStatement.setInt(5,customer.getSpent());
            orderStatement.setString(6,userLogin);
            orderStatement.executeUpdate();
            for (Product product: products) {
                PreparedStatement productStatement = connection.prepareStatement("INSERT INTO " +
                        "ORDERSPRODUCTS (orderNum,art) VALUES (?,?)");
                productStatement.setInt(1,orderNum);
                productStatement.setString(2,product.getArt());
                productStatement.executeUpdate();
                productStatement.close();
            }
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
