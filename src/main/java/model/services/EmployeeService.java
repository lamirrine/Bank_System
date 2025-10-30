package model.services;

import model.dao.*;
import model.entities.*;
import model.enums.AccessLevel;
import model.enums.AccountStatus;
import java.sql.SQLException;
import java.util.Date;

/**
 * Serviço responsável pela gestão de funcionários e operações internas (ex: abertura de conta).
 * Aplica regras de autorização baseadas no nível de acesso.
 */
public class EmployeeService {

    // Dependências injetadas
    private IEmployeeDAO employeeDAO;
    private IAccountDAO accountDAO;
    private IAgencyDAO agencyDAO;
    private ITransactionDAO transactionDAO; // Para registrar o depósito inicial

    // Construtor para Injeção de Dependência
    public EmployeeService(IEmployeeDAO employeeDAO, IAccountDAO accountDAO, IAgencyDAO agencyDAO, ITransactionDAO transactionDAO) {
        this.employeeDAO = employeeDAO;
        this.accountDAO = accountDAO;
        this.agencyDAO = agencyDAO;
        this.transactionDAO = transactionDAO;
    }

    /**
     * Verifica se o funcionário tem a permissão mínima necessária.
     */
    private boolean hasPermission(Employee employee, AccessLevel requiredLevel) {
        return employee.getAccessLevel().ordinal() >= requiredLevel.ordinal();
    }

    /**
     * Permite a um funcionário (no mínimo STAFF) abrir uma nova conta para um cliente existente.
     * @param openerEmployee Funcionário que está realizando a operação (para verificação de permissão).
     * @param customerId ID do cliente que será o dono da conta.
     * @param newAccount Objeto Account com os dados de abertura.
     * @param initialDeposit Valor do depósito inicial.
     * @return A nova Account com o ID/Número gerado.
     * @throws Exception Se a permissão for negada ou a operação falhar.
     */
    public Account openNewAccount(Employee openerEmployee, int customerId, Account newAccount, double initialDeposit) throws Exception {

        // 1. Autorização: Requer nível mínimo de STAFF
        if (!hasPermission(openerEmployee, AccessLevel.STAFF)) {
            throw new SecurityException("Acesso negado: Nível de acesso insuficiente para abrir contas.");
        }

        // 2. Preenchimento de Dados Essenciais
        if (newAccount.getOpenDate() == null) {
            newAccount.setOpenDate(new Date());
        }
        newAccount.setStatus(AccountStatus.ATIVA);
        newAccount.setOwnerCustomerId(customerId);
        newAccount.setBalance(0.0); // O depósito inicial será feito separadamente

        // TODO: Gerar newAccount.setAccountNumber(generateUniqueNumber());

        // 3. Persistência (Transação Atómica: Abrir Conta + Depósito Inicial)
        try {
            // A. Salva a nova conta (obtem o novo account_id)
            accountDAO.save(newAccount);

            // B. Realiza o depósito inicial (se houver)
            if (initialDeposit > 0) {

                // Atualiza o saldo diretamente (embora o AccountService seja mais indicado para isso)
                accountDAO.updateBalance(newAccount.getAccountId(), initialDeposit);
                newAccount.setBalance(initialDeposit); // Atualiza o objeto em memória

                // C. Registra a transação de depósito inicial
                Transaction transaction = new Transaction(
                        0, model.enums.TransactionType.DEPOSITO, initialDeposit, new Date(),
                        model.enums.TransactionStatus.CONCLUIDA, "Depósito Inicial - Abertura de Conta",
                        newAccount.getAccountId(), 0, initialDeposit, 0.0);
                transactionDAO.save(transaction);
            }

            return newAccount;
        } catch (SQLException e) {
            throw new Exception("Falha crítica ao abrir nova conta.", e);
        }
    }

    /**
     * Permite a um funcionário (MANAGER/ADMIN) atualizar o estado de uma agência.
     */
    public void updateAgencyDetails(Employee updaterEmployee, Agency agency) throws Exception {
        if (!hasPermission(updaterEmployee, AccessLevel.MANAGER)) {
            throw new SecurityException("Acesso negado: Apenas Gerentes e Admin podem atualizar Agências.");
        }
        try {
            agencyDAO.update(agency);
        } catch (SQLException e) {
            throw new Exception("Falha ao atualizar dados da agência.", e);
        }
    }
}