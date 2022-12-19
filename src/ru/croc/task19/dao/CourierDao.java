package ru.croc.task19.dao;

import ru.croc.task19.pojo.Courier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CourierDao {

    private Connection connection;

    public CourierDao(Connection connection){

        this.connection = connection;

    }

    public Courier getCourierById(Integer id){

        Courier courier = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM COURIER " +
                "WHERE id = ?")) {

            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.isBeforeFirst()) {
                if (rs.next()) {
                    courier = new Courier(rs.getString("name"), rs.getString("surname"),
                            rs.getString("number"));
                    courier.setId(id);
                }
            } else
                throw new RuntimeException("В базе нет курьера с таким id. " +
                        "Обратитесь к табличке txtTables/CreatedTables.txt");

        } catch (RuntimeException re){
            throw re;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return courier;

    }

    //либо дает айди существующему, либо добавляет нового в базу и также дает ему айди
    public Courier setCourierInBase(Courier courier){

        Courier courierToReturn = getIdFromExistingInBaseCourier(courier);

        if (courierToReturn.getId() == null) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO COURIER " +
                    "(name,surname,number) VALUES (?,?,?)")) {

                statement.setString(1, courier.getName());
                statement.setString(2, courier.getSurname());
                statement.setString(3, courier.getNumber());
                statement.executeUpdate();

                courierToReturn = getIdFromExistingInBaseCourier(courierToReturn);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return courierToReturn;

    }

    public Courier getIdFromExistingInBaseCourier(Courier courier){

        try(PreparedStatement statement = connection.prepareStatement("SELECT id FROM COURIER WHERE " +
                "name = ? AND surname = ? AND number = ?")) {

            statement.setString(1,courier.getName());
            statement.setString(2,courier.getSurname());
            statement.setString(3,courier.getNumber());

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                courier.setId(rs.getInt(1));
            }

        } catch (Exception e){

            e.printStackTrace();

        }

        return courier;

    }

}
