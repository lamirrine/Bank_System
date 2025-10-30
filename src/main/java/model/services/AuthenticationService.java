package model.services;

import model.dao.IAccountDAO;
import model.dao.IUserDAO;
import model.entities.User;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;
import model.exceptions.DomainException;
import utils.security.PasswordUtil;
import utils.security.PinUtil;

/**
 * Serviço responsável pela segurança e gestão de sessões de login e PIN.
 */
public class AuthenticationService {

    // Dependências: Injeção das Interfaces DAO
    private IUserDAO userDAO;
    private IAccountDAO accountDAO;

    // Gestão de Sessão Simples (Em memória: Token -> Usuário)
    private Map<String, User> activeSessions;

    // Construtor para Injeção de Dependência
    public AuthenticationService(IUserDAO userDAO, IAccountDAO accountDAO) {
        this.userDAO = userDAO;
        this.accountDAO = accountDAO;
        this.activeSessions = new HashMap<>();
    }

    /**
     * Tenta autenticar um usuário usando email e senha.
     * @param email Email do usuário.
     * @param password Senha em texto claro.
     * @return O objeto User autenticado (Customer ou Employee).
     * @throws Exception Se o usuário não for encontrado ou a senha for inválida.
     */
    public User login(String email, String password) throws Exception {
        User user = userDAO.findByEmail(email);

        if (user == null) {
            throw new DomainException("Utilizador não encontrado."); // Usando DomainException
        }

        // A chamada de verificação de senha usa o novo utilitário
        if (PasswordUtil.checkPassword(password, user.getPassHash())) {
            // ... Lógica de sessão ...
            return user;
        } else {
            throw new DomainException("Senha inválida."); // Usando DomainException
        }
    }

    /**
     * Valida o PIN de transação da conta antes de operações críticas.
     * @param accountId ID da conta para buscar o PIN.
     * @param pin PIN fornecido pelo usuário em texto claro.
     * @return true se o PIN for válido.
     * @throws Exception Se o PIN não estiver configurado ou ocorrer erro de DB.
     */
    public boolean validatePin(int accountId, String pin) throws Exception {
        String pinHash = accountDAO.getPinHash(accountId);

        if (pinHash == null) {
            throw new DomainException("PIN de transação não configurado para esta conta.");
        }

        // A chamada de verificação de PIN usa o novo utilitário
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