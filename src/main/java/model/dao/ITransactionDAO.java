package model.dao;

import model.entities.Transaction;

import java.util.Date;
import java.sql.ResultSet;
import java.util.List;
import java.sql.SQLException;

public interface ITransactionDAO {
    List<Transaction> findByAccountId(int accountId, int limit) throws SQLException;

    List<Transaction> findByAccountIdAndPeriod(int accountId, Date startDate, Date endDate) throws SQLException;

    List<Transaction> findByCustomerIdAndPeriod(int customerId, Date startDate, Date endDate) throws SQLException;

    Transaction findById(int transactionId) throws SQLException;

    List<Transaction> findByCustomerId(int customerId, int limit) throws SQLException;

    boolean save(Transaction transaction) throws SQLException;

    Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException;

    List<Transaction> findAll() throws SQLException;

    List<Transaction> findByFilters(String type, String status, Date startDate, Date endDate) throws SQLException;

    List<Transaction> findByDateRange(Date startDate, Date endDate) throws SQLException;
}