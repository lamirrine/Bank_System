package model.services;

import model.dao.IAccountDAO;
import model.dao.IUserDAO;
import model.entities.User;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;
import model.exceptions.DomainException;
import model.exceptions.InvalidCredentialsException;
import model.utils.PasswordUtil;
import model.utils.PinUtil;

public class AuthenticationService {

    private IUserDAO userDAO;
    private IAccountDAO accountDAO;
    private Map<String, User> activeSessions;

    public AuthenticationService(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public AuthenticationService(IUserDAO userDAO, IAccountDAO accountDAO) {
        this.userDAO = userDAO;
        this.accountDAO = accountDAO;
        this.activeSessions = new HashMap<>();
    }

    public AuthenticationService(IAccountDAO accountDAO) {
        this.accountDAO = accountDAO;
        this.activeSessions = new HashMap<>();
    }

    public User login(String email, String password) throws InvalidCredentialsException, SQLException {
        // 1. Buscar o usuário
        User user = userDAO.findByEmail(email);

        if (user == null) {
            throw new InvalidCredentialsException("Credenciais inválidas.");
        }

        boolean isPasswordValid = PasswordUtil.checkPassword(password, user.getPassHash());

        if (!isPasswordValid) {
            throw new InvalidCredentialsException("Senha inválida.");
        }
        return user;
    }

    public boolean validatePin(int accountId, String pin) throws Exception {
        String pinHash = accountDAO.getPinHash(accountId);

        if (pinHash == null) {
            throw new DomainException("PIN de transação não configurado para esta conta.");
        }

        if (PinUtil.checkPin(pin, pinHash)) {
            return true;
        }
        throw new DomainException("PIN de transação inválido.");
    }


    public void logout(String sessionToken) {
        activeSessions.remove(sessionToken);
    }

    public User getSessionUser(String sessionToken) {
        return activeSessions.get(sessionToken);
    }

}