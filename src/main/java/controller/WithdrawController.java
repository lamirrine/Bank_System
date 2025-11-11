package controller;

import model.entities.Account;
import model.services.AccountService;
import view.WithdrawView;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.List;
import java.awt.*;

public class WithdrawController {
    private WithdrawView view;
    private AccountService accountService;
    private List<Account> customerAccounts;
    private int currentCustomerId;
    private Account selectedAccount;

    public WithdrawController(WithdrawView view, AccountService accountService,
                              List<Account> customerAccounts, int currentCustomerId) {
        this.view = view;
        this.accountService = accountService;
        this.customerAccounts = customerAccounts;
        this.currentCustomerId = currentCustomerId;

        initializeController();
    }

    private void initializeController() {
        try {
            setupEventListeners();
            setupValidation();
            updateUI();
            updateContinueButtonState();
        } catch (Exception e) {
            System.err.println("Erro ao inicializar WithdrawController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupEventListeners() {
        try {
            // Botão Cancelar
            if (view.getCancelButton() != null) {
                view.getCancelButton().addActionListener(e -> {
                    clearForm();
                });
            }

            // Botão Confirmar
            if (view.getConfirmButton() != null) {
                view.getConfirmButton().addActionListener(e -> {
                    processWithdrawal();
                });
            }

            // Listener para mudança de conta origem
            if (view.getSourceAccountComboBox() != null) {
                view.getSourceAccountComboBox().addActionListener(e -> {
                    updateSelectedAccount();
                    updateContinueButtonState();
                });
            }
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

    private void updateUI() {
        try {
            if (customerAccounts != null && !customerAccounts.isEmpty()) {
                // Configurar combobox de contas origem
                String[] accountDisplay = new String[customerAccounts.size()];
                for (int i = 0; i < customerAccounts.size(); i++) {
                    Account account = customerAccounts.get(i);
                    String accountType = account.getAccountType() != null ?
                            account.getAccountType().toString() : "CORRENTE";
                    String typeDisplay = accountType.equals("CORRENTE") ? "Conta Corrente" : "Conta Poupança";
                    accountDisplay[i] = String.format("%s - %s (MZN %,.2f)",
                            typeDisplay, account.getAccountNumber(), account.getBalance());
                }
                view.setSourceAccounts(accountDisplay);

                // Usar a primeira conta como padrão
                selectedAccount = customerAccounts.get(0);
                double balance = selectedAccount.getBalance();
                double dailyLimit = selectedAccount.getDailyWithdrawLimit();

                view.setBalance(balance);
                view.setLimits(dailyLimit, 0.0, dailyLimit); // TODO: Calcular usado hoje
            }
        } catch (Exception e) {
            System.err.println("Erro ao atualizar UI: " + e.getMessage());
        }


    }

    private void updateSelectedAccount() {
        try {
            int selectedIndex = view.getSourceAccountComboBox().getSelectedIndex();
            if (selectedIndex >= 0 && customerAccounts != null && selectedIndex < customerAccounts.size()) {
                selectedAccount = customerAccounts.get(selectedIndex);
                view.setBalance(selectedAccount.getBalance());
                view.setLimits(selectedAccount.getDailyWithdrawLimit(), 0.0, selectedAccount.getDailyWithdrawLimit());
            }
        } catch (Exception e) {
            System.err.println("Erro ao atualizar conta selecionada: " + e.getMessage());
        }
    }

    private void updateContinueButtonState() {
        try {
            if (view.getAmountField() == null || view.getConfirmButton() == null ||
                    view.getPinField() == null) {
                return;
            }

            String amountText = view.getAmountField().getText().trim();
            String pin = new String(view.getPinField().getPassword());

            boolean hasValidAmount = !amountText.isEmpty() && isValidAmount(amountText);
            boolean hasValidPin = pin.length() == 4;

            view.getConfirmButton().setEnabled(hasValidAmount && hasValidPin);

            // Atualizar aparência do botão
            if (view.getConfirmButton().isEnabled()) {
                view.getConfirmButton().setBackground(new Color(16, 185, 129));
                view.getConfirmButton().setCursor(new Cursor(Cursor.HAND_CURSOR));
            } else {
                view.getConfirmButton().setBackground(new Color(148, 163, 184));
                view.getConfirmButton().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        } catch (Exception e) {
            System.err.println("Erro ao atualizar estado do botão: " + e.getMessage());
        }
    }

    private boolean isValidAmount(String amountText) {
        try {
            double amount = Double.parseDouble(amountText);
            return amount >= 100.00 && amount <= 5000.00; // Mínimo 100, máximo 5000
        } catch (NumberFormatException e) {
            return false;
        }
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
            } else if (amount > 5000.00) {
                view.showFieldError("Valor máximo: MZN 5.000,00");
            } else if (selectedAccount != null && amount > selectedAccount.getBalance()) {
                view.showFieldError("Saldo insuficiente");
            } else {
                view.clearFieldError();
            }
        } catch (NumberFormatException e) {
            view.showFieldError("Valor numérico inválido");
        }
    }

    public boolean processWithdrawal() {
        try {
            // 1. Validações básicas
            if (!validateForm()) {
                return false;
            }

            // 2. Obter valor e PIN
            double amount = getAmountFromField();
            String pin = new String(view.getPinField().getPassword());

            if (amount <= 0) {
                return false;
            }

            // 3. Validar limites
            if (!validateAmountLimits(amount)) {
                return false;
            }

            // 4. Confirmar com usuário
            if (!confirmWithdrawal(amount)) {
                return false;
            }

            // 5. Processar levantamento
            return executeWithdrawal(amount, pin);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Erro ao processar levantamento: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean validateForm() {
        // Validar valor
        String amountText = view.getAmountField().getText().trim();
        if (amountText.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Por favor, insira um valor para levantamento.",
                    "Valor Obrigatório", JOptionPane.WARNING_MESSAGE);
            view.getAmountField().requestFocus();
            return false;
        }

        // Validar PIN
        String pin = new String(view.getPinField().getPassword());
        if (pin.length() != 4) {
            JOptionPane.showMessageDialog(view,
                    "Por favor, insira um PIN de 4 dígitos.",
                    "PIN Inválido", JOptionPane.WARNING_MESSAGE);
            view.getPinField().requestFocus();
            return false;
        }

        // Validar formato numérico
        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(view,
                        "O valor do levantamento deve ser positivo.",
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
                    "O valor mínimo para levantamento é MZN 100,00.",
                    "Valor Insuficiente", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (amount > 5000.00) {
            JOptionPane.showMessageDialog(view,
                    "O valor máximo por levantamento é MZN 5.000,00.",
                    "Valor Excedido", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (selectedAccount != null && amount > selectedAccount.getBalance()) {
            JOptionPane.showMessageDialog(view,
                    "Saldo insuficiente para realizar o levantamento.",
                    "Saldo Insuficiente", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean confirmWithdrawal(double amount) {
        String message = String.format(
                "Confirmar levantamento de MZN %,.2f?",
                amount
        );

        int result = JOptionPane.showConfirmDialog(
                view,
                message,
                "Confirmar Levantamento",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        return result == JOptionPane.YES_OPTION;
    }

    private boolean executeWithdrawal(double amount, String pin) {
        try {
            if (selectedAccount == null) {
                throw new Exception("Nenhuma conta selecionada.");
            }

            // Processar levantamento via AccountService
            boolean success = accountService.withdraw(selectedAccount.getAccountId(), amount, pin);

            if (success) {
                showSuccessMessage(amount);
                clearForm();
                updateUI(); // Atualizar saldo e limites
                return true;
            } else {
                JOptionPane.showMessageDialog(view,
                        "Falha ao processar levantamento. Tente novamente.",
                        "Erro no Processamento", JOptionPane.ERROR_MESSAGE);
                return false;
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Erro durante o levantamento: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
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

    private void showSuccessMessage(double amount) {
        String message = String.format(
                "Levantamento de MZN %,.2f realizado com sucesso!\n\n" +
                        "Por favor, retire o dinheiro no terminal.",
                amount
        );

        JOptionPane.showMessageDialog(view,
                message,
                "Levantamento Concluído",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void clearForm() {
        view.setAmountField("");
        if (view.getPinField() != null) {
            view.getPinField().setText("");
        }
        view.clearFieldError();
        updateContinueButtonState();
    }

    public void updateCustomerAccounts(List<Account> accounts) {
        this.customerAccounts = accounts;
        updateUI();
    }

    // Getters
    public WithdrawView getView() {
        return view;
    }
}