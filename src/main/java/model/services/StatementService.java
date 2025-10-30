package model.services;

import model.dao.IAccountDAO;
import model.dao.ITransactionDAO;
import model.entities.Transaction;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Serviço responsável por gerar extratos e histórico de transações.
 */
public class StatementService {

    // Dependências injetadas
    private ITransactionDAO transactionDAO;
    private IAccountDAO accountDAO; // Útil para verificar se a conta pertence ao usuário

    // Construtor para Injeção de Dependência
    public StatementService(ITransactionDAO transactionDAO, IAccountDAO accountDAO) {
        this.transactionDAO = transactionDAO;
        this.accountDAO = accountDAO;
    }

    /**
     * Busca o histórico completo de transações para uma conta, dentro de um período.
     * Este método é usado para gerar o Extrato completo (PDF, CSV).
     * @param accountId ID da conta.
     * @param startDate Início do período.
     * @param endDate Fim do período.
     * @return Lista de objetos Transaction.
     * @throws Exception Se a busca falhar.
     */
    public List<Transaction> getFullStatement(int accountId, Date startDate, Date endDate) throws Exception {
        try {
            // Regra de Negócio: Garantir que o período não seja demasiado longo (Ex: Max 1 ano)
            // (Lógica de validação de datas aqui)

            return transactionDAO.findByAccountId(accountId, startDate, endDate);

        } catch (SQLException e) {
            throw new Exception("Falha ao buscar extrato completo no banco de dados.", e);
        }
    }

    /**
     * Busca as últimas N transações para exibição rápida no Dashboard.
     * @param accountId ID da conta.
     * @param limit Número máximo de transações a retornar.
     * @return Lista das transações mais recentes.
     * @throws Exception Se a busca falhar.
     */
    public List<Transaction> getRecentTransactions(int accountId, int limit) throws Exception {
        try {
            return transactionDAO.findRecentByAccountId(accountId, limit);
        } catch (SQLException e) {
            throw new Exception("Falha ao buscar transações recentes.", e);
        }
    }

    // TODO: Adicionar um método que processa a lista de Transações e gera um objeto
    // StatementReport (que poderia conter o saldo inicial e final do período, e a lista).
}