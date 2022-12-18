package ru.croc.project.database.dao;

import ru.croc.project.Achievement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class AchievementDao {

    private Connection baseConnection;

    public AchievementDao(Connection baseConnection){

        this.baseConnection = baseConnection;

    }

    public void addNewUserAchievements(Integer id) throws SQLException{

        try (PreparedStatement statement = baseConnection.prepareStatement(
                "INSERT INTO ACHIEVEMENT (id,newbie,wanderer,hardworking,truth_seeker,test_slayer," +
                        "bookworm,cinemaddict) VALUES (?,0,-2,-1,-1,-1,-1,-1) ")) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e){
            throw new SQLException();
        }

    }

    //по мапе из названий колонок и объектов класса достижений обновит записи для пользователя по id
    public void updateAllAchievements(Map<String, Achievement> achievementMap, Integer id) throws SQLException{

        try(PreparedStatement statement = baseConnection.prepareStatement(
                "UPDATE ACHIEVEMENT SET (newbie,wanderer,hardworking,truth_seeker,test_slayer," +
                        "bookworm,cinemaddict) = (?,?,?,?,?,?,?) WHERE id = ? ")) {

            String[] achievementColumns = {"newbie", "wanderer", "hardworking", "truth_seeker", "test_slayer",
                    "bookworm", "cinemaddict"};
            for (int i = 1; i <= achievementColumns.length; i++) {
                statement.setInt(i, achievementMap.get(achievementColumns[i - 1]).getLevel());
            }
            statement.setInt(achievementColumns.length + 1, id);

            statement.executeUpdate();
        }catch (SQLException e){
            throw new SQLException();
        }

    }

}
