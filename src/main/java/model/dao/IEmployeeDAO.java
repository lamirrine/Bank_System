package model.dao;

import model.entities.Employee;
import model.enums.AccessLevel;
import java.sql.SQLException;
import java.util.List;

public interface IEmployeeDAO {
    Employee findById(int employeeId) throws SQLException;
    void save(Employee employee) throws SQLException;
    void update(Employee employee) throws SQLException;

    // Métodos específicos para gerenciamento de funcionários
    List<Employee> findByAccessLevel(AccessLevel level) throws SQLException;
}