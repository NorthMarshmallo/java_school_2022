package ru.croc.project.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PointsDao {

    private Connection baseConnection;

    public PointsDao(Connection baseConnection){

        this.baseConnection = baseConnection;

    }

    public void addNewUser(Integer id) throws SQLException {

        try (PreparedStatement statement = baseConnection.prepareStatement(
                "INSERT INTO USER_POINTS (id,points) VALUES (?,0) ")) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e){
            throw new SQLException();
        }

    }

    public void updatePoints(Integer points, Integer id) throws SQLException{

        try (PreparedStatement statement = baseConnection.prepareStatement(
                "UPDATE USER_POINTS SET points = ? WHERE id = ?  ")) {

            statement.setInt(1, points);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e){
            throw new SQLException();
        }

    }

}
