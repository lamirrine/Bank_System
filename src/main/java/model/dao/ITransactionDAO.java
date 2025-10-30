package model.dao;

import model.entities.Transaction;
import java.util.List;

public interface ITransactionDAO {
    int save(Transaction tx) throws Exception;
    List<Transaction> findByAccountId(int accountId) throws Exception;
}
