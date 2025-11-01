// controller/DashboardController.java
package controller;

import model.entities.Account;
import model.entities.Customer;
import model.entities.Transaction;
import model.services.AccountService;
import model.services.StatementService;
import view.DashboardView;
import javax.swing.*;
import java.util.List;

public class DashboardController {
    private DashboardView view;
    private AccountService accountService;
    private StatementService statementService;
    private Customer currentCustomer;
    private Account currentAccount;

    public DashboardController(DashboardView view, AccountService accountService,
                               StatementService statementService, Customer customer) {
        this.view = view;
        this.accountService = accountService;
        this.statementService = statementService;
        this.currentCustomer = customer;

        initializeController();
        loadDashboardData();
    }

    private void initializeController() {
        // Configurar listeners dos botões
        view.getDepositBtn().addActionListener(e -> openDeposit());
        view.getWithdrawBtn().addActionListener(e -> openWithdraw());
        view.getTransferBtn().addActionListener(e -> openTransfer());
        view.getStatementBtn().addActionListener(e -> openStatement());
        view.getLogoutBtn().addActionListener(e -> logout());
    }

    private void loadDashboardData() {
        try {
            // Carregar dados reais do cliente
            loadCustomerAccounts();
            loadRecentTransactions();
            updateBalanceDisplay();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Erro ao carregar dados: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCustomerAccounts() {
        // TODO: Implementar busca de contas do cliente
        // List<Account> accounts = accountService.findAccountsByCustomerId(currentCustomer.getUserId());

        // Por enquanto, vamos usar dados mock
        currentAccount = new Account();
        currentAccount.setAccountNumber("12345-X");
        currentAccount.setBalance(12345.67);
        currentAccount.setDailyWithdrawLimit(3250.00);
        currentAccount.setDailyTransferLimit(10000.00);

        view.setBalance(currentAccount.getBalance());
    }

    private void loadRecentTransactions() {
        try {
            // TODO: Implementar busca de transações reais
            // List<Transaction> transactions = statementService.getRecentTransactions(currentAccount.getAccountId(), 5);

            // Por enquanto, vamos usar dados mock
            // As transações já estão sendo exibidas no layout

        } catch (Exception e) {
            System.err.println("Erro ao carregar transações: " + e.getMessage());
        }
    }

    private void updateBalanceDisplay() {
        if (currentAccount != null) {
            view.setBalance(currentAccount.getBalance());
        }
    }

    private void openDeposit() {
        JOptionPane.showMessageDialog(view,
                "Funcionalidade de Depósito em desenvolvimento!",
                "Em Desenvolvimento", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openWithdraw() {
        JOptionPane.showMessageDialog(view,
                "Funcionalidade de Levantamento em desenvolvimento!",
                "Em Desenvolvimento", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openTransfer() {
        JOptionPane.showMessageDialog(view,
                "Funcionalidade de Transferência em desenvolvimento!",
                "Em Desenvolvimento", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openStatement() {
        JOptionPane.showMessageDialog(view,
                "Funcionalidade de Extrato em desenvolvimento!",
                "Em Desenvolvimento", JOptionPane.INFORMATION_MESSAGE);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(view,
                "Tem certeza que deseja sair?", "Confirmar Saída",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            view.dispose();
            // Voltar para a tela de login
            // new LoginController().show();
        }
    }
}