package ru.croc.task18.dao;

import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import ru.croc.task17.pojo.Customer;
import ru.croc.task17.pojo.Order;
import ru.croc.task17.pojo.Product;
import ru.croc.task19.dao.CustomerDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao{

    protected Connection connection;

    public OrderDaoImpl(Connection connection){

        this.connection = connection;

    }

    @Override
    public Order createOrder(String customerName, List<Product> products) {

        Order createdOrder = null;
        Customer customer;
        Integer customerId;
        Integer orderNum;
        CustomerDao customerDao = new CustomerDao(connection);

        try{

            customer = customerDao.getCustomerByName(customerName);
            customerId = customer.getId();

        } catch (NullPointerException ne) {
            throw ne;
        }

        //берем наименьший неиспользованный номер для заказа
        try {

            orderNum = getFreeOrderNum();

        } catch (NullPointerException ne){
            throw ne;
        }

        try (PreparedStatement orderStatement = connection.prepareStatement(
                "INSERT INTO CUSTOMER_ORDER (order_num,customer_id,order_cost) VALUES (?,?,?);" +
                        "UPDATE CUSTOMER SET spent = ? WHERE id = ?" )) {

            createdOrder = new Order(new Customer(customerId,customerName),products);
            createdOrder.setOrderNum(orderNum);
            createdOrder.updateCost();
            orderStatement.setInt(1,orderNum);
            orderStatement.setInt(3,createdOrder.getCost());
            orderStatement.setInt(2,customerId);
            orderStatement.setInt(4,customer.getSpent() + createdOrder.getCost());
            orderStatement.setInt(5,customerId);
            orderStatement.executeUpdate();

            for (Product product: products) {

                PreparedStatement productStatement = connection.prepareStatement("INSERT INTO " +
                        "ORDER_PRODUCT (order_num,article) VALUES (?,?)");
                productStatement.setInt(1,orderNum);
                productStatement.setString(2,product.getArticle());
                productStatement.executeUpdate();
                productStatement.close();
            }

            getOrdersDbInfo(createdOrder);

        } catch (JdbcSQLIntegrityConstraintViolationException jse){
            throw new RuntimeException("One of products does not exist in base");
        } catch (NullPointerException ne){
            throw ne;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return createdOrder;

    }

    public Integer getFreeOrderNum(){

        Integer orderNum = null;
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT TOP 1 t1.order_num + 1\n" +
                        "FROM CUSTOMER_ORDER t1\n" +
                        "WHERE NOT EXISTS(SELECT * FROM CUSTOMER_ORDER t2 WHERE t2.order_num = t1.order_num + 1)\n" +
                        "ORDER BY t1.order_num" )) {

            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                orderNum = rs.getInt(1);
            }


        } catch (NullPointerException ne){
            throw ne;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return orderNum;
    }

    private void getOrdersDbInfo(Order order){

        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT o.order_table_id FROM CUSTOMER_ORDER o" +
                    " WHERE o.order_num = " + order.getOrderNum());
            if (rs.next())
                order.setOrderTableId(rs.getInt("order_table_id"));
            rs = statement.executeQuery("SELECT op.order_product_table_id FROM ORDER_PRODUCT op" +
                    " WHERE op.order_num = " + order.getOrderNum());
            List<Integer> strList = new ArrayList<>();
            while (rs.next())
                strList.add(rs.getInt("order_product_table_id"));
            order.setOrderProductTableIds(strList);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

}
