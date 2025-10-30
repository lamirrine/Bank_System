package model.dao;

import model.entities.Customer;
import java.sql.SQLException;

public interface ICustomerDAO {
    Customer findById(int customerId) throws SQLException;

    // Persistência
    void save(Customer customer) throws SQLException;
    void update(Customer customer) throws SQLException;
}