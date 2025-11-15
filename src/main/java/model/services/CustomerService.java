package model.services;

import model.dao.IAccountDAO;
import model.dao.ICustomerDAO;
import model.dao.ITransactionDAO;
import model.dao.IUserDAO;
import model.dao.impl.CustomerDAO;
import model.dao.impl.EmployeeDAO;
import model.dao.impl.UserDAO;
import model.entities.Account;
import model.entities.Customer;
import model.entities.Transaction;
import model.entities.User;
import utils.PasswordUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerService {

    private IUserDAO userDAO;
    private ICustomerDAO customerDAO;
    private IAccountDAO accountDAO;
    private ITransactionDAO transactionDAO;

    public CustomerService(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }
    public CustomerService(IUserDAO userDAO, ICustomerDAO customerDAO) {
        this.userDAO = userDAO;
        this.customerDAO = customerDAO;
    }

    public CustomerService() {
        this.userDAO = new UserDAO();
        this.customerDAO = new CustomerDAO();
    }

    public CustomerService(ICustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public Customer findCustomerById(int customerId) {
        try {
            return customerDAO.findById(customerId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // No CustomerService.java, adicione:
    public List<Customer> findAllCustomers() {
        return customerDAO.findAll();
    }

    // No AccountService.java, adcione:
    public List<Account> getAllAccounts() {
        try {
            return accountDAO.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar contas: " + e.getMessage(), e);
        }
    }

    public List<Customer> searchCustomers(String searchTerm) {
        try {
            return customerDAO.findBySearchTerm(searchTerm);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar clientes: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Obtém estatísticas dos clientes
     */
    public Map<String, Object> getCustomerStats() {
        Map<String, Object> stats = new HashMap<>();
        try {
            List<Customer> customers = customerDAO.findAll();

            long totalCustomers = customers.size();
            // Aqui você pode adicionar mais estatísticas conforme necessário
            // Por exemplo: clientes ativos, novos clientes este mês, etc.

            stats.put("totalCustomers", totalCustomers);
            stats.put("activeCustomers", totalCustomers); // Por enquanto, assumimos que todos estão ativos

        } catch (Exception e) {
            System.err.println("Erro ao calcular estatísticas de clientes: " + e.getMessage());
            e.printStackTrace();
        }
        return stats;
    }

    // No StatementService.java, adicione:
    public List<Transaction> getAllTransactions() {
        try {
            return transactionDAO.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar transações: " + e.getMessage(), e);
        }
    }

    public Customer findCustomerByEmail(String email) {
        return customerDAO.findByEmail(email);
    }

    public Customer registerCustomer(Customer customer) throws Exception {
        // Validações
        if (customer.getEmail() == null || customer.getEmail().isEmpty()) {
            throw new IllegalArgumentException("O email é obrigatório.");
        }

        // Verificar se email já existe
        User existingUser = userDAO.findByEmail(customer.getEmail());
        if (existingUser != null) {
            throw new IllegalArgumentException("Este email já está registrado.");
        }

        // Verificar documentos
        if ((customer.getBiNumber() == null || customer.getBiNumber().isEmpty()) &&
                (customer.getPassportNumber() == null || customer.getPassportNumber().isEmpty())) {
            throw new IllegalArgumentException("É obrigatório fornecer BI ou Passaporte para registo.");
        }

        try {
            // Hash da senha usando PasswordUtil
            String hashedPassword = PasswordUtil.hashPassword(customer.getPassHash());
            customer.setPassHash(hashedPassword);

            // Salvar na tabela user
            userDAO.save(customer);

            // Salvar na tabela customer
            customerDAO.save(customer);

            return customer;

        } catch (SQLException e) {
            throw new Exception("Falha no registo do cliente: " + e.getMessage(), e);
        }
    }


}