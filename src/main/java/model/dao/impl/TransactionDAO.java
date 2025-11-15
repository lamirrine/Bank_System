package model.dao.impl;

import model.dao.ITransactionDAO;
import model.entities.Transaction;
import config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class TransactionDAO implements ITransactionDAO {

    @Override
    public boolean save(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transaction (type, amount, timestamp, status, description, " +
                "source_account_id, destination_account_id, resulting_balance, fee_amount) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, transaction.getType().toString());
            stmt.setDouble(2, transaction.getAmount());
            stmt.setTimestamp(3, new Timestamp(transaction.getTimestamp().getTime()));
            stmt.setString(4, transaction.getStatus().toString());
            stmt.setString(5, transaction.getDescription());
            stmt.setInt(6, transaction.getSourceAccountId());
            stmt.setInt(7, transaction.getDestinationAccountId());
            stmt.setDouble(8, transaction.getResultingBalance());
            stmt.setDouble(9, transaction.getFeeAmount());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        transaction.setTransactionId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar transação: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Transaction> findByAccountId(int accountId, int limit) throws SQLException {
        String sql = "SELECT * FROM transaction WHERE source_account_id = ? OR destination_account_id = ? " +
                "ORDER BY timestamp DESC LIMIT ?";

        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            stmt.setInt(2, accountId);
            stmt.setInt(3, limit);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = mapResultSetToTransaction(rs);
                    transactions.add(transaction);
                }
            }
        }

        return transactions;
    }

    @Override
    public List<Transaction> findByCustomerId(int customerId, int limit) throws SQLException {
        String sql = "SELECT t.* FROM transaction t " +
                "JOIN account a ON t.source_account_id = a.account_id " +
                "WHERE a.owner_customer_id = ? " +
                "ORDER BY t.timestamp DESC LIMIT ?";

        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            stmt.setInt(2, limit);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = mapResultSetToTransaction(rs);
                    transactions.add(transaction);
                }
            }
        }

        return transactions;
    }

    @Override
    public List<Transaction> findByAccountIdAndPeriod(int accountId, java.util.Date startDate, java.util.Date endDate) throws SQLException {
        String sql = "SELECT * FROM transaction WHERE (source_account_id = ? OR destination_account_id = ?) " +
                "AND timestamp BETWEEN ? AND ? " +
                "ORDER BY timestamp DESC";

        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            stmt.setInt(2, accountId);
            stmt.setTimestamp(3, new Timestamp(startDate.getTime()));
            stmt.setTimestamp(4, new Timestamp(endDate.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = mapResultSetToTransaction(rs);
                    transactions.add(transaction);
                }
            }
        }

        return transactions;
    }

    @Override
    public List<Transaction> findByCustomerIdAndPeriod(int customerId, Date startDate, Date endDate) throws SQLException {
        String sql = "SELECT t.* FROM transaction t " +
                "JOIN account a ON t.source_account_id = a.account_id " +
                "WHERE a.owner_customer_id = ? " +
                "AND t.timestamp BETWEEN ? AND ? " +
                "ORDER BY t.timestamp DESC";

        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            stmt.setTimestamp(2, new Timestamp(startDate.getTime()));
            stmt.setTimestamp(3, new Timestamp(endDate.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = mapResultSetToTransaction(rs);
                    transactions.add(transaction);
                }
            }
        }

        return transactions;
    }

    @Override
    public Transaction findById(int transactionId) throws SQLException {
        String sql = "SELECT * FROM transaction WHERE transaction_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, transactionId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTransaction(rs);
                }
            }
        }

        return null;
    }

    @Override
    public Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(rs.getInt("transaction_id"));
        transaction.setType(model.enums.TransactionType.valueOf(rs.getString("type")));
        transaction.setAmount(rs.getDouble("amount"));
        transaction.setTimestamp(rs.getTimestamp("timestamp"));
        transaction.setStatus(model.enums.TransactionStatus.valueOf(rs.getString("status")));
        transaction.setDescription(rs.getString("description"));
        transaction.setSourceAccountId(rs.getInt("source_account_id"));
        transaction.setDestinationAccountId(rs.getInt("destination_account_id"));
        transaction.setResultingBalance(rs.getDouble("resulting_balance"));
        transaction.setFeeAmount(rs.getDouble("fee_amount"));

        return transaction;
    }

    // No TransactionDAO.java, adicione:

    /**
     * Busca todas as transações
     */
    @Override
    public List<Transaction> findAll() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transaction ORDER BY timestamp DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        }
        return transactions;
    }

    /**
     * Busca transações com filtros
     */
    @Override
    public List<Transaction> findByFilters(String type, String status, Date startDate, Date endDate) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM transaction WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (type != null && !type.equals("Todos")) {
            sql.append(" AND type = ?");
            params.add(type.toUpperCase());
        }

        if (status != null && !status.equals("Todos")) {
            sql.append(" AND status = ?");
            params.add(status.toUpperCase());
        }

        if (startDate != null) {
            sql.append(" AND timestamp >= ?");
            params.add(new java.sql.Timestamp(startDate.getTime()));
        }

        if (endDate != null) {
            sql.append(" AND timestamp <= ?");
            params.add(new java.sql.Timestamp(endDate.getTime()));
        }

        sql.append(" ORDER BY timestamp DESC");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        }
        return transactions;
    }

    /**
     * Busca transações por intervalo de datas
     */
    @Override
    public List<Transaction> findByDateRange(Date startDate, Date endDate) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transaction WHERE timestamp BETWEEN ? AND ? ORDER BY timestamp DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, new java.sql.Timestamp(startDate.getTime()));
            stmt.setTimestamp(2, new java.sql.Timestamp(endDate.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        }
        return transactions;
    }
}