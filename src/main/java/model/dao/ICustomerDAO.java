package model.dao;

import model.entities.Customer;
import java.sql.SQLException;
import java.util.List;

public interface ICustomerDAO {
    Customer findById(int customerId) throws SQLException;

    // Persistência
    void save(Customer customer) throws SQLException;
    void update(Customer customer) throws SQLException;

    List<Customer> findBySearchTerm(String searchTerm) throws SQLException;

    Customer findByEmail(String email);

    List<Customer> findAll();
}