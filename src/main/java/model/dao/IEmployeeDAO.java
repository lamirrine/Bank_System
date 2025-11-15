package model.dao;

import model.entities.Employee;
import model.enums.AccessLevel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface IEmployeeDAO {
    Employee findById(int employeeId) throws SQLException;
    void save(Employee employee) throws SQLException;

    Employee findByEmail(String email);

    List<Employee> findAll();


    List<Employee> findByAccessLevel(AccessLevel accessLevel);

    void updateAccessLevel(int employeeId, AccessLevel accessLevel) throws SQLException;

    Employee mapResultSetToEmployee(ResultSet rs) throws SQLException;
}