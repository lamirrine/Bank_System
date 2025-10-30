package model.dao.impl;

import config.DatabaseConnection;
import model.dao.IUserDAO;
import model.entities.User;
import model.entities.Customer; // Necessário para instanciar, pois User é abstrato
import java.sql.*;

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
        // Implementação complexa: Inserir e obter o ID gerado para ser usado em CustomerDAO/EmployeeDAO
    }

    @Override
    public User findById(int userId) throws SQLException { return null; }
    @Override
    public void update(User user) throws SQLException { /* Lógica de UPDATE */ }
    @Override
    public String findUserType(int userId) throws SQLException { return null; }
}