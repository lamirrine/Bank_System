package model.services;

import model.dao.IAccountDAO;
import model.dao.ITransactionDAO;
import model.dao.impl.AccountDAO;
import model.dao.impl.TransactionDAO;
import model.entities.Account;
import model.entities.Transaction;
import model.enums.AccountStatus;
import model.enums.AccountType;
import model.enums.TransactionType;
import model.enums.TransactionStatus;
import java.sql.SQLException;
import java.util.*;

public class AccountService {

    private IAccountDAO accountDAO;
    private ITransactionDAO transactionDAO;
    private AuthenticationService authService;
    private BankService bankService;

    // Construtor para Injeção de Dependência
    public AccountService(IAccountDAO accountDAO, ITransactionDAO transactionDAO, AuthenticationService authService, BankService bankService) {
        this.accountDAO = accountDAO;
        this.transactionDAO = transactionDAO;
        this.authService = authService;
        this.bankService = bankService;
    }

    public AccountService() {
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
        this.authService = new AuthenticationService(null, this.accountDAO);
        this.bankService = new BankService();
    }

    public AccountService(IAccountDAO accountDAO) {
        this.accountDAO = accountDAO;
        this.transactionDAO = new TransactionDAO();
        this.authService = new AuthenticationService(null, this.accountDAO);
        this.bankService = new BankService();
    }

    public void setAuthService(AuthenticationService authService) {
        this.authService = authService;
    }

