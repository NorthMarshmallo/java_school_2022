package ru.croc.project.database.dao;

import java.sql.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserMetricDao {

    private Connection baseConnection;

    public UserMetricDao(Connection baseConnection){

        this.baseConnection = baseConnection;

    }

    //вернет true, если пользователь с таким ником уже в базе
    public boolean baseContainsUser(String username) throws SQLException{

        try(PreparedStatement statement = baseConnection.prepareStatement(
                "SELECT COUNT(*) AS record_count FROM USER_METRIC WHERE username = ?")) {
            statement.setString(1,username);
            ResultSet rs = statement.executeQuery();
            Boolean result = null;
            if (rs.next()) {
                result = (rs.getInt("record_count") == 1);
            }
            return result;

        } catch (SQLException e){
            e.printStackTrace();
            throw new SQLException();
        }
    }

    //вернет id нового пользователя, если его еще нет в базе, иначе null
    public Integer addNewUser(String username) throws SQLException{

        try(PreparedStatement statement = baseConnection.prepareStatement(
                "INSERT INTO USER_METRIC (username,days_active,tests_passed,words_learned,films_watched," +
                        "texts_read) VALUES (?,0,0,0,0,0)", Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, username);
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
            else
                return null;

        } catch (SQLException e){
            throw new SQLException();
        }
    }

    //по мапе из названий колонок и их обновленных значений обновит записи для пользователя по id
    public void updateAllMetrics(Map<String,Integer> userMetricsMap, Integer id) throws SQLException{
        try (PreparedStatement statement = baseConnection.prepareStatement(
                "UPDATE USER_METRIC SET (days_active,tests_passed,words_learned,films_watched," +
                        "texts_read) = (?,?,?,?,?) WHERE id = ? ")) {

            String[] metricColumns = {"days_active", "tests_passed", "words_learned", "films_watched",
                    "texts_read"};
            for (int i = 1; i <= metricColumns.length; i++) {
                statement.setInt(i, userMetricsMap.get(metricColumns[i - 1]));
            }

            statement.setInt(metricColumns.length + 1, id);
            statement.executeUpdate();
        } catch (SQLException e){
            throw new SQLException();
        }
    }

}
