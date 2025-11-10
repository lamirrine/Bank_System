package controller;

import model.entities.Account;
import model.services.AccountService;
import view.DepositView;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.List;
import java.awt.*;

public class DepositController {
    private DepositView view;
    private AccountService accountService;
    private List<Account> customerAccounts;
    private int currentCustomerId;

    public DepositController(DepositView view, AccountService accountService,
                             List<Account> customerAccounts, int currentCustomerId) {
        this.view = view;
        this.accountService = accountService;
        this.customerAccounts = customerAccounts;
        this.currentCustomerId = currentCustomerId;

        initializeController();
    }

    private void initializeController() {
        try {
            setupAccountComboBox();
            setupEventListeners();
            setupValidation();
            updateContinueButtonState(); // Atualizar estado inicial do botão
        } catch (Exception e) {
            System.err.println("Erro ao inicializar DepositController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupAccountComboBox() {
        try {
            // Limpar combobox existente
            if (view.getAccountComboBox() != null) {
                view.getAccountComboBox().removeAllItems();
            }

            // Adicionar contas do cliente
            if (customerAccounts != null && !customerAccounts.isEmpty()) {
                for (Account account : customerAccounts) {
                    String accountType = account.getAccountType() != null ?
                            account.getAccountType().toString() : "CORRENTE";
                    String typeDisplay = accountType.equals("CORRENTE") ? "Conta Corrente" : "Conta Poupança";
                    String displayText = String.format("%s - %s (MZN %,.2f)",
                            typeDisplay, account.getAccountNumber(), account.getBalance());

                    if (view.getAccountComboBox() != null) {
                        view.getAccountComboBox().addItem(displayText);
                    }
                }
                // Habilita o combobox e botão continuar
                if (view.getAccountComboBox() != null) {
                    view.getAccountComboBox().setEnabled(true);
                }
                if (view.getContinueButton() != null) {
                    view.getContinueButton().setEnabled(true);
                }
            } else {
                if (view.getAccountComboBox() != null) {
                    view.getAccountComboBox().addItem("Nenhuma conta disponível");
                    view.getAccountComboBox().setEnabled(false);
                }
                if (view.getContinueButton() != null) {
                    view.getContinueButton().setEnabled(false);
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao configurar combobox de contas: " + e.getMessage());
        }
    }

    private void setupEventListeners() {
        try {
            // Botão Cancelar
            if (view.getCancelButton() != null) {
                view.getCancelButton().addActionListener(e -> {
                    clearForm();
                    // A navegação será tratada pelo DashboardController
                });
            }

            // Botão Continuar
            if (view.getContinueButton() != null) {
                view.getContinueButton().addActionListener(e -> {
                    processDeposit();
                });
            }

            // Listener para mudança de seleção no combobox
            if (view.getAccountComboBox() != null) {
                view.getAccountComboBox().addActionListener(e -> {
                    updateContinueButtonState();
                });
            }

            // Campos de valor rápido já estão configurados na view
        } catch (Exception e) {
            System.err.println("Erro ao configurar event listeners: " + e.getMessage());
        }
    }

    private void setupValidation() {
        try {
            // Validação em tempo real do campo de valor
            view.addAmountFieldListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    validateAmountField();
                    updateContinueButtonState();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    validateAmountField();
                    updateContinueButtonState();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    validateAmountField();
                    updateContinueButtonState();
                }
            });
        } catch (Exception e) {
            System.err.println("Erro ao configurar validação: " + e.getMessage());
        }
    }

    private void updateContinueButtonState() {
        try {
            if (view.getAmountField() == null || view.getContinueButton() == null) {
                return;
            }

            String amountText = view.getAmountField().getText().trim();
            boolean hasValidAmount = !amountText.isEmpty() && isValidAmount(amountText);
            boolean hasValidAccount = view.getAccountComboBox() != null &&
                    view.getAccountComboBox().isEnabled() &&
                    view.getAccountComboBox().getSelectedIndex() >= 0;

            view.getContinueButton().setEnabled(hasValidAmount && hasValidAccount);

            // Atualizar aparência do botão baseado no estado
            if (view.getContinueButton().isEnabled()) {
                view.getContinueButton().setBackground(new Color(16, 185, 129));
                view.getContinueButton().setCursor(new Cursor(Cursor.HAND_CURSOR));
            } else {
                view.getContinueButton().setBackground(new Color(148, 163, 184));
                view.getContinueButton().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        } catch (Exception e) {
            System.err.println("Erro ao atualizar estado do botão: " + e.getMessage());
        }
    }

    private boolean isValidAmount(String amountText) {
        try {
            double amount = Double.parseDouble(amountText);
            return amount >= 100.00 && amount <= 50000.00;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void setupQuickAmountButtons() {
        // Os botões de valor rápido já estão configurados na view
        // A lógica está no createAmountButton da view
    }

    private void validateAmountField() {
        String amountText = view.getAmountField().getText().trim();

        if (amountText.isEmpty()) {
            view.clearFieldError();
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);

            if (amount < 100.00) {
                view.showFieldError("Valor mínimo: MZN 100,00");
            } else if (amount > 50000.00) {
                view.showFieldError("Valor máximo: MZN 50.000,00");
            } else {
                view.clearFieldError();
            }
        } catch (NumberFormatException e) {
            view.showFieldError("Valor numérico inválido");
        }
    }

    public boolean processDeposit() {
        try {
            // 1. Validações básicas
            if (!validateForm()) {
                return false;
            }

            // 2. Obter conta selecionada
            Account selectedAccount = getSelectedAccount();
            if (selectedAccount == null) {
                JOptionPane.showMessageDialog(view,
                        "Por favor, selecione uma conta válida.",
                        "Conta Inválida", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            // 3. Obter valor
            double amount = getAmountFromField();
            if (amount <= 0) {
                return false;
            }

            // 4. Validar limites
            if (!validateAmountLimits(amount)) {
                return false;
            }

            // 5. Confirmar com usuário
            if (!confirmDeposit(selectedAccount, amount)) {
                return false;
            }

            // 6. Processar depósito
            return executeDeposit(selectedAccount, amount);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Erro ao processar depósito: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean validateForm() {
        // Validar valor
        String amountText = view.getAmountField().getText().trim();
        if (amountText.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Por favor, insira um valor para depósito.",
                    "Valor Obrigatório", JOptionPane.WARNING_MESSAGE);
            view.getAmountField().requestFocus();
            return false;
        }

        // Validar formato numérico
        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(view,
                        "O valor do depósito deve ser positivo.",
                        "Valor Inválido", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view,
                    "Por favor, insira um valor numérico válido.",
                    "Valor Inválido", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean validateAmountLimits(double amount) {
        if (amount < 100.00) {
            JOptionPane.showMessageDialog(view,
                    "O valor mínimo para depósito é MZN 100,00.",
                    "Valor Insuficiente", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (amount > 50000.00) {
            JOptionPane.showMessageDialog(view,
                    "O valor máximo por depósito é MZN 50.000,00.",
                    "Valor Excedido", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private Account getSelectedAccount() {
        int selectedIndex = view.getAccountComboBox().getSelectedIndex();
        if (selectedIndex >= 0 && customerAccounts != null &&
                selectedIndex < customerAccounts.size()) {
            return customerAccounts.get(selectedIndex);
        }
        return null;
    }

    private double getAmountFromField() {
        try {
            String amountText = view.getAmountField().getText().trim();
            return Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view,
                    "Valor numérico inválido.",
                    "Erro de Formatação", JOptionPane.ERROR_MESSAGE);
            return 0;
        }
    }

    private boolean confirmDeposit(Account account, double amount) {
        String message = String.format(
                "Confirmar depósito de MZN %,.2f na conta:\n%s - %s?",
                amount,
                getAccountTypeDisplay(account.getAccountType()),
                account.getAccountNumber()
        );

        int result = JOptionPane.showConfirmDialog(
                view,
                message,
                "Confirmar Depósito",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        return result == JOptionPane.YES_OPTION;
    }

    private boolean executeDeposit(Account account, double amount) {
        try {
            // Processar depósito via AccountService
            boolean success = accountService.deposit(account.getAccountId(), amount);

            if (success) {
                showSuccessMessage(account, amount);
                clearForm();
                return true;
            } else {
                JOptionPane.showMessageDialog(view,
                        "Falha ao processar depósito. Tente novamente.",
                        "Erro no Processamento", JOptionPane.ERROR_MESSAGE);
                return false;
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Erro durante o depósito: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void showSuccessMessage(Account account, double amount) {
        String message = String.format(
                "Depósito de MZN %,.2f realizado com sucesso!\n\n" +
                        "Conta: %s\n" +
                        "Número: %s\n" +
                        "Disponível: Imediatamente",
                amount,
                getAccountTypeDisplay(account.getAccountType()),
                account.getAccountNumber()
        );

        JOptionPane.showMessageDialog(view,
                message,
                "Depósito Concluído",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private String getAccountTypeDisplay(model.enums.AccountType accountType) {
        if (accountType == null) return "Conta Corrente";
        return accountType.toString().equals("CORRENTE") ? "Conta Corrente" : "Conta Poupança";
    }

    public void clearForm() {
        view.setAmountField("");
        view.clearFieldError();

        // Manter a seleção atual da conta
        if (view.getAccountComboBox().getItemCount() > 0) {
            view.getAccountComboBox().setSelectedIndex(0);
        }

        // Atualizar estado do botão após limpar
        updateContinueButtonState();
    }

    public void updateCustomerAccounts(List<Account> accounts) {
        this.customerAccounts = accounts;
        setupAccountComboBox();
        updateContinueButtonState(); // Atualizar estado do botão após atualizar contas
    }

    // Getters para acesso externo
    public DepositView getView() {
        return view;
    }

    public boolean isFormValid() {
        return validateForm();
    }

    public double getEnteredAmount() {
        return getAmountFromField();
    }

    public Account getSelectedAccountForDeposit() {
        return getSelectedAccount();
    }
}