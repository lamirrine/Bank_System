package model.dao;

import model.entities.Account;
import model.enums.AccountStatus;
import java.util.List;
import java.sql.SQLException;

public interface IAccountDAO {
    Account findById(int accountId) throws SQLException;

    // Requisito de Transferência (busca por número de conta)
    Account findByAccountNumber(String accountNumber) throws SQLException;

    // Requisito "Minhas Contas" (busca todas as contas de um cliente)
    List<Account> findByCustomerId(int customerId) throws SQLException;

    // Persistência e Operações CRÍTICAS
    void save(Account account) throws SQLException;
    void updateStatus(int accountId, AccountStatus status) throws SQLException;

    // CRÍTICO: Método isolado para atualização de saldo (usado em transações atómicas)
    void updateBalance(int accountId, double newBalance) throws SQLException;

    // CRÍTICO: Busca o hash do PIN para validação de transação
    String getPinHash(int accountId) throws SQLException;
}