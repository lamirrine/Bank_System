package model.dao.impl;

import config.DatabaseConnection;
import model.dao.IUserDAO;
import model.entities.Employee;
import model.entities.User;
import model.entities.Customer;
import model.enums.UserType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO implements IUserDAO {

    private static final String FIND_BY_EMAIL = "SELECT user_id, first_name, last_name, email, phone, address, pass_hash, user_type, registration_date FROM user WHERE email = ?";

    @Override
    public User findByEmail(String email) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_EMAIL)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String userTypeStr = rs.getString("user_type");
                UserType userType = UserType.valueOf(userTypeStr);

                User user;
                if (userType == UserType.CUSTOMER) {
                    user = new Customer();
                } else {
                    user = new Employee();
                }

                user.setUserId(rs.getInt("user_id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setPassHash(rs.getString("pass_hash"));
                user.setUserType(userType);
                user.setRegistrationDate(rs.getTimestamp("registration_date"));

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
            stmt.setString(5, user.getAddress());
            stmt.setString(6, user.getPassHash());
            stmt.setTimestamp(7, new java.sql.Timestamp(new java.util.Date().getTime()));
            stmt.setString(8, user instanceof Customer ? "CUSTOMER" : "EMPLOYEE");

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
    public void update(User user) throws SQLException {
        String sql = "UPDATE user SET first_name = ?, last_name = ?, email = ?, phone = ?, address = ?, pass_hash = ? WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getAddress());
            stmt.setString(6, user.getPassHash());
            stmt.setInt(7, user.getUserId());

            stmt.executeUpdate();
        }
    }

    @Override
    public boolean updatePassword(int userId, String newPasswordHash) throws SQLException {
        String sql = "UPDATE user SET pass_hash = ? WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPasswordHash);
            stmt.setInt(2, userId);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public User findById(int userId) throws SQLException {
        String sql = "SELECT user_id, first_name, last_name, email, phone, address, pass_hash, user_type, registration_date FROM user WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String userTypeStr = rs.getString("user_type");
                UserType userType = UserType.valueOf(userTypeStr);

                User user;
                if (userType == UserType.CUSTOMER) {
                    user = new Customer();
                } else {
                    user = new Employee();
                }

                user.setUserId(rs.getInt("user_id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setPassHash(rs.getString("pass_hash"));
                user.setUserType(userType);
                user.setRegistrationDate(rs.getTimestamp("registration_date"));

                return user;
            }
            return null;

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por ID: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public String findUserType(int userId) throws SQLException { return null; }
}