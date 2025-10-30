package model.dao.impl;

import config.DatabaseConnection;
import model.dao.ICustomerDAO;
import model.entities.Customer;
import java.sql.*;

public class MySQLCustomerDAO implements ICustomerDAO {

    // Query para INSERT, pressupondo que user_id já foi gerado por MySQLUserDAO
    private static final String SAVE_CUSTOMER =
            "INSERT INTO customer (customer_id, bi_number, nuit, passport_number) VALUES (?, ?, ?, ?)";

    @Override
    public void save(Customer customer) throws SQLException {
        // Assume que customer.getUserId() já foi preenchido pelo MySQLUserDAO.save()
        if (customer.getUserId() == 0) {
            throw new SQLException("ID do usuário base não definido. Não é possível salvar o Customer.");
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SAVE_CUSTOMER)) {

            stmt.setInt(1, customer.getUserId());
            stmt.setString(2, customer.getBiNumber());
            stmt.setString(3, customer.getNuit());
            stmt.setString(4, customer.getPassportNumber());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao salvar dados de Customer (KYC): " + e.getMessage());
            throw e;
        }
    }

    // ... Implementação dos outros métodos
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