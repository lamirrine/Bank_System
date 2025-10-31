package model.dao.impl;

import config.DatabaseConnection;
import model.dao.IUserDAO;
import model.entities.User;
import model.entities.Customer; // Necessário para instanciar, pois User é abstrato
import java.sql.*;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class MySQLUserDAO implements IUserDAO {

    private static final String FIND_BY_EMAIL = "SELECT user_id, first_name, last_name, email, pass_hash, registration_date FROM user WHERE email = ?";

    @Override
    public User findByEmail(String email) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_EMAIL)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // NOTA: Na prática, você precisaria de um método para determinar se é Customer ou Employee
                // Aqui, assumimos temporariamente que é um Customer para fins de retorno
                Customer user = new Customer();
                user.setUserId(rs.getInt("user_id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setPassHash(rs.getString("pass_hash"));
                user.setRegistrationDate(rs.getTimestamp("registration_date"));

                // O Service terá que fazer um downcast para Customer ou Employee,
                // ou buscar dados adicionais via CustomerDAO/EmployeeDAO.
                return user;
            }
            return null;

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por email: " + e.getMessage());
            throw e;
        }
    }


    @Override
    public void save(User user) throws SQLException {

        String sql = "INSERT INTO user (first_name, last_name, email, phone, address, pass_hash, registration_date, user_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getAddress()); // <-- CORRIGIDO: Campo adicionado
            stmt.setString(6, user.getPassHash());

            java.util.Date dateToSave = user.getRegistrationDate();
            if (dateToSave == null) {
                dateToSave = new java.util.Date();
            }
            stmt.setTimestamp(7, new java.sql.Timestamp(dateToSave.getTime()));

            String userType = user instanceof Customer ? "CUSTOMER" : "EMPLOYEE";
            stmt.setString(8, userType);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Falha ao criar usuário, nenhuma linha afetada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setUserId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Falha ao criar usuário, não foi obtido o ID gerado.");
                }
            }
        }
    }

    @Override
    public User findById(int userId) throws SQLException { return null; }
    @Override
    public void update(User user) throws SQLException { /* Lógica de UPDATE */ }
    @Override
    public String findUserType(int userId) throws SQLException { return null; }
}