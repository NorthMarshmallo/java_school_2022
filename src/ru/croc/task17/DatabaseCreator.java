package ru.croc.task17;

import ru.croc.task17.pojo.Customer;
import ru.croc.task17.pojo.Order;
import ru.croc.task17.pojo.Product;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DatabaseCreator {

    //продукты по артикулам
    private Map<String, Product> products;
    //заказчики по ид
    private Map<String,Customer> customers;
    //заказы по номеру
    private Map<Integer,Order> orders;

    private String fileName, dbAdress;

    public DatabaseCreator(){

        this.orders = new HashMap<>();
        this.products = new HashMap<>();
        this.customers = new HashMap<>();

    }

    private void setFileName(String fileName){
        this.fileName = fileName;
    }

    private void setDbAdress(String dbAdress){
        this.dbAdress = dbAdress;
    }

    public void createDatabase(String fileName, String dbAdress) {

        setFileName(fileName);
        setDbAdress(dbAdress);
        getInfoForTables();

        //если обращаются по старому адресу с просьбой СОЗДАТЬ базу данных, скорее всего хотят ее переписать
        //поэтому для начала удалим старые данные
        clearIfExist();

        try(Connection connection = DriverManager.getConnection(dbAdress, "sa","")) {

            Thread t1 = new Thread(() -> setProductTable(connection));
            Thread t2 = new Thread(() -> setCustomerTable(connection));
            t1.start();
            t2.start();
            t1.join();
            t2.join();
            setOrdersTables(connection);

            System.out.println("Created tables in given database...");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void clearIfExist(){
        try (Connection connection = DriverManager.getConnection(dbAdress, "sa","");
                Statement statement = connection.createStatement()) {
            statement.execute("DROP ALL OBJECTS DELETE FILES");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private void getInfoForTables(){
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {

                String[] pl = line.split(","); //pl for parsedLine
                String name = pl[1], art = pl[2], title = pl[3];
                int orderNum = Integer.parseInt(pl[0]), price = Integer.parseInt(pl[4]);

                Product product = new Product(art,title,price);
                products.putIfAbsent(art, product);

                Customer customer = new Customer(name);
                customers.putIfAbsent(name, customer);

                if (orders.get(orderNum) == null) {
                    Order order = new Order(orderNum, customer, product);
                    orders.put(orderNum, order);
                    customer.updateOrders(order);
                }
                else
                    orders.get(orderNum).updateProducts(product);

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void setProductTable(Connection connection){

        try (Statement statement = connection.createStatement()) {

            String createProductTable = "CREATE TABLE PRODUCT " +
                    "(id INTEGER NOT NULL AUTO_INCREMENT, " +
                    "art VARCHAR(255) PRIMARY KEY NOT NULL, " +
                    "title VARCHAR(255), " +
                    "price INTEGER NOT NULL)";
            //в целом название показывается только в данной таблице и ни с чем не связано, поэтому потенциально
            //может быть null

            statement.execute(createProductTable);
            for (String art : products.keySet()) {
                statement.executeUpdate("INSERT INTO PRODUCT (art,title,price) VALUES (" + products.get(art) + ")");
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setCustomerTable(Connection connection){

        try (Statement statement = connection.createStatement()) {

            String createCustomerTable = "CREATE TABLE CUSTOMER " +
                    "(name VARCHAR(255) PRIMARY KEY NOT NULL , " +
                    "spent INTEGER NOT NULL)";

            statement.execute(createCustomerTable);

            for (String id : customers.keySet()) {
                statement.executeUpdate("INSERT INTO CUSTOMER VALUES (" + customers.get(id) + ")");
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setOrdersTables(Connection connection){

        try (Statement statement = connection.createStatement()) {

            String createOrderTable = "CREATE TABLE ORDERS " +
                    "(strInOrders INT NOT NULL AUTO_INCREMENT, " +
                    "orderNum INT PRIMARY KEY NOT NULL, " +
                    "customerName VARCHAR(255) NOT NULL," +
                    "orderCost INT NOT NULL, "+
                    "FOREIGN KEY (customerName) REFERENCES CUSTOMER(name))";

            String createOrdersProductsTable = "CREATE TABLE ORDERSPRODUCTS " +
                    "(strInOP INT PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
                    "orderNum INT NOT NULL, " +
                    "art VARCHAR(255) NOT NULL, " +
                    "FOREIGN KEY (orderNum) REFERENCES ORDERS(orderNum), " +
                    "FOREIGN KEY (art) REFERENCES PRODUCT(art) " +
                    "ON DELETE CASCADE)";

            statement.execute(createOrderTable);
            statement.execute(createOrdersProductsTable);

            for (Integer orderNum : orders.keySet()) {
                Order order = orders.get(orderNum);
                statement.executeUpdate("INSERT INTO ORDERS (orderNum,customerName,orderCost) VALUES (" + orderNum +
                        ", '" + order.getCustomerName() + "', " + order.getCost() + ")");
                for (Product product: order.getProducts())
                    statement.executeUpdate("INSERT INTO ORDERSPRODUCTS (orderNum,art) VALUES (" + orderNum +
                            ", '" + product.getArt() + "')");
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void printTables(Iterable<String> tables){

        try(Connection connection = DriverManager.getConnection(dbAdress, "sa","")){

            for (String tableName: tables) {

                System.out.println(tableName);
                String sql = "select * from " + tableName;
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet rs = statement.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();

                while (rs.next()) {

                    for(int i = 1 ; i <= columnsNumber; i++){
                        System.out.print(rs.getString(i) + " "); 
                    }
                    System.out.println();
                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Map<Integer, Order> getOrders() {
        return new HashMap<>(orders);
    }

    public Map<String, Customer> getCustomers() {
        return new HashMap<>(customers);
    }

    public Map<String, Product> getProducts() {
        return new HashMap<>(products);
    }
}
