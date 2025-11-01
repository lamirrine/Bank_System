package model.dao.impl;

import config.DatabaseConnection;
import model.dao.IAccountDAO;
import model.entities.Account;
import model.enums.AccountStatus;
import java.sql.*;
import java.util.List;

public class AccountDAO implements IAccountDAO {

    private static final String UPDATE_BALANCE = "UPDATE account SET balance = ? WHERE account_id = ?";

    private static final String GET_PIN_HASH = "SELECT transaction_pin_hash FROM account WHERE account_id = ?";

    @Override
    public void updateBalance(int accountId, double newBalance) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_BALANCE)) {

            stmt.setDouble(1, newBalance);
            stmt.setInt(2, accountId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Nenhuma conta encontrada para o ID: " + accountId);
            }

        } catch (SQLException e) {
            System.err.println("Erro crítico ao atualizar saldo: " + e.getMessage());
            throw e; // Lançar a exceção para que o Service possa tratá-la (transação JDBC)
        }
    }

    @Override
    public String getPinHash(int accountId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_PIN_HASH)) {

            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("transaction_pin_hash");
            }
            return null;

        } catch (SQLException e) {
            System.err.println("Erro ao buscar PIN hash: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public Account findById(int accountId) throws SQLException { return null; }
    @Override
    public Account findByAccountNumber(String accountNumber) throws SQLException { return null; }
    @Override
    public List<Account> findByCustomerId(int customerId) throws SQLException { return null; }
    @Override
    public void save(Account account) throws SQLException { /* Lógica de SAVE */ }
    @Override
    public void updateStatus(int accountId, AccountStatus status) throws SQLException { /* Lógica de UPDATE STATUS */ }
}