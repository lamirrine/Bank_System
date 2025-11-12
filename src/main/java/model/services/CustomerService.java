package model.services;

import model.dao.ICustomerDAO;
import model.dao.IUserDAO;
import model.dao.impl.CustomerDAO;
import model.dao.impl.UserDAO;
import model.entities.Customer;
import model.entities.User;
import utils.PasswordUtil;

import java.sql.SQLException;

public class CustomerService {

    private IUserDAO userDAO;
    private ICustomerDAO customerDAO;

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