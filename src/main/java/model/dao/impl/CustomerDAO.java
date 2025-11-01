package model.dao.impl;

import config.DatabaseConnection;
import model.dao.ICustomerDAO;
import model.entities.Customer;
import java.sql.*;

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
    public Customer findById(int customerId) throws SQLException {
        // Lógica JDBC: SELECT de 'user' JOIN 'customer'
        return null;
    }

    @Override
    public void update(Customer customer) throws SQLException {
        // Lógica JDBC: UPDATE nas tabelas 'user' e 'customer' (requer transação)
    }
}