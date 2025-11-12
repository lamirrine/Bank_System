package model.services;

import model.dao.IUserDAO;
import model.dao.IAccountDAO;
import model.dao.impl.UserDAO;
import model.dao.impl.AccountDAO;
import model.entities.User;
import utils.PasswordUtil;

import java.sql.SQLException;

public class PasswordChangeService {

    private IUserDAO userDAO;
    private IAccountDAO accountDAO;

    public PasswordChangeService() {
        this.userDAO = new UserDAO();
        this.accountDAO = new AccountDAO();
    }

    public PasswordChangeService(IUserDAO userDAO, IAccountDAO accountDAO) {
        this.userDAO = userDAO;
        this.accountDAO = accountDAO;
    }

    public boolean changeUserPassword(int userId, String currentPassword, String newPassword) throws Exception {
        System.out.println("Tentando alterar senha para userId: " + userId); // DEBUG

        try {
            // Buscar usuário
            User user = userDAO.findById(userId);
            if (user == null) {
                System.err.println("Usuário não encontrado para ID: " + userId); // DEBUG
                throw new Exception("Usuário não encontrado.");
            }

            System.out.println("Usuário encontrado: " + user.getEmail()); // DEBUG

            // Verificar senha atual
            boolean currentPasswordCorrect = PasswordUtil.checkPassword(currentPassword, user.getPassHash());
            System.out.println("Senha atual correta: " + currentPasswordCorrect); // DEBUG

            if (!currentPasswordCorrect) {
                throw new Exception("Senha atual incorreta.");
            }

            // Validar nova senha
            if (newPassword == null || newPassword.length() < 6) {
                throw new Exception("A nova senha deve ter pelo menos 6 caracteres.");
            }

            // Criar hash da nova senha
            String newPasswordHash = PasswordUtil.hashPassword(newPassword);
            System.out.println("Novo hash gerado"); // DEBUG

            // Atualizar no banco
            boolean updated = userDAO.updatePassword(userId, newPasswordHash);
            System.out.println("Senha atualizada no banco: " + updated); // DEBUG

            if (!updated) {
                throw new Exception("Falha ao atualizar senha no banco de dados.");
            }

            return true;

        } catch (SQLException e) {
            System.err.println("Erro SQL ao alterar senha: " + e.getMessage()); // DEBUG
            throw new Exception("Erro de banco de dados: " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Erro geral ao alterar senha: " + e.getMessage()); // DEBUG
            throw e;
        }
    }

    public boolean changeAccountPin(int accountId, String currentPin, String newPin) throws Exception {
        System.out.println("Tentando alterar PIN para accountId: " + accountId); // DEBUG

        try {
            // Validar novo PIN
            if (newPin == null || newPin.length() != 4 || !newPin.matches("\\d+")) {
                throw new Exception("O novo PIN deve conter exatamente 4 dígitos numéricos.");
            }

            // Buscar hash do PIN atual
            String currentPinHash = accountDAO.getPinHash(accountId);
            if (currentPinHash == null) {
                throw new Exception("Conta não encontrada.");
            }

            // Verificar PIN atual
            boolean currentPinCorrect = PasswordUtil.checkPassword(currentPin, currentPinHash);
            System.out.println("PIN atual correto: " + currentPinCorrect); // DEBUG

            if (!currentPinCorrect) {
                throw new Exception("PIN atual incorreto.");
            }

            // Criar hash do novo PIN
            String newPinHash = PasswordUtil.hashPassword(newPin);

            // Atualizar no banco
            boolean updated = accountDAO.updatePin(accountId, newPinHash);

            if (!updated) {
                throw new Exception("Falha ao atualizar PIN no banco de dados.");
            }

            return true;

        } catch (SQLException e) {
            throw new Exception("Erro de banco de dados: " + e.getMessage(), e);
        }
    }
}