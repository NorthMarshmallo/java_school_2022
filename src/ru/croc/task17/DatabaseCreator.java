package ru.croc.task17;

import ru.croc.task17.pojo.Customer;
import ru.croc.task17.pojo.Order;
import ru.croc.task17.pojo.Product;

import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseCreator {

    //продукты по артикулам
    protected Map<String, Product> products;

    //в поданном csv у пользователей ид нет, здесь для удобства создания базы ключи - имена, в дальнейших задачах
    //будет мапа с ключами - ид
    protected Map<String,Customer> customers;
    //заказы по номеру
    protected Map<Integer,Order> orders;

    protected String fileName, dbAdress;

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
        setAllMoney();

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
            int id = 1;
            while ((line = br.readLine()) != null) {

                String[] pl = line.split(","); //pl for parsedLine
                String name = pl[1], art = pl[2], title = pl[3];
                int orderNum = Integer.parseInt(pl[0]), price = Integer.parseInt(pl[4]);

                Product product = new Product(art,title,price);
                products.putIfAbsent(art, product);


                Customer customer = new Customer(id,name);
                if (customers.get(name) == null) {
                    customers.put(name, customer);
                    id++;
                }

                if (orders.get(orderNum) == null) {
                    Order order = new Order(orderNum, customers.get(name), product);
                    orders.put(orderNum, order);
                    customers.get(name).updateOrders(order);
                }
                else
                    orders.get(orderNum).updateProducts(product);

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void setAllMoney(){
        for (Order order: orders.values()){
            order.updateCost();
        }
        for (Customer customer: customers.values()){
            customer.updateSpent();
        }
    }

    private void setProductTable(Connection connection){

        try (Statement statement = connection.createStatement()) {

            //в целом название показывается только в данной таблице и ни с чем не связано, поэтому потенциально
            //может быть null

            statement.execute("CREATE TABLE PRODUCT " +
                    "(id INTEGER NOT NULL AUTO_INCREMENT, " +
                    "art VARCHAR(255) PRIMARY KEY NOT NULL, " +
                    "title VARCHAR(255), " +
                    "price INTEGER NOT NULL)");
            for (String art : products.keySet()) {
                statement.executeUpdate("INSERT INTO PRODUCT (art,title,price) VALUES (" + products.get(art) + ")");
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setCustomerTable(Connection connection){

        try (Statement statement = connection.createStatement()) {

            statement.execute("CREATE TABLE CUSTOMER " +
                    "(id INTEGER PRIMARY KEY NOT NULL," +
                    "name VARCHAR(255) UNIQUE NOT NULL , " +
                    "spent INTEGER NOT NULL)");

            for (String name : customers.keySet()) {
                statement.executeUpdate("INSERT INTO CUSTOMER VALUES (" + customers.get(name) + ")");
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setOrdersTables(Connection connection){

        try (Statement statement = connection.createStatement()) {

            statement.execute("CREATE TABLE ORDERS " +
                    "(strInOrders INT NOT NULL AUTO_INCREMENT, " +
                    "orderNum INT PRIMARY KEY NOT NULL, " +
                    "customerId INTEGER NOT NULL," +
                    "customerName VARCHAR(255) NOT NULL," +
                    "orderCost INT NOT NULL, "+
                    "FOREIGN KEY (customerId) REFERENCES CUSTOMER(id)," +
                    "FOREIGN KEY (customerName) REFERENCES CUSTOMER(name))");

            statement.execute("CREATE TABLE ORDERSPRODUCTS " +
                    "(strInOP INT PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
                    "orderNum INT NOT NULL, " +
                    "art VARCHAR(255) NOT NULL, " +
                    "FOREIGN KEY (orderNum) REFERENCES ORDERS(orderNum), " +
                    "FOREIGN KEY (art) REFERENCES PRODUCT(art) " +
                    "ON DELETE CASCADE)");

            for (Integer orderNum : orders.keySet()) {
                Order order = orders.get(orderNum);
                statement.executeUpdate("INSERT INTO ORDERS (orderNum,customerName,customerId,orderCost) " +
                        "VALUES (" + orderNum + ", '" + order.getCustomerName() +
                        "', " + order.getCustomerId() + ", " + order.getCost() + ")");
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
                PreparedStatement statement = connection.prepareStatement("select * from " + tableName);
                ResultSet rs = statement.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();

                for(int i = 1 ; i <= columnsNumber; i++){
                    System.out.print(rsmd.getColumnName(i) + " ");
                }
                System.out.println();

                while (rs.next()) {

                    for(int i = 1 ; i <= columnsNumber; i++){
                        System.out.print(rs.getString(i) + " ");
                    }
                    System.out.println();

                }
                statement.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void printTables(Iterable<String> tables, String file) {

        try {
            File f = new File(file);
            f.getParentFile().mkdirs();
            f.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file));
             Connection connection = DriverManager.getConnection(dbAdress, "sa", "")) {

            for (String tableName : tables) {

                bw.write(tableName);
                bw.newLine();
                PreparedStatement statement = connection.prepareStatement("select * from " + tableName);
                ResultSet rs = statement.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();

                for (int i = 1; i <= columnsNumber; i++) {
                    bw.write(rsmd.getColumnName(i) + " ");
                }
                bw.newLine();

                while (rs.next()) {

                    for (int i = 1; i <= columnsNumber; i++) {
                        bw.write(rs.getString(i) + " ");
                    }
                    bw.newLine();

                }
                statement.close();
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
