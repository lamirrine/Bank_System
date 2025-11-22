package model.dao.impl;

import config.DatabaseConnection;
import model.dao.IBankDAO;
import model.entities.Bank;
import java.sql.*;

public class BankDAO implements IBankDAO {

    private static final String GET_BANK_INFO = "SELECT bank_code, bank_name, transfer_fee_rate, support_phone, emergency_phone FROM bank LIMIT 1";

    @Override
    public Bank getBankInfo() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_BANK_INFO);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                Bank bank = new Bank();
                bank.setBankCode(rs.getString("bank_code"));
                bank.setBankName(rs.getString("bank_name"));
                bank.setTransferFeeRate(rs.getDouble("transfer_fee_rate"));
                bank.setSupportPhone(rs.getString("support_phone"));
                bank.setEmergencyPhone(rs.getString("emergency_phone"));
                return bank;
            }
            return null; // Retorna nulo se a tabela de configuração estiver vazia

        } catch (SQLException e) {
            System.err.println("Erro ao carregar configurações do banco: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateInfo(Bank bank) throws SQLException {
        //UPDATE
    }
}