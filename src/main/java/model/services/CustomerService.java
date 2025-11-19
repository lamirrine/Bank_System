package model.services;

import config.DatabaseConnection;
import model.dao.IAccountDAO;
import model.dao.ICustomerDAO;
import model.dao.ITransactionDAO;
import model.dao.IUserDAO;
import model.dao.impl.CustomerDAO;
import model.dao.impl.UserDAO;
import model.entities.Account;
import model.entities.Customer;
import model.entities.Transaction;
import model.entities.User;
import model.utils.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public List<Customer> findAllCustomers() {
        return customerDAO.findAll();
    }

    public List<Account> getAllAccounts() {
        try {
            return accountDAO.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar contas: " + e.getMessage(), e);
        }
    }

    public boolean deactivateCustomer(int customerId) {
        try {
            // Buscar a conta do cliente para verificar saldo
            List<Account> accounts = accountDAO.findByCustomerId(customerId);

            // Verificar se há saldo nas contas
            for (Account account : accounts) {
                if (account.getBalance() > 0) {
                    throw new Exception("Cliente possui saldo em conta. Não pode ser desativado.");
                }
            }

            // Atualizar status na tabela user (soft delete)
            String sql = "UPDATE user SET user_type = 'INACTIVE' WHERE user_id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, customerId);
                return stmt.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao desativar cliente: " + e.getMessage());
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean updateCustomer(Customer customer) {
        try {
            String sql = "UPDATE user SET first_name = ?, last_name = ?, email = ?, phone = ?, address = ? WHERE user_id = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, customer.getFirstName());
                stmt.setString(2, customer.getLastName());
                stmt.setString(3, customer.getEmail());
                stmt.setString(4, customer.getPhone());
                stmt.setString(5, customer.getAddress());
                stmt.setInt(6, customer.getUserId());

                return stmt.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar cliente: " + e.getMessage());
            return false;
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