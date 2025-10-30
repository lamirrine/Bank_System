package model.dao.impl;

import model.dao.IAccountDAO;
import model.entities.Account;
import config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MySQLAccountDAO implements IAccountDAO {

    @Override
    public Account findByAccountNumber(String accountNumber) throws Exception {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Account a = map(rs);
                    return a;
                }
            }
        }
        return null;
    }

    @Override
    public List<Account> findByOwnerId(int ownerId) throws Exception {
        String sql = "SELECT * FROM accounts WHERE owner_id = ?";
        List<Account> list = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }
        return list;
    }

    @Override
    public Account findById(int id) throws Exception {
        String sql = "SELECT * FROM accounts WHERE id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    @Override
    public int save(Account account) throws Exception {
        String sql = "INSERT INTO accounts(account_number,type,balance,owner_id) VALUES(?,?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, account.getAccountNumber());
            ps.setString(2, account.getType());
            ps.setDouble(3, account.getBalance());
            if (account.getOwnerId() > 0) ps.setInt(4, account.getOwnerId()); else ps.setNull(4, java.sql.Types.INTEGER);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    @Override
    public void updateBalance(int accountId, double newBalance) throws Exception {
        String sql = "UPDATE accounts SET balance = ? WHERE id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, newBalance);
            ps.setInt(2, accountId);
            ps.executeUpdate();
        }
    }

    private Account map(ResultSet rs) throws Exception {
        Account a = new Account();
        a.setId(rs.getInt("id"));
        a.setAccountNumber(rs.getString("account_number"));
        a.setType(rs.getString("type"));
        a.setBalance(rs.getDouble("balance"));
        a.setOwnerId(rs.getInt("owner_id"));
        return a;
    }
}
