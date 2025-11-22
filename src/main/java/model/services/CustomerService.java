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

    public boolean deleteCustomer(int customerId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Iniciar transação

            // 1. Primeiro eliminar transações das contas do cliente
            String deleteTransactionsSQL = "DELETE t FROM transaction t " +
                    "JOIN account a ON t.source_account_id = a.account_id " +
                    "WHERE a.owner_customer_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteTransactionsSQL)) {
                stmt.setInt(1, customerId);
                stmt.executeUpdate();
                System.out.println("Transações do cliente ID " + customerId + " eliminadas");
            }

            // 2. Eliminar contas do cliente
            String deleteAccountsSQL = "DELETE FROM account WHERE owner_customer_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteAccountsSQL)) {
                stmt.setInt(1, customerId);
                int accountsDeleted = stmt.executeUpdate();
                System.out.println(accountsDeleted + " contas do cliente ID " + customerId + " eliminadas");
            }

            // 3. Eliminar da tabela customer
            String deleteCustomerSQL = "DELETE FROM customer WHERE customer_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteCustomerSQL)) {
                stmt.setInt(1, customerId);
                int customerRows = stmt.executeUpdate();

                if (customerRows == 0) {
                    conn.rollback();
                    System.out.println("Cliente não encontrado na tabela customer");
                    return false;
                }
            }

            // 4. Eliminar da tabela user
            String deleteUserSQL = "DELETE FROM user WHERE user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteUserSQL)) {
                stmt.setInt(1, customerId);
                int userRows = stmt.executeUpdate();

                if (userRows == 0) {
                    conn.rollback();
                    System.out.println("Usuário não encontrado na tabela user");
                    return false;
                }
            }

            conn.commit(); // Confirmar transação
            System.out.println("Cliente ID " + customerId + " e todos os dados associados eliminados com sucesso");
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback(); // Reverter em caso de erro
                    System.out.println("Transação revertida devido a erro");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("Erro ao eliminar cliente: " + e.getMessage());
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

    public Map<String, Object> getCustomerAccountsInfo(int customerId) {
        Map<String, Object> info = new HashMap<>();

        String sql = "SELECT COUNT(*) as total_accounts, SUM(balance) as total_balance " +
                "FROM account WHERE owner_customer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                info.put("totalAccounts", rs.getInt("total_accounts"));
                info.put("totalBalance", rs.getDouble("total_balance"));
                info.put("hasAccounts", rs.getInt("total_accounts") > 0);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return info;
    }

    public boolean hasAccounts(int customerId) throws SQLException {
        // Você precisará injetar o accountDAO e implementar esta verificação
        // Por enquanto, retornamos false para teste
        return false;
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