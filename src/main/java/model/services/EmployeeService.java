package model.services;

import model.dao.*;
import model.dao.impl.EmployeeDAO;
import model.dao.impl.UserDAO;
import model.entities.*;
import model.enums.AccessLevel;
import model.dao.IEmployeeDAO;
import model.dao.IUserDAO;
import model.entities.Employee;
import utils.PasswordUtil;
import model.enums.AccountStatus;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

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

    public boolean deleteEmployee(int employeeId) {
        // Implementar lógica de exclusão (se necessário)
        // Nota: Em sistemas reais, geralmente fazemos soft delete
        return false;
    }
}