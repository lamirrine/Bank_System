package controller;

import model.entities.Account;
import model.entities.Transaction;
import model.services.StatementService;
import view.StatementView;
import utils.PdfGenerator;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StatementController {
    private StatementView view;
    private StatementService statementService;
    private List<Account> customerAccounts;
    private int currentCustomerId;
    private List<Transaction> currentTransactions;
    private ExecutorService executorService;

    public StatementController(StatementView view, StatementService statementService,
                               List<Account> customerAccounts, int currentCustomerId) {
        this.view = view;
        this.statementService = statementService;
        this.customerAccounts = customerAccounts;
        this.currentCustomerId = currentCustomerId;
        this.executorService = Executors.newSingleThreadExecutor();

        initializeController();
    }

    private void initializeController() {
        try {
            setupEventListeners();
            setupUI();
            loadInitialData();
        } catch (Exception e) {
            System.err.println("Erro ao inicializar StatementController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupEventListeners() {
        try {
            // Botão Voltar
            if (view.getCancelButton() != null) {
                view.getCancelButton().addActionListener(e -> {
                    clearForm();
                });
            }

            // Botão Buscar
            if (view.getSearchButton() != null) {
                view.getSearchButton().addActionListener(e -> {
                    searchTransactions();
                });
            }

            // Botão Gerar PDF
            if (view.getGeneratePdfButton() != null) {
                view.getGeneratePdfButton().addActionListener(e -> {
                    generatePdf();
                });
            }

            // Listener para mudança de período
            if (view.getPeriodComboBox() != null) {
                view.getPeriodComboBox().addActionListener(e -> {
                    handlePeriodChange();
                });
            }

        } catch (Exception e) {
            System.err.println("Erro ao configurar event listeners: " + e.getMessage());
        }
    }

    private void setupUI() {
        try {
            // Configurar datas padrão
            view.getStartDateChooser().setDate(new Date());
            view.getEndDateChooser().setDate(new Date());

            // Configurar combobox de contas
            if (customerAccounts != null && !customerAccounts.isEmpty()) {
                String[] accountDisplay = new String[customerAccounts.size()];
                for (int i = 0; i < customerAccounts.size(); i++) {
                    Account account = customerAccounts.get(i);
                    String accountType = account.getAccountType() != null ?
                            account.getAccountType().toString() : "CORRENTE";
                    String typeDisplay = accountType.equals("CORRENTE") ? "Conta Corrente" : "Conta Poupança";
                    accountDisplay[i] = String.format("%s - %s", typeDisplay, account.getAccountNumber());
                }
                view.setAccounts(accountDisplay);
            }
        } catch (Exception e) {
            System.err.println("Erro ao configurar UI: " + e.getMessage());
        }
    }

    private void loadInitialData() {
        // Carregar dados iniciais (últimos 30 dias)
        searchTransactions();
    }

    private void handlePeriodChange() {
        String selectedPeriod = (String) view.getPeriodComboBox().getSelectedItem();
        Date startDate = null;
        Date endDate = new Date();

        switch (selectedPeriod) {
            case "Últimos 7 dias":
                startDate = new Date(System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000);
                break;
            case "Últimos 30 dias":
                startDate = new Date(System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000);
                break;
            case "Este mês":
                startDate = getFirstDayOfMonth(new Date());
                break;
            case "Mês anterior":
                Date lastMonth = new Date(System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000);
                startDate = getFirstDayOfMonth(lastMonth);
                endDate = getLastDayOfMonth(lastMonth);
                break;
            case "Personalizado":
                // Mantém as datas atuais para edição manual
                return;
        }

        if (startDate != null) {
            view.getStartDateChooser().setDate(startDate);
            view.getEndDateChooser().setDate(endDate);
        }
    }

    private Date getFirstDayOfMonth(Date date) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    private Date getLastDayOfMonth(Date date) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        cal.set(java.util.Calendar.DAY_OF_MONTH, cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    private void searchTransactions() {
        try {
            Date startDate = view.getStartDateChooser().getDate();
            Date endDate = view.getEndDateChooser().getDate();

            if (startDate == null || endDate == null) {
                JOptionPane.showMessageDialog(view,
                        "Por favor, selecione as datas de início e fim.",
                        "Datas Obrigatórias", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (startDate.after(endDate)) {
                JOptionPane.showMessageDialog(view,
                        "A data de início não pode ser posterior à data de fim.",
                        "Datas Inválidas", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Obter conta selecionada
            Account selectedAccount = getSelectedAccount();

            List<Transaction> transactions;

            if (selectedAccount != null) {
                // Buscar transações da conta específica usando método otimizado
                transactions = statementService.getTransactionsByAccountAndPeriod(
                        selectedAccount.getAccountId(), startDate, endDate);
            } else {
                // Buscar transações de todas as contas do cliente usando método otimizado
                transactions = statementService.getTransactionsByCustomerAndPeriod(
                        currentCustomerId, startDate, endDate);
            }

            currentTransactions = transactions;
            updateTransactionsTable(transactions);
            updateTotal(transactions);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Erro ao buscar transações: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Account getSelectedAccount() {
        int selectedIndex = view.getAccountComboBox().getSelectedIndex();
        if (selectedIndex > 0 && customerAccounts != null && (selectedIndex - 1) < customerAccounts.size()) {
            return customerAccounts.get(selectedIndex - 1); // -1 porque o primeiro item é "Todas as contas"
        }
        return null;
    }

    private void updateTransactionsTable(List<Transaction> transactions) {
        String[] columns = {"Data", "Descrição", "Tipo", "Valor", "Saldo"};
        Object[][] data = new Object[transactions.size()][5];

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (int i = 0; i < transactions.size(); i++) {
            Transaction transaction = transactions.get(i);
            data[i][0] = dateFormat.format(transaction.getTimestamp());
            data[i][1] = transaction.getDescription();
            data[i][2] = transaction.getType().toString();

            // Formatar valor com cor baseada no tipo
            String amountText = String.format("MZN %,.2f", Math.abs(transaction.getAmount()));
            if (transaction.getAmount() >= 0) {
                data[i][3] = "<html><font color='#10b981'>+" + amountText + "</font></html>";
            } else {
                data[i][3] = "<html><font color='#ef4444'>-" + amountText + "</font></html>";
            }

            data[i][4] = String.format("MZN %,.2f", transaction.getResultingBalance());
        }

        view.setTableData(data, columns);
    }

    private void updateTotal(List<Transaction> transactions) {
        double total = 0.0;
        if (!transactions.isEmpty()) {
            // Usar o saldo da última transação como saldo atual
            total = transactions.get(transactions.size() - 1).getResultingBalance();
        }
        view.getTotalLabel().setText(String.format("MZN %,.2f", total));
    }

    private void generatePdf() {
        if (currentTransactions == null || currentTransactions.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Não há transações para gerar o PDF.",
                    "Nenhuma Transação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Usar thread separada para geração do PDF
        view.showProgress(true);
        view.getGeneratePdfButton().setEnabled(false);

        executorService.execute(() -> {
            try {
                view.updateProgress(10, "Preparando dados...");

                // Obter informações para o PDF
                Account selectedAccount = getSelectedAccount();
                Date startDate = view.getStartDateChooser().getDate();
                Date endDate = view.getEndDateChooser().getDate();

                String accountInfo = selectedAccount != null ?
                        selectedAccount.getAccountNumber() : "Todas as Contas";

                view.updateProgress(30, "Gerando PDF...");

                // Gerar PDF
                String filePath = PdfGenerator.generateStatement(
                        currentTransactions,
                        accountInfo,
                        startDate,
                        endDate,
                        currentCustomerId
                );

                view.updateProgress(90, "Finalizando...");

                // Atualizar UI na thread EDT
                SwingUtilities.invokeLater(() -> {
                    view.showProgress(false);
                    view.getGeneratePdfButton().setEnabled(true);

                    int option = JOptionPane.showConfirmDialog(view,
                            "PDF gerado com sucesso!\nDeseja abrir o arquivo?",
                            "PDF Gerado",
                            JOptionPane.YES_NO_OPTION);

                    if (option == JOptionPane.YES_OPTION) {
                        openPdfFile(filePath);
                    }
                });

                view.updateProgress(100, "Concluído!");

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    view.showProgress(false);
                    view.getGeneratePdfButton().setEnabled(true);
                    JOptionPane.showMessageDialog(view,
                            "Erro ao gerar PDF: " + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                });
            }
        });
    }

    private void openPdfFile(String filePath) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new java.io.File(filePath));
            } else {
                JOptionPane.showMessageDialog(view,
                        "PDF salvo em: " + filePath,
                        "Local do Arquivo",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "PDF salvo em: " + filePath + "\nNão foi possível abrir automaticamente.",
                    "Local do Arquivo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void clearForm() {
        // Limpa seleções mas mantém dados
        view.getPeriodComboBox().setSelectedIndex(0);
        view.getStartDateChooser().setDate(new Date());
        view.getEndDateChooser().setDate(new Date());
    }

    public void updateCustomerAccounts(List<Account> accounts) {
        this.customerAccounts = accounts;
        setupUI();
    }

    // Getters
    public StatementView getView() {
        return view;
    }

    public void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}