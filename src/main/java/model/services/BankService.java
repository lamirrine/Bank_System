package model.services;

import model.dao.IBankDAO;
import model.entities.Bank;
import java.sql.SQLException;

/**
 * Serviço para regras gerais e configurações globais do banco (Taxas, Contatos).
 * Implementa cache simples para evitar múltiplas buscas de DB.
 */
public class BankService {

    // Dependência injetada
    private IBankDAO bankDAO;

    // Cache de informações para desempenho
    private Bank currentBankInfo;

    // Construtor para Injeção de Dependência
    public BankService(IBankDAO bankDAO) {
        this.bankDAO = bankDAO;
    }

    /**
     * Carrega as informações do banco do DB ou do cache.
     * @throws SQLException Se a leitura do DB falhar.
     */
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

    /**
     * Retorna a taxa de comissão percentual para transferências.
     * @return Taxa (Ex: 0.005 para 0.5%).
     * @throws SQLException
     */
    public double getTransferFeeRate() throws SQLException {
        loadBankInfo();
        return currentBankInfo.getTransferFeeRate();
    }

    /**
     * Retorna o telefone de suporte ao cliente.
     * @return Número de telefone.
     * @throws SQLException
     */
    public String getSupportPhone() throws SQLException {
        loadBankInfo();
        return currentBankInfo.getSupportPhone();
    }

    /**
     * Método para atualizar as configurações (uso exclusivo para Administradores/Empregados).
     * @param updatedBankInfo Novo objeto Bank com as configurações.
     * @throws Exception Se a atualização falhar.
     */
    public void updateBankConfiguration(Bank updatedBankInfo) throws Exception {
        try {
            bankDAO.updateInfo(updatedBankInfo);
            // Invalida o cache para recarregar as novas informações na próxima chamada
            currentBankInfo = null;
        } catch (SQLException e) {
            throw new Exception("Falha ao atualizar configurações do banco.", e);
        }
    }
}