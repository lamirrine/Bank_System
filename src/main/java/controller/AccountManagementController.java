package controller;

import model.entities.Account;
import model.services.AccountService;
import model.enums.AccountStatus;
import model.services.CustomerService;
import view.admin.AccountManagementView;

import javax.swing.*;
import java.util.List;

public class AccountManagementController {
    private AccountManagementView view;
    private AccountService accountService;
    private JDialog parentDialog;

    public AccountManagementController(AccountManagementView view, AccountService accountService) {
        this.view = view;
        this.accountService = accountService;
        this.parentDialog = null;
        setupListeners();
    }

    public AccountManagementController(AccountManagementView view, AccountService accountService, JDialog parentDialog) {
        this.view = view;
        this.accountService = accountService;
        this.parentDialog = parentDialog;
        setupListeners();
    }

    private void setupListeners() {
        // Botão VOLTAR
        view.getBackBtn().addActionListener(e -> handleBack());

        // Botão FILTRAR
        view.getSearchBtn().addActionListener(e -> handleFilter());

        // Botão NOVA CONTA
        view.getOpenAccountBtn().addActionListener(e -> handleOpenNewAccount());

        // Botão DETALHES
        view.getViewDetailsBtn().addActionListener(e -> handleViewDetails());

        // Botão AJUSTAR LIMITES
        view.getAdjustLimitsBtn().addActionListener(e -> handleAdjustLimits());

        // Botão ENCERRAR CONTA
        view.getCloseAccountBtn().addActionListener(e -> handleCloseAccount());
    }

    private void handleBack() {
        System.out.println("Voltando...");
        if (parentDialog != null) {
            parentDialog.dispose();
        }
    }

