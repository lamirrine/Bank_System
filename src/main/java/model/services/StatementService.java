package model.services;

import model.dao.ITransactionDAO;
import model.dao.impl.MySQLTransactionDAO;
import model.entities.Transaction;

import java.util.List;

public class StatementService {
    private ITransactionDAO txDAO = new MySQLTransactionDAO();

    public List<Transaction> getStatement(int accountId) throws Exception {
        return txDAO.findByAccountId(accountId);
    }
}
