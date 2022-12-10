package com.revature.P1.DAO;

import com.revature.P1.Model.Reimbursement;
import com.revature.P1.Model.User;
import com.revature.P1.Util.ConnectionFactory;

import com.revature.P1.Util.Exceptions.InvalidUserInputException;
import com.revature.P1.Util.Interface.Crudable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;




import java.sql.*;
import java.util.ArrayList;

public class UserDAO implements Crudable<User> {


    @Override
    public User create(User newUser) {

        try (Connection connection = ConnectionFactory.getConnectionFactory().getConnection()) {



            String sql = "insert into user_table (user_name, password, position) values (?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, newUser.getUserName());
            preparedStatement.setString(2, newUser.getPassword());
            preparedStatement.setString(3, newUser.getPosition());

            int verifyInsert = preparedStatement.executeUpdate();

            if (verifyInsert == 0) {
                throw new RuntimeException("User was not added to the database");
            }
            return newUser;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public List<User> findAll() {
        try(Connection connection = ConnectionFactory.getConnectionFactory().getConnection()){
            List<User> users = new ArrayList<>();

            String sql = "select * from user_table";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                users.add(convertSqlInfoToUser(resultSet));
            }

            return users;

        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User findByUserName(String userName) {
        return null;
    }

    @Override
    public Reimbursement findById(int ticketId) {
        return null;
    }

    @Override
    public boolean update(User updatedUser) {
        return false;
    }

    @Override
    public boolean delete(String userName) {
        return false;
    }

    @Override
    public boolean delete(int ticketId) {
        return false;
    }

    public User loginCheck(String userName, String password){

        try(Connection connection = ConnectionFactory.getConnectionFactory().getConnection()){

            String sql = "select * from user_table where user_name = ? and password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()){
                throw new InvalidUserInputException("Entered information for " + userName + " was incorrect. Please try again.");
            }
            return convertSqlInfoToUser(resultSet);


        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }

    }

    private User convertSqlInfoToUser(ResultSet resultSet) throws SQLException {
        User user = new User();

        user.setUserName(resultSet.getString("user_name"));
        user.setPassword(resultSet.getString("password"));
        user.setPosition(resultSet.getString("position"));

        return user;
    }
}