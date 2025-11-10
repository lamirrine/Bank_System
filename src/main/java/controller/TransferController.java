package controller;

import model.entities.Account;
import model.services.AccountService;
import view.TransferView;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.List;
import java.awt.*;

public class TransferController {
    private TransferView view;
    private AccountService accountService;
    private List<Account> customerAccounts;
    private int currentCustomerId;
    private Account selectedAccount;

    public TransferController(TransferView view, AccountService accountService,
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
            System.err.println("Erro ao inicializar TransferController: " + e.getMessage());
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
                    processTransfer();
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

            // Validação em tempo real do campo de conta destino
            if (view.getDestinationAccountField() != null) {
                view.getDestinationAccountField().getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        updateContinueButtonState();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        updateContinueButtonState();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        updateContinueButtonState();
                    }
                });
            }
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

                // Selecionar primeira conta por padrão
                selectedAccount = customerAccounts.get(0);
                double balance = selectedAccount.getBalance();
                double transferLimit = selectedAccount.getDailyTransferLimit();

                view.setBalance(balance);
                view.setLimits(transferLimit, 0.0, transferLimit); // TODO: Calcular usado hoje
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
                view.setLimits(selectedAccount.getDailyTransferLimit(), 0.0, selectedAccount.getDailyTransferLimit());
            }
        } catch (Exception e) {
            System.err.println("Erro ao atualizar conta selecionada: " + e.getMessage());
        }
    }

    private void updateContinueButtonState() {
        try {
            if (view.getAmountField() == null || view.getConfirmButton() == null ||
                    view.getPinField() == null || view.getDestinationAccountField() == null) {
                return;
            }

            String amountText = view.getAmountField().getText().trim();
            String destinationAccount = view.getDestinationAccountField().getText().trim();
            String pin = new String(view.getPinField().getPassword());

            boolean hasValidAmount = !amountText.isEmpty() && isValidAmount(amountText);
            boolean hasValidDestination = !destinationAccount.isEmpty();
            boolean hasValidPin = pin.length() == 4;
            boolean hasValidSource = selectedAccount != null;

            view.getConfirmButton().setEnabled(hasValidAmount && hasValidDestination && hasValidPin && hasValidSource);

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
            return amount >= 100.00 && amount <= 10000.00; // Mínimo 100, máximo 10.000
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
            } else if (amount > 10000.00) {
                view.showFieldError("Valor máximo: MZN 10.000,00");
            } else if (selectedAccount != null && amount > selectedAccount.getBalance()) {
                view.showFieldError("Saldo insuficiente");
            } else if (selectedAccount != null && amount > selectedAccount.getDailyTransferLimit()) {
                view.showFieldError("Valor excede limite de transferência");
            } else {
                view.clearFieldError();
            }
        } catch (NumberFormatException e) {
            view.showFieldError("Valor numérico inválido");
        }
    }

    public boolean processTransfer() {
        try {
            // 1. Validações básicas
            if (!validateForm()) {
                return false;
            }

            // 2. Obter dados do formulário
            double amount = getAmountFromField();
            String destinationAccount = view.getDestinationAccountField().getText().trim();
            String pin = new String(view.getPinField().getPassword());

            if (amount <= 0) {
                return false;
            }

            // 3. Validar limites
            if (!validateAmountLimits(amount)) {
                return false;
            }

            // 4. Confirmar com usuário
            if (!confirmTransfer(amount, destinationAccount)) {
                return false;
            }

            // 5. Processar transferência
            return executeTransfer(amount, destinationAccount, pin);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Erro ao processar transferência: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean validateForm() {
        // Validar conta origem
        if (selectedAccount == null) {
            JOptionPane.showMessageDialog(view,
                    "Por favor, selecione uma conta de origem.",
                    "Conta Origem Obrigatória", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Validar conta destino
        String destinationAccount = view.getDestinationAccountField().getText().trim();
        if (destinationAccount.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Por favor, insira o número da conta destino.",
                    "Conta Destino Obrigatória", JOptionPane.WARNING_MESSAGE);
            view.getDestinationAccountField().requestFocus();
            return false;
        }

        // Validar valor
        String amountText = view.getAmountField().getText().trim();
        if (amountText.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Por favor, insira um valor para transferência.",
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
                        "O valor da transferência deve ser positivo.",
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
                    "O valor mínimo para transferência é MZN 100,00.",
                    "Valor Insuficiente", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (amount > 10000.00) {
            JOptionPane.showMessageDialog(view,
                    "O valor máximo por transferência é MZN 10.000,00.",
                    "Valor Excedido", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (selectedAccount != null && amount > selectedAccount.getBalance()) {
            JOptionPane.showMessageDialog(view,
                    "Saldo insuficiente para realizar a transferência.",
                    "Saldo Insuficiente", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (selectedAccount != null && amount > selectedAccount.getDailyTransferLimit()) {
            JOptionPane.showMessageDialog(view,
                    "Valor excede o limite diário de transferência.",
                    "Limite Excedido", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean confirmTransfer(double amount, String destinationAccount) {
        String message = String.format(
                "Confirmar transferência de MZN %,.2f\n" +
                        "De: %s\n" +
                        "Para: %s?",
                amount,
                selectedAccount.getAccountNumber(),
                destinationAccount
        );

        int result = JOptionPane.showConfirmDialog(
                view,
                message,
                "Confirmar Transferência",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        return result == JOptionPane.YES_OPTION;
    }

    private boolean executeTransfer(double amount, String destinationAccount, String pin) {
        try {
            if (selectedAccount == null) {
                throw new Exception("Nenhuma conta selecionada.");
            }

            // Verificar se o PIN tem formato válido
            if (pin.length() != 4 || !pin.matches("\\d{4}")) {
                throw new Exception("PIN deve conter exatamente 4 dígitos numéricos.");
            }

            // Processar transferência via AccountService
            boolean success = accountService.transfer(
                    selectedAccount.getAccountId(),
                    destinationAccount,
                    amount,
                    pin
            );

            if (success) {
                showSuccessMessage(amount, destinationAccount);
                clearForm();
                updateUI(); // Atualizar saldo e limites
                return true;
            } else {
                throw new Exception("Não foi possível processar a transferência.");
            }

        } catch (Exception ex) {
            String errorMessage = ex.getMessage();

            // Mensagens mais amigáveis para o usuário
            if (errorMessage.contains("PIN de transação inválido")) {
                errorMessage = "PIN incorreto. Tente novamente.";
            } else if (errorMessage.contains("PIN não configurado")) {
                errorMessage = "PIN de transação não configurado. Contacte o banco.";
            } else if (errorMessage.contains("Saldo insuficiente")) {
                errorMessage = "Saldo insuficiente para realizar a transferência.";
            } else if (errorMessage.contains("limite diário") || errorMessage.contains("excede o limite")) {
                errorMessage = "Valor excede o limite diário de transferência.";
            } else if (errorMessage.contains("Conta não encontrada")) {
                errorMessage = "Conta destino não encontrada.";
            } else if (errorMessage.contains("taxa")) {
                errorMessage = "Saldo insuficiente para cobrir valor + taxa.";
            }

            JOptionPane.showMessageDialog(view,
                    errorMessage,
                    "Erro na Transferência", JOptionPane.ERROR_MESSAGE);
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

    private void showSuccessMessage(double amount, String destinationAccount) {
        String message = String.format(
                "Transferência de MZN %,.2f realizada com sucesso!\n\n" +
                        "De: %s\n" +
                        "Para: %s\n" +
                        "Disponível: Imediatamente",
                amount,
                selectedAccount.getAccountNumber(),
                destinationAccount
        );

        JOptionPane.showMessageDialog(view,
                message,
                "Transferência Concluída",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void clearForm() {
        view.setAmountField("");
        if (view.getDestinationAccountField() != null) {
            view.getDestinationAccountField().setText("");
        }
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
    public TransferView getView() {
        return view;
    }
}