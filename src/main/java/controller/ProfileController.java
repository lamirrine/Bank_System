package controller;

import model.entities.Account;
import model.entities.Customer;
import model.services.AccountService;
import model.services.CustomerService;
import model.services.PasswordChangeService;
import view.componet.ChangeAccountPinDialog;
import view.componet.ChangeUserPasswordDialog;
import view.ProfileView;
import view.componet.CreateAccountPinDialog;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class ProfileController {
    private ProfileView view;
    private CustomerService customerService;
    private AccountService accountService;
    private List<Account> customerAccounts;
    private int currentCustomerId;
    private Customer currentCustomer;

    public ProfileController(ProfileView view, CustomerService customerService,
                             AccountService accountService, List<Account> customerAccounts,
                             int currentCustomerId) {
        this.view = view;
        this.customerService = customerService;
        this.accountService = accountService;
        this.customerAccounts = customerAccounts;
        this.currentCustomerId = currentCustomerId;

        initializeController();
    }

    private void initializeController() {
        try {
            setupEventListeners();
            loadCustomerData();
            updateAccountsDisplay();
        } catch (Exception e) {
            System.err.println("Erro ao inicializar ProfileController: " + e.getMessage());
            e.printStackTrace();
            showError("Erro ao inicializar perfil: " + e.getMessage());
        }
    }

    private void setupEventListeners() {
        try {
            // Botão Abrir Conta Corrente
            if (view.getOpenCurrentAccountBtn() != null) {
                view.getOpenCurrentAccountBtn().addActionListener(e -> {
                    openNewAccount("CORRENTE");
                });
            }

            // Botão Abrir Conta Poupança
            if (view.getOpenSavingsAccountBtn() != null) {
                view.getOpenSavingsAccountBtn().addActionListener(e -> {
                    openNewAccount("POUPANCA");
                });
            }

            // Botão Alterar Senha
            if (view.getChangePasswordBtn() != null) {
                view.getChangePasswordBtn().addActionListener(e -> {
                    changePassword();
                });
            }

        } catch (Exception e) {
            System.err.println("Erro ao configurar event listeners: " + e.getMessage());
            showError("Erro de configuração: " + e.getMessage());
        }
    }

    private void openNewAccount(String accountType) {
        try {
            String typeDisplay = getAccountTypeDisplay(accountType);

            // Verificar se o cliente já tem muitas contas (opcional)
            if (customerAccounts != null && customerAccounts.size() >= 5) {
                int confirm = JOptionPane.showConfirmDialog(view,
                        "Você já possui " + customerAccounts.size() + " contas. Deseja continuar?",
                        "Múltiplas Contas",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            // Mostrar diálogo para coletar PIN
            showCreateAccountDialog(accountType);

        } catch (Exception e) {
            System.err.println("Erro ao abrir nova conta: " + e.getMessage());
            showError("Erro ao abrir conta: " + e.getMessage());
        }
    }

    private void showCreateAccountDialog(String accountType) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(view);
        CreateAccountPinDialog pinDialog = new CreateAccountPinDialog(parentFrame, accountType);

        pinDialog.addConfirmListener(e -> handleAccountCreation(accountType, pinDialog));
        pinDialog.addCancelListener(e -> pinDialog.dispose());

        pinDialog.setVisible(true);
    }

    private void handleAccountCreation(String accountType, CreateAccountPinDialog pinDialog) {
        try {
            String pin = pinDialog.getPin();
            String confirmPin = pinDialog.getConfirmPin();

            // Validar PIN
            if (pin.isEmpty() || confirmPin.isEmpty()) {
                pinDialog.showError("Por favor, preencha ambos os campos de PIN!");
                return;
            }

            if (pin.length() != 4 || !pin.matches("\\d+")) {
                pinDialog.showError("O PIN deve conter exatamente 4 dígitos numéricos!");
                return;
            }

            if (!pin.equals(confirmPin)) {
                pinDialog.showError("Os PINs não coincidem!");
                return;
            }

            // Criar a conta
            Account newAccount = accountService.createNewAccount(currentCustomerId, accountType, pin);

            // Fechar diálogo
            pinDialog.dispose();

            // Mostrar mensagem de sucesso
            showSuccess("Conta criada com sucesso!\n" +
                    "Número da conta: " + newAccount.getAccountNumber() + "\n" +
                    "Tipo: " + getAccountTypeDisplay(accountType) + "\n" +
                    "Saldo inicial: MZN 0,00");

            // Atualizar lista de contas
            refreshCustomerAccounts();

            // Atualizar display
            updateAccountsDisplay();

        } catch (Exception e) {
            System.err.println("Erro ao criar conta: " + e.getMessage());
            pinDialog.showError("Erro ao criar conta: " + e.getMessage());
        }
    }

    private void refreshCustomerAccounts() {
        try {
            this.customerAccounts = accountService.findAccountsByCustomerId(currentCustomerId);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar lista de contas: " + e.getMessage());
            showError("Erro ao atualizar contas: " + e.getMessage());
        }
    }

    private String getAccountTypeDisplay(String accountType) {
        return accountType.equals("CORRENTE") ? "Conta Corrente" : "Conta Poupança";
    }

    private void loadCustomerData() {
        try {
            // Buscar dados completos do cliente
            currentCustomer = customerService.findCustomerById(currentCustomerId);

            if (currentCustomer != null) {
                // Formatar data de registro
                String registrationDate = "Não disponível";
                if (currentCustomer.getRegistrationDate() != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm");
                    registrationDate = sdf.format(currentCustomer.getRegistrationDate());
                }

                // Atualizar a view com os dados do cliente
                view.setCustomerInfo(
                        currentCustomer.getFullName(),
                        currentCustomer.getEmail(),
                        currentCustomer.getPhone(),
                        currentCustomer.getAddress(),
                        currentCustomer.getBiNumber(),
                        currentCustomer.getNuit(),
                        currentCustomer.getPassportNumber(),
                        registrationDate
                );
            } else {
                showError("Não foi possível carregar os dados do cliente.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar dados do cliente: " + e.getMessage());
            showError("Erro ao carregar dados: " + e.getMessage());
        }
    }

    private void updateAccountsDisplay() {
        try {
            view.updateAccounts(customerAccounts);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar display de contas: " + e.getMessage());
            showError("Erro ao carregar contas: " + e.getMessage());
        }
    }

    private void changePassword() {
        try {
            // Diálogo para selecionar tipo de senha a alterar
            String[] options = {"Senha do Usuário", "PIN da Conta", "Cancelar"};
            int choice = JOptionPane.showOptionDialog(view,
                    "Qual senha deseja alterar?",
                    "Alterar Senha",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            switch (choice) {
                case 0: // Senha do Usuário
                    changeUserPassword();
                    break;
                case 1: // PIN da Conta
                    changeAccountPin();
                    break;
                default:
                    // Cancelar
                    break;
            }

        } catch (Exception e) {
            System.err.println("Erro ao iniciar alteração de senha: " + e.getMessage());
            showError("Erro: " + e.getMessage());
        }
    }

    private void changeUserPassword() {
        try {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(view);
            ChangeUserPasswordDialog passwordDialog = new ChangeUserPasswordDialog(parentFrame);

            passwordDialog.addConfirmListener(e -> handleUserPasswordChange(passwordDialog));
            passwordDialog.addCancelListener(e -> passwordDialog.dispose());

            passwordDialog.setVisible(true);

        } catch (Exception e) {
            System.err.println("Erro ao abrir diálogo de senha: " + e.getMessage());
            showError("Erro: " + e.getMessage());
        }
    }

    private void handleUserPasswordChange(ChangeUserPasswordDialog dialog) {
        try {
            String currentPassword = dialog.getCurrentPassword();
            String newPassword = dialog.getNewPassword();
            String confirmPassword = dialog.getConfirmPassword();

            // Validações
            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                dialog.showError("Todos os campos são obrigatórios.");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                dialog.showError("As novas senhas não coincidem.");
                return;
            }

            if (newPassword.length() < 6) {
                dialog.showError("A nova senha deve ter pelo menos 6 caracteres.");
                return;
            }

            // Criar serviço e alterar senha
            PasswordChangeService passwordService = new PasswordChangeService();
            boolean success = passwordService.changeUserPassword(currentCustomerId, currentPassword, newPassword);

            if (success) {
                dialog.showSuccess("Senha alterada com sucesso!");
                dialog.dispose();
            } else {
                dialog.showError("Falha ao alterar senha.");
            }

        } catch (Exception e) {
            System.err.println("Erro ao alterar senha do usuário: " + e.getMessage());
            dialog.showError("Erro: " + e.getMessage());
        }
    }

    private void changeAccountPin() {
        try {
            if (customerAccounts == null || customerAccounts.isEmpty()) {
                showError("Você não possui contas para alterar o PIN.");
                return;
            }

            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(view);
            ChangeAccountPinDialog pinDialog = new ChangeAccountPinDialog(parentFrame, customerAccounts);

            pinDialog.addConfirmListener(e -> handleAccountPinChange(pinDialog));
            pinDialog.addCancelListener(e -> pinDialog.dispose());

            pinDialog.setVisible(true);

        } catch (Exception e) {
            System.err.println("Erro ao abrir diálogo de PIN: " + e.getMessage());
            showError("Erro: " + e.getMessage());
        }
    }

    private void handleAccountPinChange(ChangeAccountPinDialog dialog) {
        try {
            Account selectedAccount = dialog.getSelectedAccount();
            String currentPin = dialog.getCurrentPin();
            String newPin = dialog.getNewPin();
            String confirmPin = dialog.getConfirmPin();

            // Validações
            if (selectedAccount == null) {
                dialog.showError("Por favor, selecione uma conta.");
                return;
            }

            if (currentPin.isEmpty() || newPin.isEmpty() || confirmPin.isEmpty()) {
                dialog.showError("Todos os campos são obrigatórios.");
                return;
            }

            if (currentPin.length() != 4 || !currentPin.matches("\\d+")) {
                dialog.showError("O PIN atual deve conter 4 dígitos numéricos.");
                return;
            }

            if (newPin.length() != 4 || !newPin.matches("\\d+")) {
                dialog.showError("O novo PIN deve conter 4 dígitos numéricos.");
                return;
            }

            if (!newPin.equals(confirmPin)) {
                dialog.showError("Os novos PINs não coincidem.");
                return;
            }

            if (newPin.equals(currentPin)) {
                dialog.showError("O novo PIN deve ser diferente do atual.");
                return;
            }

            // Criar serviço e alterar PIN
            PasswordChangeService passwordService = new PasswordChangeService();
            boolean success = passwordService.changeAccountPin(
                    selectedAccount.getAccountId(), currentPin, newPin);

            if (success) {
                dialog.showSuccess("PIN da conta " + selectedAccount.getAccountNumber() +
                        " alterado com sucesso!");
                dialog.dispose();
            } else {
                dialog.showError("Falha ao alterar PIN.");
            }

        } catch (Exception e) {
            System.err.println("Erro ao alterar PIN da conta: " + e.getMessage());
            dialog.showError("Erro: " + e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(view, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(view, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    public void updateCustomerAccounts(List<Account> accounts) {
        this.customerAccounts = accounts;
        updateAccountsDisplay();
    }

    // Getters
    public ProfileView getView() {
        return view;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public List<Account> getCustomerAccounts() {
        return customerAccounts;
    }
}