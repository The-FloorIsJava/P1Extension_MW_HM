package com.revature.P1.DAO;

import com.revature.P1.Model.Reimbursement;
import com.revature.P1.Util.ConnectionFactory;
import com.revature.P1.Util.Interface.Crudable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReimbursementDAO implements Crudable<Reimbursement> {
//

    @Override
    public Reimbursement create(Reimbursement newReimbursement) {

        try(Connection connection = ConnectionFactory.getConnectionFactory().getConnection()){

            String sql = "insert into reimbursement_ticket(user_name, ticket_id, status, description, amount) values (?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, newReimbursement.getUserName());
            preparedStatement.setDouble(2, newReimbursement.getTicketId());
            preparedStatement.setString(3, newReimbursement.getStatus());
            preparedStatement.setString(4, newReimbursement.getDescription());
            preparedStatement.setDouble(5, newReimbursement.getAmount());

            int verifyInsert = preparedStatement.executeUpdate();
            if (verifyInsert == 0){
                throw  new RuntimeException("Reimbursement was not added");
            }
            return newReimbursement;

        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Reimbursement> findAll() {

        try(Connection connection = ConnectionFactory.getConnectionFactory().getConnection()){

            List<Reimbursement> reimbursements = new ArrayList<>();

            String sql = "select * from reimbursement_ticket";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){

                reimbursements.add(convertSqlInfoToReimbursement(resultSet));

            }
            return reimbursements;

        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Reimbursement findByUserName(String userName) {
        return null;
    }

    @Override
    public Reimbursement findById(int ticketId) {
        try(Connection connection = ConnectionFactory.getConnectionFactory().getConnection()){

            Reimbursement reimbursement = new Reimbursement();

            String sql = "select * from reimbursement_ticket where ticket_id  = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1,ticketId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()){
                throw new RuntimeException("Reimbursement "+ ticketId + " not found");
            }

            return convertSqlInfoToReimbursement(resultSet);

        }catch (SQLException e){
            e.printStackTrace();
            return null;

        }
    }
    public Reimbursement approve(int TicketId){



        try (Connection connection = ConnectionFactory.getConnectionFactory().getConnection()) {

            Reimbursement reimbursement = new Reimbursement();

            String sql = "Update reimbursement_ticket set status = 'approved' where ticket_id  = ?;" +
                    "select * from reimbursement_ticket where ticket_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, TicketId);
            preparedStatement.setInt(2, TicketId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new RuntimeException("Reimbursement " + TicketId + " not found");
            }


            return convertSqlInfoToReimbursement(resultSet);


        } catch (SQLException e) {
            e.printStackTrace();
            return null;

        }
    }

    public Reimbursement deny(int ticketId) {

        try(Connection connection = ConnectionFactory.getConnectionFactory().getConnection()){

            Reimbursement reimbursement = new Reimbursement();

            String sql = "Update reimbursement_ticket set status = 'denied' where ticket_id  = ?;" +
                    "select * from reimbursement_ticket where ticket_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1,ticketId);
            preparedStatement.setInt(2,ticketId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()){
                throw new RuntimeException("Reimbursement "+ ticketId + " not found");
            }


            return convertSqlInfoToReimbursement(resultSet);


        }catch (SQLException e){
            e.printStackTrace();
            return null;

        }
    }


    @Override
    public boolean update(Reimbursement updatedObject) {
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


    public List<Reimbursement> findAllPending() {

        try(Connection connection = ConnectionFactory.getConnectionFactory().getConnection()){

            List<Reimbursement> reimbursements = new ArrayList<>();

            String sql = "select * from reimbursement_ticket where status = 'pending'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){


                reimbursements.add(convertSqlInfoToReimbursement(resultSet));

            }
            return reimbursements;

        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public Reimbursement findPersonalRequests(String userName) {
        try(Connection connection = ConnectionFactory.getConnectionFactory().getConnection()){

            List<Reimbursement> reimbursements = new ArrayList<>();


            String sql = "select * from reimbursement_ticket" +
                    " inner join user_table on reimbursement_ticket.user_name = user_table.user_name" +
                    " where user_name  = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1,userName);
            ResultSet resultSet = preparedStatement.executeQuery();


            while(resultSet.next()){
                reimbursements.add(convertSqlInfoToReimbursement(resultSet));

            }

            return convertSqlInfoToReimbursement(resultSet);


        }catch (SQLException e){
            e.printStackTrace();
            return null;

        }
    }

    private Reimbursement convertSqlInfoToReimbursement(ResultSet resultSet) throws SQLException {
        Reimbursement reimbursement = new Reimbursement();


        reimbursement.setTicketId(resultSet.getInt("ticket_id"));
        reimbursement.setAmount(resultSet.getDouble("amount"));
        reimbursement.setDescription(resultSet.getString("description"));
        reimbursement.setStatus(resultSet.getString("status"));
        reimbursement.setUserName(resultSet.getString("user_name"));
        reimbursement.setTicketNumbers(resultSet.getInt("ticket_numbers"));

        return reimbursement;
    }


}