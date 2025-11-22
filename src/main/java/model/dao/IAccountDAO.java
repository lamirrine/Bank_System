package model.dao;

import model.entities.Account;
import model.enums.AccountStatus;
import java.util.List;
import java.sql.SQLException;

public interface IAccountDAO {
    Account findById(int accountId) throws SQLException;

    boolean updateBalanceSafe(int accountId, double newBalance) throws SQLException;

    List<Account> findByFilters(String accountType, String status) throws SQLException;

    List<Account> findAll() throws SQLException;

    Account findByAccountNumber(String accountNumber) throws SQLException;

    List<Account> findByCustomerId(int customerId) throws SQLException;

    void save(Account account) throws SQLException;

    boolean updatePin(int accountId, String newPinHash) throws SQLException;

    boolean updateLimits(int accountId, double dailyWithdrawLimit, double dailyTransferLimit) throws SQLException;

    boolean updateStatus(int accountId, AccountStatus status) throws SQLException;

    boolean updateBalance(int accountId, double newBalance);

    // CRÍTICO: Busca o hash do PIN para validação de transação
    String getPinHash(int accountId) throws SQLException;
}