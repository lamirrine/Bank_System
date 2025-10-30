package model.services;

import model.dao.IAccountDAO;
import model.dao.IUserDAO;
import model.dao.impl.MySQLAccountDAO;
import model.dao.impl.MySQLUserDAO;
import model.entities.Account;
import model.entities.User;

import java.util.List;

public class CustomerService {
    private IUserDAO userDAO = new MySQLUserDAO();
    private IAccountDAO accountDAO = new MySQLAccountDAO();

    public User getUserByUsername(String username) throws Exception {
        return userDAO.findByUsername(username);
    }

    public List<Account> getAccountsForUser(int userId) throws Exception {
        return accountDAO.findByOwnerId(userId);
    }
}
