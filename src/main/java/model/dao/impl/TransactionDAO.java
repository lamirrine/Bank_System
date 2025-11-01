package model.dao.impl;

import config.DatabaseConnection;
import model.dao.ITransactionDAO;
import model.entities.Transaction;
import java.sql.*;
import java.util.Date;
import java.util.List;

public class TransactionDAO implements ITransactionDAO {

    private static final String SAVE_TRANSACTION =
            "INSERT INTO transaction (type, amount, timestamp, status, description, source_account_id, destination_account_id, resulting_balance, fee_amount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    @Override
    public void save(Transaction transaction) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SAVE_TRANSACTION, Statement.RETURN_GENERATED_KEYS)) {

            // Mapeamento dos atributos do objeto Transaction para a Query SQL
            stmt.setString(1, transaction.getType().name()); // Usar o nome do Enum
            stmt.setDouble(2, transaction.getAmount());
            stmt.setTimestamp(3, new Timestamp(transaction.getTimestamp().getTime()));
            stmt.setString(4, transaction.getStatus().name());
            stmt.setString(5, transaction.getDescription());
            stmt.setInt(6, transaction.getSourceAccountId());

            // Trata o campo destinationAccountId que pode ser 0/null
            if (transaction.getDestinationAccountId() > 0) {
                stmt.setInt(7, transaction.getDestinationAccountId());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }

            stmt.setDouble(8, transaction.getResultingBalance());
            stmt.setDouble(9, transaction.getFeeAmount());

            stmt.executeUpdate();

            // Opcional: Recuperar o ID gerado para atualizar o objeto Transaction
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                transaction.setTransactionId(rs.getInt(1));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao salvar transação: " + e.getMessage());
            throw e;
        }
    }

    // ... Implementação dos outros métodos (findByAccountId, findRecentByAccountId)
    @Override
    public List<Transaction> findByAccountId(int accountId, Date start, Date end) throws SQLException { return null; }
    @Override
    public List<Transaction> findRecentByAccountId(int accountId, int limit) throws SQLException { return null; }
}