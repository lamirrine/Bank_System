package model.dao;

import model.entities.Account;
import java.util.List;

public interface IAccountDAO {
    Account findByAccountNumber(String accountNumber) throws Exception;
    List<Account> findByOwnerId(int ownerId) throws Exception;
    Account findById(int id) throws Exception;
    int save(Account account) throws Exception;
    void updateBalance(int accountId, double newBalance) throws Exception;
}
