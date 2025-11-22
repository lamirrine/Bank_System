package model.services;

import config.DatabaseConnection;
import model.dao.*;
import model.dao.impl.EmployeeDAO;
import model.dao.impl.UserDAO;
import model.enums.AccessLevel;
import model.dao.IEmployeeDAO;
import model.dao.IUserDAO;
import model.entities.Employee;
import model.utils.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class EmployeeService {

    // Dependências injetadas
    private IEmployeeDAO employeeDAO;
    private IUserDAO userDAO;
    private IAccountDAO accountDAO;
    private IAgencyDAO agencyDAO;
    private ITransactionDAO transactionDAO; // Para registrar o depósito inicial

    // Construtor para Injeção de Dependência
    public EmployeeService(IEmployeeDAO employeeDAO, IAccountDAO accountDAO, IAgencyDAO agencyDAO, ITransactionDAO transactionDAO) {
        this.employeeDAO = employeeDAO;
        this.accountDAO = accountDAO;
        this.agencyDAO = agencyDAO;
        this.transactionDAO = transactionDAO;
    }

    public EmployeeService() {
        this.userDAO = new UserDAO();
        this.employeeDAO = new EmployeeDAO();
    }

    public EmployeeService(IUserDAO userDAO, IEmployeeDAO employeeDAO) {
        this.userDAO = userDAO;
        this.employeeDAO = employeeDAO;
    }

    private boolean hasPermission(Employee employee, AccessLevel requiredLevel) {
        return employee.getAccessLevel().ordinal() >= requiredLevel.ordinal();
    }

    public boolean updateEmployee(Employee employee) throws SQLException {
        try {
            // Atualizar dados básicos na tabela user
            String userSql = "UPDATE user SET first_name = ?, last_name = ?, email = ?, phone = ?, address = ? WHERE user_id = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(userSql)) {

                stmt.setString(1, employee.getFirstName());
                stmt.setString(2, employee.getLastName());
                stmt.setString(3, employee.getEmail());
                stmt.setString(4, employee.getPhone());
                stmt.setString(5, employee.getAddress());
                stmt.setInt(6, employee.getUserId());

                stmt.executeUpdate();
            }

            // Atualizar dados específicos na tabela employee
            String employeeSql = "UPDATE employee SET access_level = ?, is_supervisor = ? WHERE employee_id = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(employeeSql)) {

                stmt.setString(1, employee.getAccessLevel().toString());
                stmt.setBoolean(2, employee.isSupervisor());
                stmt.setInt(3, employee.getUserId());

                stmt.executeUpdate();
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean deleteEmployee(int employeeId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Iniciar transação

            // 1. Primeiro eliminar da tabela employee
            String deleteEmployeeSQL = "DELETE FROM employee WHERE employee_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteEmployeeSQL)) {
                stmt.setInt(1, employeeId);
                int employeeRows = stmt.executeUpdate();

                if (employeeRows == 0) {
                    conn.rollback();
                    return false; // Funcionário não encontrado na tabela employee
                }
            }

            // 2. Depois eliminar da tabela user
            String deleteUserSQL = "DELETE FROM user WHERE user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteUserSQL)) {
                stmt.setInt(1, employeeId);
                int userRows = stmt.executeUpdate();

                if (userRows == 0) {
                    conn.rollback();
                    return false; // Usuário não encontrado na tabela user
                }
            }

            conn.commit(); // Confirmar transação
            System.out.println("Funcionário ID " + employeeId + " eliminado com sucesso");
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); // Reverter em caso de erro
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("Erro ao eliminar funcionário: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Employee registerEmployee(String firstName, String lastName, String email,
                                     String phone, String password, String address,
                                     AccessLevel accessLevel, boolean isSupervisor) throws Exception {
        // Validações
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("O email é obrigatório.");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("A senha é obrigatória.");
        }

        // Verificar se email já existe
        var existingUser = userDAO.findByEmail(email);
        if (existingUser != null) {
            throw new IllegalArgumentException("Este email já está registrado.");
        }

        try {
            // Hash da senha
            String hashedPassword = PasswordUtil.hashPassword(password);

            // Criar objeto Employee
            Employee employee = new Employee(firstName, lastName, email, phone,
                    hashedPassword, address, accessLevel, isSupervisor);

            // Salvar na tabela user (isso deve gerar o user_id)
            userDAO.save(employee);

            // Agora salvar na tabela employee
            employeeDAO.save(employee);

            return employee;

        } catch (SQLException e) {
            throw new Exception("Falha no registro do funcionário: " + e.getMessage(), e);
        }
    }



    public Employee login(String email, String password) throws Exception {
        try {
            Employee employee = employeeDAO.findByEmail(email);

            if (employee == null) {
                throw new Exception("Funcionário não encontrado.");
            }

            if (!PasswordUtil.checkPassword(password, employee.getPassHash())) {
                throw new Exception("Senha incorreta.");
            }

            return employee;

        } catch (SQLException e) {
            throw new Exception("Erro de banco de dados: " + e.getMessage(), e);
        }
    }

    public Employee registerEmployee(Employee employee) throws Exception {
        // Validações
        if (employee.getEmail() == null || employee.getEmail().isEmpty()) {
            throw new IllegalArgumentException("O email é obrigatório.");
        }

        // Verificar se email já existe
        var existingUser = userDAO.findByEmail(employee.getEmail());
        if (existingUser != null) {
            throw new IllegalArgumentException("Este email já está registrado.");
        }

        try {
            // Hash da senha
            String hashedPassword = PasswordUtil.hashPassword(employee.getPassHash());
            employee.setPassHash(hashedPassword);

            // Salvar na tabela user
            userDAO.save(employee);

            // Salvar na tabela employee
            employeeDAO.save(employee);

            return employee;

        } catch (SQLException e) {
            throw new Exception("Falha no registo do funcionário: " + e.getMessage(), e);
        }
    }

    public List<Employee> getAllEmployees() {
        return employeeDAO.findAll();
    }

    public List<Employee> getEmployeesByAccessLevel(AccessLevel accessLevel) {
        return employeeDAO.findByAccessLevel(accessLevel);
    }

    public Employee getEmployeeById(int employeeId) throws SQLException {
        return employeeDAO.findById(employeeId);
    }

    public boolean updateEmployeeAccessLevel(int employeeId, AccessLevel newAccessLevel) {
        try {
            employeeDAO.updateAccessLevel(employeeId, newAccessLevel);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}