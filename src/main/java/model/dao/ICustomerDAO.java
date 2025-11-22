package model.dao;

import model.entities.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ICustomerDAO {
    Customer findById(int customerId) throws SQLException;

    void save(Customer customer) throws SQLException;
    void update(Customer customer) throws SQLException;

    List<Customer> findBySearchTerm(String searchTerm) throws SQLException;

    Customer findByEmail(String email);

    List<Customer> findAll();

    Customer mapResultSetToCustomer(ResultSet rs) throws SQLException;
}