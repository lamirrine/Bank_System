package model.dao.impl;

import config.DatabaseConnection;
import model.dao.IAccountDAO;
import model.entities.Account;
import model.enums.AccountStatus;
import model.enums.AccountType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO implements IAccountDAO {

    @Override
    public String getPinHash(int accountId) throws SQLException {
        String sql = "SELECT transaction_pin_hash FROM account WHERE account_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("transaction_pin_hash");
                }
            }
        }
        return null;
    }

    @Override
    public Account findById(int accountId) {
        String sql = "SELECT * FROM account WHERE account_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAccount(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(Account account) throws SQLException {
        String sql = "INSERT INTO account (account_number, account_type, balance, open_date, close_date, status, owner_customer_id, agency_id, daily_withdraw_limit, daily_transfer_limit, transaction_pin_hash) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, account.getAccountNumber());
            stmt.setString(2, account.getAccountType().toString());
            stmt.setDouble(3, account.getBalance());
            stmt.setTimestamp(4, new java.sql.Timestamp(account.getOpenDate().getTime()));

            if (account.getCloseDate() != null) {
                stmt.setTimestamp(5, new java.sql.Timestamp(account.getCloseDate().getTime()));
            } else {
                stmt.setNull(5, java.sql.Types.TIMESTAMP);
            }

            stmt.setString(6, account.getStatus().toString());
            stmt.setInt(7, account.getOwnerCustomerId());
            stmt.setInt(8, account.getAgencyId());
            stmt.setDouble(9, account.getDailyWithdrawLimit());
            stmt.setDouble(10, account.getDailyTransferLimit());
            stmt.setString(11, account.getTransactionPinHash());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Falha ao criar conta, nenhuma linha afetada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    account.setAccountId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public boolean updatePin(int accountId, String newPinHash) throws SQLException {
        String sql = "UPDATE account SET transaction_pin_hash = ? WHERE account_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPinHash);
            stmt.setInt(2, accountId);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public void updateStatus(int accountId, AccountStatus status) throws SQLException {

    }

    @Override
    public List<Account> findByFilters(String accountType, String status) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM account WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (accountType != null && !accountType.equals("Todos")) {
            sql.append(" AND account_type = ?");
            params.add(accountType.toUpperCase());
        }

        if (status != null && !status.equals("Todos")) {
            sql.append(" AND status = ?");
            params.add(status.toUpperCase());
        }

        sql.append(" ORDER BY open_date DESC");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapResultSetToAccount(rs));
                }
            }
        }
        return accounts;
    }

    @Override
    public List<Account> findAll() throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM account ORDER BY open_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs));
            }
        }
        return accounts;
    }

    @Override
    public Account findByAccountNumber(String accountNumber) {
        String sql = "SELECT * FROM account WHERE account_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAccount(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateBalance(int accountId, double newBalance) {
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, newBalance);
            stmt.setInt(2, accountId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setAccountNumber(rs.getString("account_number"));
        account.setAccountType(AccountType.valueOf(rs.getString("account_type")));
        account.setBalance(rs.getDouble("balance"));
        account.setOpenDate(rs.getTimestamp("open_date"));
        account.setCloseDate(rs.getTimestamp("close_date"));
        account.setStatus(AccountStatus.valueOf(rs.getString("status")));
        account.setOwnerCustomerId(rs.getInt("owner_customer_id"));
        account.setAgencyId(rs.getInt("agency_id"));
        account.setDailyWithdrawLimit(rs.getDouble("daily_withdraw_limit"));
        account.setDailyTransferLimit(rs.getDouble("daily_transfer_limit"));
        account.setTransactionPinHash(rs.getString("transaction_pin_hash"));

        return account;
    }

    @Override
    public List<Account> findByCustomerId(int customerId) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM account WHERE owner_customer_id = ? AND status = 'ATIVA'";

        System.out.println("Executando query para customerId: " + customerId);
        System.out.println("SQL: " + sql);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            int count = 0;
            while (rs.next()) {
                count++;
                Account account = mapResultSetToAccount(rs);
                accounts.add(account);
                System.out.println("Conta " + count + ": " + account.getAccountNumber() + " - " + account.getBalance());
            }

            System.out.println("Total de contas encontradas: " + count);

        } catch (SQLException e) {
            System.err.println("ERRO no AccountDAO: " + e.getMessage());
            e.printStackTrace();
        }
        return accounts;
    }
}