    public void setTransactionDAO(ITransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    private AuthenticationService getAuthService() {
        if (authService == null) {
            // Inicializa com as dependências necessárias
            this.authService = new AuthenticationService(null, this.accountDAO);
        }
        return authService;
    }

    public List<Account> getAllAccounts() {
        try {
            return accountDAO.findAll();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todas as contas: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Retorna lista vazia em caso de erro
        }
    }

    public Account createNewAccount(int customerId, String accountType, String pin) throws Exception {
        if (pin == null || pin.length() != 4 || !pin.matches("\\d+")) {
            throw new IllegalArgumentException("PIN deve conter exatamente 4 dígitos numéricos");
        }

        try {
            // Gerar número de conta único
            String accountNumber = generateAccountNumber();

            // Criar hash do PIN
            String pinHash = utils.PasswordUtil.hashPassword(pin);

            // Determinar tipo de conta
            model.enums.AccountType type = model.enums.AccountType.valueOf(accountType.toUpperCase());

            // Definir limites baseados no tipo de conta
            double dailyWithdrawLimit = type == model.enums.AccountType.CORRENTE ? 3250.00 : 5000.00;
            double dailyTransferLimit = type == model.enums.AccountType.CORRENTE ? 10000.00 : 20000.00;

            // Criar conta
            Account account = new Account(
                    0, // ID será gerado pelo banco
                    accountNumber,
                    type,
                    0.0, // Saldo inicial zero
                    new Date(),
                    null, // Data de fechamento (null)
                    model.enums.AccountStatus.ATIVA,
                    customerId,
                    1, // ID da agência padrão (ajuste conforme necessário)
                    dailyWithdrawLimit,
                    dailyTransferLimit,
                    pinHash
            );

            // Salvar conta no banco
            accountDAO.save(account);

            System.out.println("Nova conta criada: " + accountNumber + " - Tipo: " + accountType);
            return account;

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

    public boolean accountNumberExists(String accountNumber) {
        try {
            Account account = accountDAO.findByAccountNumber(accountNumber);
            return account != null;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Account> findAccountsByCustomerId(int customerId) {
        try {
            return accountDAO.findByCustomerId(customerId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Account findAccountById(int accountId) {
        try {
            return accountDAO.findById(accountId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Account> findAccountsByFilters(String accountType, String status) {
        try {
            return accountDAO.findByFilters(accountType, status);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar contas com filtros: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Obtém estatísticas gerais das contas
     */
    public Map<String, Object> getAccountStats() {
        Map<String, Object> stats = new HashMap<>();
        try {
            List<Account> accounts = accountDAO.findAll();

            long totalAccounts = accounts.size();
            long activeAccounts = accounts.stream()
                    .filter(acc -> acc.getStatus() == AccountStatus.ATIVA)
                    .count();
            double totalBalance = accounts.stream()
                    .mapToDouble(Account::getBalance)
                    .sum();
            long correnteCount = accounts.stream()
                    .filter(acc -> acc.getAccountType() == AccountType.CORRENTE)
                    .count();
            long poupancaCount = accounts.stream()
                    .filter(acc -> acc.getAccountType() == AccountType.POUPANCA)
                    .count();

            stats.put("totalAccounts", totalAccounts);
            stats.put("activeAccounts", activeAccounts);
            stats.put("totalBalance", totalBalance);
            stats.put("correnteCount", correnteCount);
            stats.put("poupancaCount", poupancaCount);

        } catch (SQLException e) {
            System.err.println("Erro ao calcular estatísticas: " + e.getMessage());
            e.printStackTrace();
        }
        return stats;
    }

    public Account findAccountByNumber(String accountNumber) {
        try {
            return accountDAO.findByAccountNumber(accountNumber);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateAccountBalance(int accountId, double newBalance) {
        return accountDAO.updateBalance(accountId, newBalance);
    }

    public double getTotalBalance(int customerId) {
        List<Account> accounts = findAccountsByCustomerId(customerId);
        if (accounts == null || accounts.isEmpty()) {
            return 0.0;
        }

        double total = 0.0;
        for (Account account : accounts) {
            total += account.getBalance();
        }
        return total;
    }

    public boolean deposit(int accountId, double amount) throws Exception {
        if (amount <= 0) {
            throw new IllegalArgumentException("Valor de depósito deve ser positivo.");
        }

        // Validação de valor mínimo e máximo
        if (amount < 100.00) {
            throw new IllegalArgumentException("Valor mínimo para depósito é MZN 100,00.");
        }

        if (amount > 50000.00) {
            throw new IllegalArgumentException("Valor máximo por depósito é MZN 50.000,00.");
        }

        // 1. Obter a conta
        Account account = accountDAO.findById(accountId);
        if (account == null) {
            throw new Exception("Conta de destino não encontrada.");
        }

        // 2. Verificar se a conta está ativa
        if (account.getStatus() != model.enums.AccountStatus.ATIVA) {
            throw new Exception("Conta não está ativa. Não é possível realizar depósitos.");
        }

        // 3. Calcular novo saldo
        double newBalance = account.getBalance() + amount;

        try {
            // 4. Verificar se transactionDAO está inicializado
            if (transactionDAO == null) {
                throw new Exception("Sistema de transações não disponível. Tente novamente.");
            }

            // 5. Atualiza o saldo no banco de dados
            boolean balanceUpdated = accountDAO.updateBalance(accountId, newBalance);
            if (!balanceUpdated) {
                throw new Exception("Falha ao atualizar saldo da conta.");
            }

            // 6. Registra a transação
            Transaction transaction = new Transaction(
                    0, // ID gerado pelo BD
                    TransactionType.DEPOSITO,
                    amount,
                    new Date(),
                    TransactionStatus.CONCLUIDA,
                    "Depósito em conta - Disponível imediatamente",
                    accountId,
                    0, // Sem conta destino
                    newBalance,
                    0.0 // Sem taxas para depósitos
            );

            boolean transactionSaved = transactionDAO.save(transaction);
            if (!transactionSaved) {
                // Em um sistema real, deveríamos reverter a atualização do saldo aqui
                throw new Exception("Falha ao registrar transação.");
            }

            System.out.println("Depósito realizado: Conta " + accountId +
                    ", Valor: " + amount + ", Novo saldo: " + newBalance);

            return true;

        } catch (SQLException e) {
            System.err.println("Erro SQL durante depósito: " + e.getMessage());
            throw new Exception("Falha no sistema durante o depósito. Tente novamente.", e);
        } catch (Exception e) {
            System.err.println("Erro durante depósito: " + e.getMessage());
            throw e; // Re-lança a exceção para tratamento superior
        }
    }

    public Account getAccountForDeposit(int accountId) throws Exception {
        Account account = accountDAO.findById(accountId);
        if (account == null) {
            throw new Exception("Conta não encontrada.");
        }
        return account;
    }

    public boolean withdraw(int accountId, double amount, String pin) throws Exception {
        if (amount <= 0) {
            throw new IllegalArgumentException("Valor de levantamento inválido.");
        }

        Account account = accountDAO.findById(accountId);
        if (account == null) {
            throw new Exception("Conta de origem não encontrada.");
        }

        // 1. Validação de Segurança: PIN - CORRIGIDO
        try {
            if (!getAuthService().validatePin(accountId, pin)) {
                throw new Exception("PIN de transação inválido.");
            }
        } catch (model.exceptions.DomainException e) {
            // Converte DomainException para Exception genérica com mensagem amigável
            throw new Exception(e.getMessage());
        }

        // 2. Validação de Saldo e Limites
        if (amount > account.getBalance()) {
            throw new Exception("Saldo insuficiente.");
        }
        if (amount > account.getDailyWithdrawLimit()) {
            throw new Exception("Valor excede o limite diário de levantamento.");
        }

        // 3. Lógica: Calcular novo saldo
        double newBalance = account.getBalance() - amount;

        try {
            accountDAO.updateBalance(accountId, newBalance);

            Transaction transaction = new Transaction(
                    0,
                    TransactionType.LEVANTAMENTO,
                    amount,
                    new Date(),
                    TransactionStatus.CONCLUIDA,
                    "Levantamento em balcão/ATM",
                    accountId,
                    0,
                    newBalance,
                    0.0
            );

            if (transactionDAO == null) {
                throw new Exception("Sistema de transações não disponível.");
            }

            transactionDAO.save(transaction);

            return true;
        } catch (SQLException e) {
            throw new Exception("Falha crítica na operação de levantamento.", e);
        }
    }

    public boolean transfer(int sourceAccountId, String destinationAccountNumber, double amount, String pin) throws Exception {
        if (amount <= 0) {
            throw new IllegalArgumentException("Valor de transferência inválido.");
        }

        Account sourceAccount = accountDAO.findById(sourceAccountId);
        Account destinationAccount = accountDAO.findByAccountNumber(destinationAccountNumber);

        // Validações
        if (sourceAccount == null || destinationAccount == null) {
            throw new Exception("Conta de origem ou destino não encontrada.");
        }

        // 1. Validação de Segurança: PIN - CORRIGIDO para usar getAuthService()
        try {
            if (!getAuthService().validatePin(sourceAccountId, pin)) {
                throw new Exception("PIN de transação inválido.");
            }
        } catch (model.exceptions.DomainException e) {
            throw new Exception(e.getMessage());
        } catch (Exception e) {
            throw new Exception("Erro ao validar PIN: " + e.getMessage());
        }

        if (amount > sourceAccount.getBalance()) {
            throw new Exception("Saldo insuficiente.");
        }

        if (amount > sourceAccount.getDailyTransferLimit()) {
            throw new Exception("Valor excede o limite diário de transferência.");
        }

        // 1. Lógica de Taxas
        double fee = 0.0;
        // Se as contas estiverem em bancos diferentes, aplica-se a taxa
        if (sourceAccount.getAgencyId() != destinationAccount.getAgencyId()) {
            fee = amount * bankService.getTransferFeeRate();
        }

        double totalDebit = amount + fee;
        if (totalDebit > sourceAccount.getBalance()) {
            throw new Exception("Saldo insuficiente para cobrir valor + taxa (" + totalDebit + " MZN).");
        }

        try {
            // DÉBITO (CONTA DE ORIGEM)
            double newSourceBalance = sourceAccount.getBalance() - totalDebit;
            accountDAO.updateBalance(sourceAccountId, newSourceBalance);

            Transaction debitTransaction = new Transaction(
                    0, TransactionType.TRANSFERENCIA, totalDebit, new Date(), TransactionStatus.CONCLUIDA,
                    "Transferência enviada para " + destinationAccountNumber, sourceAccountId, destinationAccount.getAccountId(),
                    newSourceBalance, fee);
            transactionDAO.save(debitTransaction);

            // CRÉDITO (CONTA DE DESTINO)
            double newDestinationBalance = destinationAccount.getBalance() + amount;
            accountDAO.updateBalance(destinationAccount.getAccountId(), newDestinationBalance);

            Transaction creditTransaction = new Transaction(
                    0, TransactionType.TRANSFERENCIA, amount, new Date(), TransactionStatus.CONCLUIDA,
                    "Transferência recebida de " + sourceAccount.getAccountNumber(), destinationAccount.getAccountId(),
                    sourceAccount.getAccountId(), newDestinationBalance, 0.0);
            transactionDAO.save(creditTransaction);

            return true;
        } catch (SQLException e) {
            throw new Exception("Falha na transferência. Tente novamente.", e);
        }
    }

}