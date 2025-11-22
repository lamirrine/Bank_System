package model.services;

import model.dao.IAccountDAO;
import model.dao.impl.AccountDAO;
import model.entities.Account;
import model.entities.Customer;
import model.enums.AccountStatus;
import model.enums.AccountType;
import model.utils.PasswordUtil;

import java.sql.SQLException;
import java.util.Date;
import java.util.Random;

public class AccountCreationService {

    private IAccountDAO accountDAO;

    public AccountCreationService() {
        this.accountDAO = new AccountDAO();
    }

    public AccountCreationService(IAccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public boolean createCurrentAccountForCustomer(Customer customer, String pin) throws Exception {
        try {
            // Validar PIN
            if (pin == null || pin.length() != 4 || !pin.matches("\\d+")) {
                throw new IllegalArgumentException("PIN deve conter 4 dígitos numéricos");
            }

            // Gerar número de conta único
            String accountNumber = generateAccountNumber();

            // Criar hash do PIN
            String pinHash = PasswordUtil.hashPassword(pin);

            // Criar conta corrente
            Account account = new Account(
                    0, // ID será gerado pelo banco
                    accountNumber,
                    AccountType.CORRENTE,
                    0.0, // Saldo inicial zero
                    new Date(),
                    null, // Data de fechamento (null)
                    AccountStatus.ATIVA,
                    customer.getUserId(),
                    1,
                    3250.00, // Limite diário de levantamento padrão
                    10000.00, // Limite diário de transferência padrão
                    pinHash
            );

            // Salvar conta no banco
            accountDAO.save(account);

            System.out.println("Conta criada: " + accountNumber + " para cliente: " + customer.getEmail());
            return true;

        } catch (SQLException e) {
            throw new Exception("Erro ao criar conta: " + e.getMessage(), e);
        }
    }

    private String generateAccountNumber() {
        Random random = new Random();
        // Gera número de conta no formato: 10000000 - 99999999
        int accountNum = 10000000 + random.nextInt(90000000);
        return String.valueOf(accountNum);
    }
}