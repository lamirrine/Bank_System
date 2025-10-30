package model.dao.impl;

import config.DatabaseConnection;
import model.dao.IEmployeeDAO;
import model.entities.Employee;
import model.enums.AccessLevel;
import java.sql.*;
import java.util.List;

public class MySQLEmployeeDAO implements IEmployeeDAO {

    private static final String SAVE_EMPLOYEE =
            "INSERT INTO employee (employee_id, access_level, is_supervisor) VALUES (?, ?, ?)";

    @Override
    public void save(Employee employee) throws SQLException {
        // Assume que employee.getUserId() já foi preenchido pelo MySQLUserDAO.save()
        if (employee.getUserId() == 0) {
            throw new SQLException("ID do usuário base não definido. Não é possível salvar o Employee.");
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SAVE_EMPLOYEE)) {

            stmt.setInt(1, employee.getUserId());
            stmt.setString(2, employee.getAccessLevel().name());
            stmt.setBoolean(3, employee.isSupervisor());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao salvar dados de Employee: " + e.getMessage());
            throw e;
        }
    }

    // ... Implementação dos outros métodos
    @Override
    public Employee findById(int employeeId) throws SQLException {
        // Lógica JDBC: SELECT de 'user' JOIN 'employee'
        return null;
    }

    @Override
    public void update(Employee employee) throws SQLException { /* Lógica de UPDATE */ }

    @Override
    public List<Employee> findByAccessLevel(AccessLevel level) throws SQLException { return null; }
}