    private void handleFilter() {
        try {
            String typeFilter = view.getTypeFilter();
            String statusFilter = view.getStatusFilter();

            System.out.println("Filtrando - Tipo: " + typeFilter + ", Status: " + statusFilter);

            // Buscar contas com filtros
            List<Account> filteredAccounts = accountService.findAccountsByFilters(typeFilter, statusFilter);

            if (filteredAccounts.isEmpty()) {
                JOptionPane.showMessageDialog(view,
                        "Nenhuma conta encontrada com os filtros selecionados.",
                        "Sem Resultados", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Atualizar tabela com os resultados filtrados
                view.setAccounts(filteredAccounts);

                // Atualizar estatísticas
                int activeAccounts = (int) filteredAccounts.stream()
                        .filter(acc -> acc.getStatus() == AccountStatus.ATIVA)
                        .count();
                double totalBalance = filteredAccounts.stream()
                        .mapToDouble(Account::getBalance)
                        .sum();

                view.setStats(filteredAccounts.size(), activeAccounts, totalBalance);

                JOptionPane.showMessageDialog(view,
                        "Filtro aplicado com sucesso!\nContas encontradas: " + filteredAccounts.size(),
                        "Filtro Aplicado", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Erro ao aplicar filtro: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void handleOpenNewAccount() {
        try {
            // PASSO 1: Carregar lista de clientes
            System.out.println("Carregando lista de clientes...");
            CustomerService customerService = new CustomerService();
            java.util.List<model.entities.Customer> customers = customerService.findAllCustomers();

            if (customers == null || customers.isEmpty()) {
                JOptionPane.showMessageDialog(view,
                        "Nenhum cliente encontrado no sistema!",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            System.out.println("✓ Total de clientes carregados: " + customers.size());

            // PASSO 2: Abrir diálogo de criação de conta
            view.componet.CreateAccountDialog createDialog = new view.componet.CreateAccountDialog(customers);

            // Listener para confirmar
            createDialog.addConfirmListener(e -> handleAccountCreationConfirm(createDialog));

            // Listener para cancelar
            createDialog.addCancelListener(e -> createDialog.dispose());

            createDialog.setVisible(true);

        } catch (Exception ex) {
            System.err.println("Erro ao abrir diálogo de criação: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view,
                    "Erro ao abrir diálogo: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAccountCreationConfirm(view.componet.CreateAccountDialog dialog) {
        try {
            System.out.println("\n=== INICIANDO CRIAÇÃO DE CONTA ===");

            // Obter dados do diálogo
            int customerId = dialog.getSelectedCustomerId();
            String accountType = dialog.getAccountType();
            String pin = dialog.getPin();
            String confirmPin = dialog.getConfirmPin();
            model.entities.Customer customer = dialog.getSelectedCustomer();

            System.out.println("Cliente ID: " + customerId);
            System.out.println("Cliente: " + customer.getFirstName() + " " + customer.getLastName());
            System.out.println("Tipo de Conta: " + accountType);
            System.out.println("PIN: " + pin);

            // VALIDAÇÕES
            if (customerId < 0) {
                dialog.showError("Por favor, selecione um cliente!");
                return;
            }

            if (pin.isEmpty() || confirmPin.isEmpty()) {
                dialog.showError("PIN não pode estar vazio!");
                return;
            }

            if (pin.length() != 4 || !pin.matches("\\d+")) {
                dialog.showError("PIN deve conter EXATAMENTE 4 dígitos numéricos!");
                return;
            }

            if (!pin.equals(confirmPin)) {
                dialog.showError("Os PINs não coincidem!");
                return;
            }

            // CRIAR A CONTA
            System.out.println("Criando conta para cliente ID: " + customerId);
            Account newAccount = accountService.createNewAccount(customerId, accountType, pin);

            // Validar resultado
            if (newAccount != null && newAccount.getAccountId() > 0) {
                System.out.println("✓ CONTA CRIADA COM SUCESSO!");
                System.out.println("  ID: " + newAccount.getAccountId());
                System.out.println("  Número: " + newAccount.getAccountNumber());
                System.out.println("  Tipo: " + newAccount.getAccountType());

                // Mostrar mensagem de sucesso
                JOptionPane.showMessageDialog(view,
                        "✓ CONTA CRIADA COM SUCESSO!\n\n" +
                                "Cliente: " + customer.getFirstName() + " " + customer.getLastName() + "\n" +
                                "ID da Conta: " + newAccount.getAccountId() + "\n" +
                                "Número da Conta: " + newAccount.getAccountNumber() + "\n" +
                                "Tipo: " + accountType + "\n" +
                                "Saldo Inicial: MZN 0,00\n" +
                                "Status: ATIVA",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                // Fechar diálogo
                dialog.dispose();

                // Recarregar tabela
                refreshAccountsList();

            } else {
                System.out.println("✗ ERRO: Conta retornou nula ou ID inválido");
                dialog.showError("Erro ao criar conta (retorno inválido do service)");
            }

        } catch (Exception ex) {
            System.err.println("✗ ERRO na criação: " + ex.getMessage());
            ex.printStackTrace();
            dialog.showError("Erro: " + ex.getMessage());
        }
    }

    private void handleViewDetails() {
        try {
            String accountNumber = view.getSelectedAccountNumber();

            if (accountNumber == null) {
                JOptionPane.showMessageDialog(view,
                        "Por favor, selecione uma conta na tabela!",
                        "Nenhuma Seleção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Buscar a conta no banco de dados
            Account account = accountService.findAccountByNumber(accountNumber);

            if (account == null) {
                JOptionPane.showMessageDialog(view,
                        "Conta não encontrada!",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Montar string com detalhes
            StringBuilder details = new StringBuilder();
            details.append("========== DETALHES DA CONTA ==========\n\n");
            details.append("ID da Conta: ").append(account.getAccountId()).append("\n");
            details.append("Número da Conta: ").append(account.getAccountNumber()).append("\n");
            details.append("Tipo: ").append(account.getAccountType()).append("\n");
            details.append("Cliente ID: ").append(account.getOwnerCustomerId()).append("\n");
            details.append("Agência ID: ").append(account.getAgencyId()).append("\n");
            details.append("\n--- SALDO E LIMITES ---\n");
            details.append("Saldo Atual: MZN ").append(String.format("%,.2f", account.getBalance())).append("\n");
            details.append("Limite Diário de Levantamento: MZN ").append(String.format("%,.2f", account.getDailyWithdrawLimit())).append("\n");
            details.append("Limite Diário de Transferência: MZN ").append(String.format("%,.2f", account.getDailyTransferLimit())).append("\n");
            details.append("\n--- STATUS ---\n");
            details.append("Status: ").append(account.getStatus()).append("\n");
            details.append("Data de Abertura: ").append(account.getOpenDate()).append("\n");
            if (account.getCloseDate() != null) {
                details.append("Data de Fechamento: ").append(account.getCloseDate()).append("\n");
            }

            JOptionPane.showMessageDialog(view,
                    details.toString(),
                    "Detalhes da Conta", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Erro ao obter detalhes: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void handleAdjustLimits() {
        try {
            String accountNumber = view.getSelectedAccountNumber();

            if (accountNumber == null) {
                JOptionPane.showMessageDialog(view,
                        "Por favor, selecione uma conta na tabela!",
                        "Nenhuma Seleção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Buscar a conta
            Account account = accountService.findAccountByNumber(accountNumber);
            if (account == null) {
                JOptionPane.showMessageDialog(view,
                        "Conta não encontrada!",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Dialog para ajustar limites
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            JLabel label1 = new JLabel("Novo Limite Diário de Levantamento (MZN):");
            JTextField field1 = new JTextField(String.valueOf(account.getDailyWithdrawLimit()), 10);

            JLabel label2 = new JLabel("Novo Limite Diário de Transferência (MZN):");
            JTextField field2 = new JTextField(String.valueOf(account.getDailyTransferLimit()), 10);

            panel.add(label1);
            panel.add(field1);
            panel.add(Box.createVerticalStrut(10));
            panel.add(label2);
            panel.add(field2);

            int result = JOptionPane.showConfirmDialog(view, panel,
                    "Ajustar Limites", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    double newWithdrawLimit = Double.parseDouble(field1.getText());
                    double newTransferLimit = Double.parseDouble(field2.getText());

                    // Validações
                    if (newWithdrawLimit <= 0 || newTransferLimit <= 0) {
                        JOptionPane.showMessageDialog(view,
                                "Os limites devem ser valores positivos!",
                                "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // ✅ ATUALIZAR NO BANCO DE DADOS (IMPORTANTE!)
                    boolean updated = accountService.updateAccountLimits(
                            account.getAccountId(),
                            newWithdrawLimit,
                            newTransferLimit
                    );

                    if (updated) {
                        JOptionPane.showMessageDialog(view,
                                "✓ Limites atualizados com sucesso!\n\n" +
                                        "Novo limite de levantamento: MZN " + String.format("%,.2f", newWithdrawLimit) + "\n" +
                                        "Novo limite de transferência: MZN " + String.format("%,.2f", newTransferLimit),
                                "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                        // Recarregar lista
                        refreshAccountsList();
                    } else {
                        JOptionPane.showMessageDialog(view,
                                "Erro ao salvar os limites no banco de dados!",
                                "Erro", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(view,
                            "Por favor, insira valores numéricos válidos!",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Erro ao ajustar limites: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void handleCloseAccount() {
        try {
            String accountNumber = view.getSelectedAccountNumber();

            if (accountNumber == null) {
                JOptionPane.showMessageDialog(view,
                        "Por favor, selecione uma conta na tabela!",
                        "Nenhuma Seleção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Buscar a conta
            Account account = accountService.findAccountByNumber(accountNumber);
            if (account == null) {
                JOptionPane.showMessageDialog(view,
                        "Conta não encontrada!",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar se tem saldo
            if (account.getBalance() > 0) {
                JOptionPane.showMessageDialog(view,
                        "⚠ A conta ainda possui saldo!\n\n" +
                                "Saldo atual: MZN " + String.format("%,.2f", account.getBalance()) + "\n" +
                                "Retire o saldo antes de encerrar a conta.",
                        "Saldo Pendente", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Confirmar encerramento
            int confirm = JOptionPane.showConfirmDialog(view,
                    "Tem certeza que deseja encerrar a conta " + accountNumber + "?\n" +
                            "Esta ação é IRREVERSÍVEL!",
                    "Confirmar Encerramento", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Marcar conta como fechada
                account.setStatus(AccountStatus.FECHADA);
                account.setCloseDate(new java.util.Date());

                JOptionPane.showMessageDialog(view,
                        "✓ Conta encerrada com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                // Recarregar lista
                refreshAccountsList();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Erro ao encerrar conta: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void refreshAccountsList() {
        try {
            List<Account> accounts = accountService.getAllAccounts();
            view.setAccounts(accounts);

            int activeAccounts = (int) accounts.stream()
                    .filter(acc -> acc.getStatus() == AccountStatus.ATIVA)
                    .count();
            double totalBalance = accounts.stream()
                    .mapToDouble(Account::getBalance)
                    .sum();

            view.setStats(accounts.size(), activeAccounts, totalBalance);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadAccounts() {
        try {
            List<Account> accounts = accountService.getAllAccounts();
            view.setAccounts(accounts);

            if (!accounts.isEmpty()) {
                int activeAccounts = (int) accounts.stream()
                        .filter(acc -> acc.getStatus() == AccountStatus.ATIVA)
                        .count();
                double totalBalance = accounts.stream()
                        .mapToDouble(Account::getBalance)
                        .sum();

                view.setStats(accounts.size(), activeAccounts, totalBalance);
            } else {
                view.setStats(0, 0, 0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}