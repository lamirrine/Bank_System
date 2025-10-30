package model.services;

import model.dao.IAccountDAO;
import model.dao.ITransactionDAO;
import model.entities.Account;
import model.entities.Transaction;
import model.enums.TransactionType;
import model.enums.TransactionStatus;
import java.sql.SQLException;
import java.util.Date;

/**
 * Serviço responsável por toda a lógica de operações financeiras (Depósito, Levantamento, Transferência).
 * Implementa a lógica de transação atómica (atualização de saldo + registro).
 */
public class AccountService {

    // Dependências injetadas
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

    /**
     * Realiza um depósito numa conta.
     * Não requer validação de PIN.
     * @param accountId ID da conta destino.
     * @param amount Valor do depósito.
     * @return true se concluído com sucesso.
     * @throws Exception Se a conta for inválida ou o valor for negativo.
     */
    public boolean deposit(int accountId, double amount) throws Exception {
        if (amount <= 0) {
            throw new IllegalArgumentException("Valor de depósito inválido.");
        }

        // 1. Obter a conta (necessário para o saldo atual)
        Account account = accountDAO.findById(accountId);
        if (account == null) {
            throw new Exception("Conta de destino não encontrada.");
        }

        // 2. Lógica: Calcular novo saldo
        double newBalance = account.getBalance() + amount;

        // --- TRANSAÇÃO ATÓMICA: Atualização de Saldo e Registro ---
        try {
            // A. Atualiza o saldo no banco de dados
            accountDAO.updateBalance(accountId, newBalance);

            // B. Registra a transação
            Transaction transaction = new Transaction(
                    0, // ID gerado pelo BD
                    TransactionType.DEPOSITO,
                    amount,
                    new Date(),
                    TransactionStatus.CONCLUIDA,
                    "Depósito em conta",
                    accountId,
                    0, // Sem conta destino
                    newBalance,
                    0.0
            );
            transactionDAO.save(transaction);

            return true;
        } catch (SQLException e) {
            // Em um sistema real, aqui o Service precisa garantir o ROLLBACK
            throw new Exception("Falha crítica na operação de depósito.", e);
        }
    }

    /**
     * Realiza um levantamento (saque) de uma conta.
     * Requer validação de PIN e verifica limites.
     * @param accountId ID da conta de origem.
     * @param amount Valor do levantamento.
     * @param pin PIN de 4 dígitos para autorização.
     * @return true se concluído com sucesso.
     * @throws Exception Se o PIN ou saldo for inválido.
     */
    public boolean withdraw(int accountId, double amount, String pin) throws Exception {
        if (amount <= 0) {
            throw new IllegalArgumentException("Valor de levantamento inválido.");
        }

        Account account = accountDAO.findById(accountId);
        if (account == null) {
            throw new Exception("Conta de origem não encontrada.");
        }

        // 1. Validação de Segurança: PIN
        if (!authService.validatePin(accountId, pin)) {
            throw new Exception("PIN de transação inválido.");
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

        // --- TRANSAÇÃO ATÓMICA ---
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
            transactionDAO.save(transaction);

            return true;
        } catch (SQLException e) {
            throw new Exception("Falha crítica na operação de levantamento.", e);
        }
    }

    /**
     * Realiza uma transferência entre duas contas.
     * A mais complexa por envolver débito, crédito e taxas.
     */
    public boolean transfer(int sourceAccountId, String destinationAccountNumber, double amount, String pin) throws Exception {
        // Obter contas
        Account sourceAccount = accountDAO.findById(sourceAccountId);
        Account destinationAccount = accountDAO.findByAccountNumber(destinationAccountNumber);

        // Validações
        if (sourceAccount == null || destinationAccount == null) {
            throw new Exception("Conta de origem ou destino não encontrada.");
        }
        if (!authService.validatePin(sourceAccountId, pin)) {
            throw new Exception("PIN de transação inválido.");
        }
        if (amount > sourceAccount.getBalance()) {
            throw new Exception("Saldo insuficiente.");
        }
        // TODO: Adicionar validação de limite diário de transferência (sourceAccount.getDailyTransferLimit())

        // 1. Lógica de Taxas
        double fee = 0.0;
        // Se as contas estiverem em bancos diferentes, aplica-se a taxa
        if (sourceAccount.getAgencyId() != destinationAccount.getAgencyId()) {
            // Chamada ao BankService para obter a taxa global
            fee = amount * bankService.getTransferFeeRate();
        }

        double totalDebit = amount + fee;
        if (totalDebit > sourceAccount.getBalance()) {
            throw new Exception("Saldo insuficiente para cobrir valor + taxa (" + totalDebit + " MZN).");
        }

        // --- TRANSAÇÃO CRÍTICA (REQUER GESTÃO DE TRANSAÇÃO JDBC) ---
        // Em um Service real, você iniciaria uma transação JDBC aqui.
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

            // Se tudo correr bem, faria COMMIT
            return true;
        } catch (SQLException e) {
            // Se falhar (ex: a atualização do débito falhou), você faria ROLLBACK aqui.
            throw new Exception("Falha na transferência. Tente novamente.", e);
        }
    }
}