package model.dao.impl;

import config.DatabaseConnection;
import model.dao.ICustomerDAO;
import model.entities.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO implements ICustomerDAO {


    @Override
    public void save(Customer customer) throws SQLException {
        if (customer.getUserId() == 0) {
            throw new SQLException("ID do usuário base não definido.");
        }

        String sql = "INSERT INTO customer (customer_id, bi_number, nuit, passport_number) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customer.getUserId());
            stmt.setString(2, customer.getBiNumber());
            stmt.setString(3, customer.getNuit());
            stmt.setString(4, customer.getPassportNumber()); // <-- CORRIGIDO

            stmt.executeUpdate();
        }
    }

    @Override
    public void update(Customer customer) throws SQLException {

    }

    @Override
    public Customer findById(int customerId) {
        String sql = "SELECT u.*, c.bi_number, c.nuit, c.passport_number " +
                "FROM user u JOIN customer c ON u.user_id = c.customer_id " +
                "WHERE u.user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCustomer(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Customer findByEmail(String email) {
        String sql = "SELECT u.*, c.bi_number, c.nuit, c.passport_number " +
                "FROM user u JOIN customer c ON u.user_id = c.customer_id " +
                "WHERE u.email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCustomer(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT u.*, c.bi_number, c.nuit, c.passport_number " +
                "FROM user u JOIN customer c ON u.user_id = c.customer_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        return new Customer(
                rs.getInt("user_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("pass_hash"),
                rs.getString("address"),
                rs.getString("bi_number"),
                rs.getString("nuit"),
                rs.getString("passport_number")
        );
    }
}
