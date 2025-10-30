package model.services;

import model.dao.IAccountDAO;
import model.dao.ITransactionDAO;
import model.dao.impl.MySQLAccountDAO;
import model.dao.impl.MySQLTransactionDAO;
import model.entities.Account;
import model.entities.Transaction;

import java.util.Date;
import java.util.List;

public class AccountService {
    private IAccountDAO accountDAO = new MySQLAccountDAO();
    private ITransactionDAO txDAO = new MySQLTransactionDAO();

    public Account getByAccountNumber(String accNum) throws Exception {
        return accountDAO.findByAccountNumber(accNum);
    }

    public List<Account> getByOwnerId(int ownerId) throws Exception {
        return accountDAO.findByOwnerId(ownerId);
    }

    public List<Transaction> getTransactions(int accountId) throws Exception {
        return txDAO.findByAccountId(accountId);
    }

    public boolean deposit(int accountId, double amount, String description) throws Exception {
        Account a = accountDAO.findById(accountId);
        if (a == null) return false;
        double newBal = a.getBalance() + amount;
        accountDAO.updateBalance(accountId, newBal);
        Transaction tx = new Transaction();
        tx.setAccountId(accountId);
        tx.setType("DEPOSIT");
        tx.setAmount(amount);
        tx.setDescription(description);
        tx.setCreatedAt(new Date());
        tx.setBalanceAfter(newBal);
        txDAO.save(tx);
        return true;
    }

    public boolean withdraw(int accountId, double amount, String description) throws Exception {
        Account a = accountDAO.findById(accountId);
        if (a == null) return false;
        if (a.getBalance() < amount) return false;
        double newBal = a.getBalance() - amount;
        accountDAO.updateBalance(accountId, newBal);
        Transaction tx = new Transaction();
        tx.setAccountId(accountId);
        tx.setType("WITHDRAWAL");
        tx.setAmount(amount);
        tx.setDescription(description);
        tx.setCreatedAt(new Date());
        tx.setBalanceAfter(newBal);
        txDAO.save(tx);
        return true;
    }
}
