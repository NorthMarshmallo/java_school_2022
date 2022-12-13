package ru.croc.task19;

import ru.croc.task17.DatabaseCreator;
import ru.croc.task17.pojo.Customer;
import ru.croc.task19.pojo.Courier;
import ru.croc.task19.pojo.OrderTime;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class DatabaseTimeCreator extends DatabaseCreator {

    //ид через курьеров с помощью equals и hashcode(нужно для задания курьеров в базу и получения из нее ид)
    private Map<Courier,Integer> couriers;
    /*курьеры по ид для дальнейшей передачи дао(там при создании нового курьера уже
    можно будет просто добавлять его в базу получать индекс и сохранять по нему)*/
    private Map<Integer,Courier> couriersOutput;
    //пользователи по ид для дао
    private Map<Integer, Customer> customersOutput;
    //обновленный вид заказов по номеру
    private Map<Integer, OrderTime> ordersTime;

    public DatabaseTimeCreator(){
        super();
        this.couriers = new HashMap<>();
        this.ordersTime = new HashMap<>();
        this.customersOutput = new HashMap<>();
        this.couriersOutput = new HashMap<>();
    }
    public void createDatabase(String fileName, String dbAdress) {

        super.createDatabase(fileName,dbAdress);
        try(Connection connection = DriverManager.getConnection(dbAdress, "sa","")) {

            addTimeFeaturesToTables(connection);
            getTimeInfo(connection);

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void getTimeInfo(Connection connection){

        try (BufferedReader br = new BufferedReader(new FileReader(super.fileName));
             Statement statement = connection.createStatement()) {
            String line;
            Integer courierId = null;
            while ((line = br.readLine()) != null) {

                String[] pl = line.split(","); //pl for parsedLine
                int orderNum = Integer.parseInt(pl[0]);
                String customerName = pl[1], courierNumber = pl[6], courierSurame = pl[7], courierName = pl[8];
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime time = LocalDateTime.parse(pl[5], formatter);


                Courier courier = new Courier(courierNumber, courierName, courierSurame);

                if (couriers.get(courier) == null) {
                    statement.executeUpdate("INSERT INTO COURIER (name,surname,number) VALUES ('" + courierName +
                            "', '" + courierSurame + "', '" + courierNumber + "')");
                    ResultSet rs = statement.executeQuery("SELECT id FROM COURIER c WHERE name = '" + courierName +
                            "' AND surname = '" + courierSurame + "' AND number = '" + courierNumber + "'");
                    if (rs.next()) {
                        courierId = rs.getInt(1);
                        courier.setId(courierId);
                        couriers.put(courier,courierId);
                        couriersOutput.put(courierId,courier);
                    }
                } else {
                    courierId = couriers.get(courier);
                }

                if (ordersTime.get(orderNum) == null){
                    try(PreparedStatement prst = connection.prepareStatement(
                            "UPDATE ORDERS SET (time,courierId) = ( ?, ?) WHERE orderNum = ?")){
                        prst.setObject(1,time);
                        prst.setInt(2,courierId);
                        prst.setInt(3,orderNum);
                        prst.executeUpdate();
                    }
                    Courier orderCourier = couriersOutput.get(courierId); //не подходит обычный courier, объявленный выше
                    //т.к. в нем может не быть ид
                    OrderTime orderTime = new OrderTime(orders.get(orderNum),time,orderCourier);
                    ordersTime.put(orderNum, orderTime);
                    orderCourier.addOrder(orderTime);
                }

                Customer customer = customers.get(customerName);
                customersOutput.put(customer.getId(),customer);

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    private void addTimeFeaturesToTables(Connection connection){

        try (Statement statement = connection.createStatement()) {

            statement.execute("CREATE TABLE COURIER" +
                    "(id INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "surname VARCHAR(255) NOT NULL, " +
                    "number VARCHAR(255) NOT NULL)");

            statement.executeUpdate("ALTER TABLE ORDERS " +
                    "ADD (time TIMESTAMP," +
                    "courierId INTEGER," +
                    "FOREIGN KEY (courierId) REFERENCES COURIER(id))");

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public Map<Integer, Courier> getCouriersOutput() {
        return new HashMap<>(couriersOutput);
    }

    public Map<Integer, Customer> getCustomersOutput() {
        return new HashMap<>(customersOutput);
    }

    public Map<Integer, OrderTime> getOrdersTime() {
        return new HashMap<>(ordersTime);
    }
}
