package ru.croc.task19.dao;

import ru.croc.task17.pojo.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerDao {

    private Connection connection;

    public CustomerDao(Connection connection){

        this.connection = connection;

    }

    public Customer getCustomerByName(String customerName){

        Customer customer = null;
        try (PreparedStatement customerStatement = connection.prepareStatement(
                "SELECT * FROM CUSTOMER WHERE name = ?" )) {

            customerStatement.setString(1,customerName);
            ResultSet rs = customerStatement.executeQuery();
            if (rs.next()){
                customer = new Customer(rs.getInt("id"),customerName);
                customer.setSpent(rs.getInt("spent"));
            }


        } catch (NullPointerException ne){
            throw ne;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return customer;
    }
    public String getCustomerNameById(Integer customerId){

        String customerName = null;

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM CUSTOMER " +
                "WHERE id = ?")){

            statement.setInt(1,customerId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()){

                customerName = rs.getString("name");

            } else throw new RuntimeException("В базе нет пользователя с таким id. " +
                    "Обратитесь к табличке txtTables/CreatedTables.txt");

        } catch (RuntimeException re){
            throw re;
        } catch (Exception e){
            e.printStackTrace();
        }

        return customerName;

    }

}
