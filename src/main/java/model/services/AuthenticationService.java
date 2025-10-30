package model.services;

import model.dao.IAccountDAO;
import model.dao.IUserDAO;
import model.entities.User;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;

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
        // 1. Buscar o usuário pelo email na Camada DAO
        User user = userDAO.findByEmail(email);

        if (user == null) {
            throw new Exception("Utilizador não encontrado.");
        }

        // 2. Lógica de Segurança: Comparar a senha (DEVE usar um algoritmo de hash seguro)
        // O código final deve usar PasswordUtil.checkPassword(password, user.getPassHash())
        // Usamos uma comparação simples por enquanto:
        if (user.getPassHash() != null && user.getPassHash().equals(password)) {

            // 3. Gerar e armazenar o token de sessão
            String sessionToken = email + System.currentTimeMillis();
            activeSessions.put(sessionToken, user);

            return user;

        } else {
            throw new Exception("Senha inválida.");
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
        // 1. Buscar o hash do PIN na Camada DAO
        String pinHash = accountDAO.getPinHash(accountId);

        if (pinHash == null) {
            throw new Exception("PIN de transação não configurado para esta conta.");
        }

        // 2. Lógica de Segurança: Comparar o PIN (DEVE usar um algoritmo de hash seguro)
        // O código final deve usar PinUtil.checkPin(pin, pinHash)
        // Usamos uma comparação simples por enquanto:
        if (pinHash.equals(pin)) {
            return true;
        }
        return false;
    }

    public void logout(String sessionToken) {
        activeSessions.remove(sessionToken);
    }

    public User getSessionUser(String sessionToken) {
        return activeSessions.get(sessionToken);
    }
}