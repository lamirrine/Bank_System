// EmployeeDAO.java
package model.dao.impl;

import config.DatabaseConnection;
import model.dao.IEmployeeDAO;
import model.entities.Employee;
import model.enums.AccessLevel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO implements IEmployeeDAO {

    @Override
    public void save(Employee employee) throws SQLException {
        if (employee.getUserId() == 0) {
            throw new SQLException("ID do usuário base não definido.");
        }

        String sql = "INSERT INTO employee (employee_id, access_level, is_supervisor) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employee.getUserId());
            stmt.setString(2, employee.getAccessLevel().toString());
            stmt.setBoolean(3, employee.isSupervisor());

            stmt.executeUpdate();
        }
    }

    @Override
    public Employee findById(int employeeId) {
        String sql = "SELECT u.*, e.access_level, e.is_supervisor " +
                "FROM user u JOIN employee e ON u.user_id = e.employee_id " +
                "WHERE u.user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToEmployee(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Employee findByEmail(String email) {
        String sql = "SELECT u.*, e.access_level, e.is_supervisor " +
                "FROM user u JOIN employee e ON u.user_id = e.employee_id " +
                "WHERE u.email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToEmployee(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT u.*, e.access_level, e.is_supervisor " +
                "FROM user u JOIN employee e ON u.user_id = e.employee_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    @Override
    public List<Employee> findByAccessLevel(AccessLevel accessLevel) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT u.*, e.access_level, e.is_supervisor " +
                "FROM user u JOIN employee e ON u.user_id = e.employee_id " +
                "WHERE e.access_level = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, accessLevel.toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    @Override
    public void updateAccessLevel(int employeeId, AccessLevel accessLevel) throws SQLException {
        String sql = "UPDATE employee SET access_level = ? WHERE employee_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, accessLevel.toString());
            stmt.setInt(2, employeeId);

            stmt.executeUpdate();
        }
    }

    @Override
    public Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setUserId(rs.getInt("user_id"));
        employee.setFirstName(rs.getString("first_name"));
        employee.setLastName(rs.getString("last_name"));
        employee.setEmail(rs.getString("email"));
        employee.setPhone(rs.getString("phone"));
        employee.setAddress(rs.getString("address"));
        employee.setPassHash(rs.getString("pass_hash"));
        employee.setRegistrationDate(rs.getTimestamp("registration_date"));

        // Campos específicos do Employee
        employee.setAccessLevel(AccessLevel.valueOf(rs.getString("access_level")));
        employee.setSupervisor(rs.getBoolean("is_supervisor"));

        return employee;
    }
}