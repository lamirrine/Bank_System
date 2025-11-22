package model.services;

import model.dao.IBankDAO;
import model.entities.Bank;
import java.sql.SQLException;

public class BankService {

    private IBankDAO bankDAO;
    private Bank currentBankInfo;
    private double transferFeeRate = 0.02;

    public BankService(){}

    public BankService(IBankDAO bankDAO) {
        this.bankDAO = bankDAO;
    }

    private void loadBankInfo() throws SQLException {
        if (currentBankInfo == null) {
            currentBankInfo = bankDAO.getBankInfo();

            // Se o DB estiver vazio, usa valores padrão/mock para evitar NullPointer
            if (currentBankInfo == null) {
                System.out.println("WARN: Configurações do banco não encontradas no DB. Usando valores padrão.");
                currentBankInfo = new Bank("MZBD", "Banco Digital", 0.005, "84 123 4567", "84 999 9999");
            }
        }
    }

    public double getTransferFeeRate() throws SQLException {
        loadBankInfo();
        return currentBankInfo.getTransferFeeRate();
    }

    public void setTransferFeeRate(double rate) {
        this.transferFeeRate = rate;
    }

    public double getMaxWithdrawLimit() {
        return 5000.00;
    }

    public double getMaxTransferLimit() {
        return 10000.00;
    }

    public String getSupportPhone() throws SQLException {
        loadBankInfo();
        return currentBankInfo.getSupportPhone();
    }

    public void updateBankConfiguration(Bank updatedBankInfo) throws Exception {
        try {
            bankDAO.updateInfo(updatedBankInfo);
            currentBankInfo = null;
        } catch (SQLException e) {
            throw new Exception("Falha ao atualizar configurações do banco.", e);
        }
    }
}