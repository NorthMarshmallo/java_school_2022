package ru.croc.task19;

import ru.croc.task17.pojo.Customer;
import ru.croc.task17.pojo.Order;
import ru.croc.task17.pojo.Product;
import ru.croc.task18.dao.OrderDaoImpl;
import ru.croc.task18.dao.ProductDao;
import ru.croc.task18.dao.ProductDaoImpl;
import ru.croc.task19.dao.CourierDao;
import ru.croc.task19.dao.CustomerDao;
import ru.croc.task19.pojo.Courier;
import ru.croc.task19.pojo.OrderTime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImplTime extends OrderDaoImpl {

    public OrderDaoImplTime(Connection connection){

        super(connection);

    }

    public List<OrderTime> getCustomerOrders(Integer customerId){

        List<OrderTime> orderList = new ArrayList<>();
        OrderTime orderTime;
        CustomerDao customerDao = new CustomerDao(connection);

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM CUSTOMER_ORDER " +
                "JOIN COURIER ON customer_order.courier_id = courier.id WHERE " +
                "customer_order.customer_id = ?")){

            statement.setInt(1,customerId);
            ResultSet rs = statement.executeQuery();

            String customerName = customerDao.getCustomerNameById(customerId);

            while (rs.next()){

                Integer orderNum = rs.getInt("order_num");
                orderTime = new OrderTime(new Order(orderNum,new Customer(customerId,
                        customerName),getOrderListOfProducts(rs.getInt("order_num"))),
                        rs.getTimestamp("time"),new Courier(rs.getString("number"),
                        rs.getString("name"),rs.getString("surname")));
                orderList.add(orderTime);

            }

        } catch (RuntimeException re){
            throw re;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return orderList;

    }

    public List<OrderTime> getCourierOrders(Integer courierId){

        List<OrderTime> orderList = new ArrayList<>();
        OrderTime orderTime;
        CourierDao courierDao = new CourierDao(connection);

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM CUSTOMER_ORDER " +
                "JOIN CUSTOMER ON customer_order.customer_id = customer.id WHERE " +
                "customer_order.courier_id = ?")){

            statement.setInt(1,courierId);
            ResultSet rs = statement.executeQuery();

            Courier courier = courierDao.getCourierById(courierId);

            while (rs.next()){

                Integer orderNum = rs.getInt("order_num");
                Integer customerId = rs.getInt("customer_id");
                orderTime = new OrderTime(new Order(orderNum,new Customer(customerId,
                        rs.getString("name")),getOrderListOfProducts(rs.getInt("order_num"))),
                        rs.getTimestamp("time"),courier);
                orderList.add(orderTime);

            }

        } catch (RuntimeException re){
            throw re;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return orderList;

    }

    public List<Product> getOrderListOfProducts(Integer orderNum){

        List<Product> productList = new ArrayList<>();
        ProductDao productDao = new ProductDaoImpl(connection);

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM ORDER_PRODUCT " +
                "WHERE order_num = ?")){

            statement.setInt(1,orderNum);
            ResultSet rs = statement.executeQuery();

            while (rs.next()){

                productList.add(productDao.findProduct(rs.getString("article")));

            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return productList;

        }

}



