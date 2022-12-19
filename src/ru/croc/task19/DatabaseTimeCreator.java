package ru.croc.task19;

import ru.croc.task17.DatabaseCreator;
import ru.croc.task19.dao.CourierDao;
import ru.croc.task19.pojo.Courier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatabaseTimeCreator extends DatabaseCreator {
    public DatabaseTimeCreator(){
        super();
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
             PreparedStatement statement = connection.prepareStatement("UPDATE " +
                     "CUSTOMER_ORDER SET (time,courier_id) = (?,?) WHERE order_num = ?")) {
            String line;
            CourierDao courierDao = new CourierDao(connection);

            while ((line = br.readLine()) != null) {

                String[] pl = line.split(","); //pl for parsedLine
                int orderNum = Integer.parseInt(pl[0]);
                String courierNumber = pl[6], courierSurame = pl[7], courierName = pl[8];
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime time = LocalDateTime.parse(pl[5], formatter);

                //вернет курьера с заполненным значением id
                Courier courier = courierDao.setCourierInBase(new Courier(courierName,courierSurame,courierNumber));

                statement.setObject(1,time);
                statement.setInt(2,courier.getId());
                statement.setInt(3,orderNum);
                statement.executeUpdate();

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

            statement.executeUpdate("ALTER TABLE CUSTOMER_ORDER " +
                    "ADD (time TIMESTAMP," +
                    "courier_id INTEGER," +
                    "FOREIGN KEY (courier_id) REFERENCES COURIER(id))");

        } catch (Exception e){
            e.printStackTrace();
            System.exit(0);
        }

    }

}
