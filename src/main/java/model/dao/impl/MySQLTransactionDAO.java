package model.dao.impl;

import model.dao.ITransactionDAO;
import model.entities.Transaction;
import config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MySQLTransactionDAO implements ITransactionDAO {

    @Override
    public int save(Transaction tx) throws Exception {
        String sql = "INSERT INTO transactions(account_id,type,amount,description,balance_after) VALUES(?,?,?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, tx.getAccountId());
            ps.setString(2, tx.getType());
            ps.setDouble(3, tx.getAmount());
            ps.setString(4, tx.getDescription());
            ps.setDouble(5, tx.getBalanceAfter());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    @Override
    public List<Transaction> findByAccountId(int accountId) throws Exception {
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY created_at DESC";
        List<Transaction> list = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Transaction t = new Transaction();
                    t.setId(rs.getInt("id"));
                    t.setAccountId(rs.getInt("account_id"));
                    t.setType(rs.getString("type"));
                    t.setAmount(rs.getDouble("amount"));
                    t.setDescription(rs.getString("description"));
                    t.setCreatedAt(new Date(rs.getTimestamp("created_at").getTime()));
                    t.setBalanceAfter(rs.getDouble("balance_after"));
                    list.add(t);
                }
            }
        }
        return list;
    }
}
