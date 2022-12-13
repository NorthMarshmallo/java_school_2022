package ru.croc.task19;


import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.List;

public class WebStore19 {

    private static WebStoreDaoTime wsDaoTime;
    private static DatabaseTimeCreator dbtc;

    public static void main(String[] args){

        String url = "jdbc:h2:~/ru/croc/task17/Database/WebStoreDb";
        dbtc = new DatabaseTimeCreator();
        dbtc.createDatabase("src\\ru\\croc\\task19\\OrdersWithDelivery.csv",
                url);
        List<String> tables = Arrays.asList("CUSTOMER","PRODUCT","ORDERS","ORDERSPRODUCTS","COURIER");
        dbtc.printTables(tables, "src\\ru\\croc\\task19\\txtTables\\CreatedTables.txt");

        try(Connection connection = DriverManager.getConnection(url,"sa","")) {

            wsDaoTime = new WebStoreDaoTime(connection, dbtc);
            System.out.println("Результат поиска заказов для курьеров:");
            System.out.println();
            checkCourierOrders(1); //в базе
            checkCourierOrders(5); //нет в базе
            System.out.println("Результат поиска заказов для пользователя:");
            System.out.println();
            checkCustomerOrders(2); //в базе
            checkCustomerOrders(10); //нет в базе

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private static void checkCustomerOrders(Integer id){

        try{
            System.out.println("Пользователю с id " + id + " должны прийти следующие заказы:");
            wsDaoTime.getCustomerOrders(id).entrySet().stream().forEach(System.out::println);
            System.out.println();
        } catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println();
        }

    }

    private static void checkCourierOrders(Integer id){

        try{
            System.out.println("Курьер с id " + id + " должен доставить следующие заказы:");
            wsDaoTime.getCourierOrders(id).entrySet().stream().forEach(System.out::println);
            System.out.println();
        } catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println();
        }

    }

}
