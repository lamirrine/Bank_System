package controller;

import model.entities.Account;
import model.entities.Customer;
import model.services.AccountService;
import model.services.CustomerService;
import model.services.StatementService;
import view.DashboardView;
import view.login.componet.Loginview;

import javax.swing.*;
import java.util.List;

public class DashboardController {
    private DashboardView view;
    private AccountService accountService;
    private StatementService statementService;
    private CustomerService customerService;
    private Customer currentCustomer;
    private List<Account> customerAccounts;
    private Account primaryAccount;
    private DepositController depositController;
    private WithdrawController withdrawController;
    private TransferController transferController;
    private StatementController statementController;

    public DashboardController(DashboardView view, AccountService accountService,
                               StatementService statementService, CustomerService customerService,
                               Customer customer) {
        this.view = view;
        this.accountService = accountService;
        this.statementService = statementService;
        this.customerService = customerService;
        this.currentCustomer = customer;

        System.out.println("DashboardController iniciado para: " + customer.getFullName());

        initializeController();
        loadDashboardData();
    }

    private void initializeController() {
        try {
            List<Account> accountsForUse = customerAccounts != null ? customerAccounts : List.of();

            // Inicializar controllers
            this.depositController = new DepositController(
                    view.getDepositView(),
                    accountService,
                    accountsForUse,
                    currentCustomer.getUserId()
            );

            this.withdrawController = new WithdrawController(
                    view.getWithdrawView(),
                    accountService,
                    accountsForUse,
                    currentCustomer.getUserId()
            );

            this.transferController = new TransferController(
                    view.getTransferView(),
                    accountService,
                    accountsForUse,
                    currentCustomer.getUserId()
            );

            this.statementController = new StatementController(
                    view.getStatementView(),
                    statementService,
                    accountsForUse,
                    currentCustomer.getUserId()
            );

            setupSidebarListeners();
            setupDepositNavigation();
            setupWithdrawNavigation();
            setupTransferNavigation();
            setupStatementNavigation();

        } catch (Exception e) {
            System.err.println("Erro ao inicializar controllers: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupSidebarListeners() {
        // Home button
        view.getSidebarView().getHomeButton().addActionListener(e -> showHomepage());

        // Deposit button
        view.getSidebarView().getDepositBtn().addActionListener(e -> {
            view.getSidebarView().setActiveButton(view.getSidebarView().getDepositBtn());
            openDeposit();
        });

        // Withdraw button
        view.getSidebarView().getWithdrawBtn().addActionListener(e -> {
            view.getSidebarView().setActiveButton(view.getSidebarView().getWithdrawBtn());
            openWithdraw();
        });

        // Transfer button
        view.getSidebarView().getTransferBtn().addActionListener(e -> {
            view.getSidebarView().setActiveButton(view.getSidebarView().getTransferBtn());
            openTransfer();
        });

        // Statement button
        view.getSidebarView().getStatementBtn().addActionListener(e -> {
            view.getSidebarView().setActiveButton(view.getSidebarView().getStatementBtn());
            openStatement();
        });

        // Logout button
        view.getSidebarView().getLogoutBtn().addActionListener(e -> logout());
    }

    private void setupDepositNavigation() {
        view.getDepositView().getCancelButton().addActionListener(e -> {
            returnToHomepage();
        });
    }

    private void setupWithdrawNavigation() {
        view.getWithdrawView().getCancelButton().addActionListener(e -> {
            returnToHomepage();
        });
    }

    private void setupTransferNavigation() {
        view.getTransferView().getCancelButton().addActionListener(e -> {
            returnToHomepage();
        });
    }

    private void setupStatementNavigation() {
        view.getStatementView().getCancelButton().addActionListener(e -> {
            returnToHomepage();
        });
    }

    // NOVO MÉTODO: showHomepage
    private void showHomepage() {
        try {
            view.getSidebarView().setActiveButton(view.getSidebarView().getHomeButton());
            view.showHomepage();
            // Atualizar dados na homepage se necessário
            refreshDashboardData();
        } catch (Exception ex) {
            System.err.println("Erro ao voltar para homepage: " + ex.getMessage());
        }
    }

    private void openDeposit() {
        try {
            if (customerAccounts != null) {
                depositController.updateCustomerAccounts(customerAccounts);
            }
            depositController.clearForm();
            view.showDepositView();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Erro ao abrir tela de depósito: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openWithdraw() {
        try {
            if (customerAccounts != null) {
                withdrawController.updateCustomerAccounts(customerAccounts);
            }
            withdrawController.clearForm();
            view.showWithdrawView();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Erro ao abrir tela de levantamento: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openTransfer() {
        try {
            if (customerAccounts != null) {
                transferController.updateCustomerAccounts(customerAccounts);
            }
            transferController.clearForm();
            view.showTransferView();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Erro ao abrir tela de transferência: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openStatement() {
        try {
            if (customerAccounts != null) {
                statementController.updateCustomerAccounts(customerAccounts);
            }
            view.showStatementView();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Erro ao abrir tela de extrato: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void returnToHomepage() {
        showHomepage();
    }

    private void refreshDashboardData() {
        // Recarregar dados atualizados
        loadCustomerAccounts();
        loadRecentTransactions();
        updateLimitsDisplay();
    }

    private void loadDashboardData() {
        try {
            // Carregar dados reais do cliente
            view.setUserInfo(currentCustomer.getFullName(), getPrimaryAccountNumber());
            view.getHomepageView().updateUserInfo(currentCustomer.getFirstName());
            loadCustomerAccounts();
            loadRecentTransactions();
            updateLimitsDisplay();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Erro ao carregar dados: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCustomerAccounts() {
        System.out.println("Carregando contas para customerId: " + currentCustomer.getUserId());

        try {
            customerAccounts = accountService.findAccountsByCustomerId(currentCustomer.getUserId());
            System.out.println("Número de contas encontradas: " + (customerAccounts != null ? customerAccounts.size() : "null"));

            if (customerAccounts != null && !customerAccounts.isEmpty()) {
                // Log para debug
                for (Account acc : customerAccounts) {
                    System.out.println("Conta encontrada: " + acc.getAccountNumber() + " - Saldo: " + acc.getBalance());
                }

                // Usar a primeira conta como principal
                primaryAccount = customerAccounts.get(0);
                System.out.println("Conta principal: " + primaryAccount.getAccountNumber());

                // Atualizar a view com as contas
                view.getHomepageView().updateAccounts(customerAccounts);

                // Calcular saldo total SOMANDO todas as contas
                double totalBalance = calculateTotalBalance(customerAccounts);
                System.out.println("Saldo total calculado: " + totalBalance);
                view.setBalance(totalBalance);

                // Atualizar todos os controllers com as contas atualizadas
                updateAllControllers();

            } else {
                System.out.println("Nenhuma conta encontrada!");
                view.getHomepageView().updateAccounts(List.of());
                view.setBalance(0.0);

                // Atualizar controllers com lista vazia
                updateAllControllers();
            }
        } catch (Exception e) {
            System.err.println("ERRO ao carregar contas: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(view,
                    "Erro ao carregar contas: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateAllControllers() {
        List<Account> accounts = customerAccounts != null ? customerAccounts : List.of();

        if (depositController != null) {
            depositController.updateCustomerAccounts(accounts);
        }
        if (withdrawController != null) {
            withdrawController.updateCustomerAccounts(accounts);
        }
        if (transferController != null) {
            transferController.updateCustomerAccounts(accounts);
        }
        if (statementController != null) {
            statementController.updateCustomerAccounts(accounts);
        }
    }

    private double calculateTotalBalance(List<Account> accounts) {
        if (accounts == null || accounts.isEmpty()) {
            return 0.0;
        }

        double total = 0.0;
        for (Account account : accounts) {
            total += account.getBalance();
        }
        return total;
    }

    private void loadRecentTransactions() {
        System.out.println("Carregando transações...");

        try {
            List<model.entities.Transaction> transactions = List.of();

            if (primaryAccount != null) {
                System.out.println("Carregando transações da conta: " + primaryAccount.getAccountId());
                transactions = statementService.getRecentTransactionsByAccount(
                        primaryAccount.getAccountId(), 10);
            } else if (customerAccounts != null && !customerAccounts.isEmpty()) {
                Account firstAccount = customerAccounts.get(0);
                System.out.println("Carregando transações da primeira conta: " + firstAccount.getAccountId());
                transactions = statementService.getRecentTransactionsByAccount(
                        firstAccount.getAccountId(), 10);
            } else {
                System.out.println("Carregando transações do cliente: " + currentCustomer.getUserId());
                transactions = statementService.getRecentTransactionsByCustomer(
                        currentCustomer.getUserId(), 10);
            }

            System.out.println("Número de transações encontradas: " + transactions.size());
            view.getHomepageView().updateTransactions(transactions);

        } catch (Exception e) {
            System.err.println("Erro ao carregar transações: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateLimitsDisplay() {
        System.out.println("Atualizando limites...");

        if (primaryAccount != null) {
            view.getHomepageView().updateLimits(
                    primaryAccount.getDailyWithdrawLimit(),
                    primaryAccount.getDailyTransferLimit()
            );
        } else if (customerAccounts != null && !customerAccounts.isEmpty()) {
            Account firstAccount = customerAccounts.get(0);
            view.getHomepageView().updateLimits(
                    firstAccount.getDailyWithdrawLimit(),
                    firstAccount.getDailyTransferLimit()
            );
        } else {
            view.getHomepageView().updateLimits(3250.00, 10000.00);
        }
    }

    private String getPrimaryAccountNumber() {
        if (primaryAccount != null) {
            return primaryAccount.getAccountNumber();
        } else if (customerAccounts != null && !customerAccounts.isEmpty()) {
            return customerAccounts.get(0).getAccountNumber();
        }
        return "N/A";
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(view,
                "Tem certeza que deseja sair?", "Confirmar Saída",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (statementController != null) {
                statementController.shutdown();
            }

            view.dispose();
            new Loginview().setVisible(true);
        }
    }

    // Getters para acesso externo
    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public List<Account> getCustomerAccounts() {
        return customerAccounts;
    }

    public Account getPrimaryAccount() {
        return primaryAccount;
    }
}