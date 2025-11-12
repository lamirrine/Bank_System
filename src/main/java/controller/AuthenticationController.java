package controller;

import model.entities.Customer;
import model.entities.User;
import model.services.*;
import view.login.componet.CreateAccountPinView;
import view.login.componet.Loginview;
import view.UserTypeSelectionView;
import view.DashboardView;
import javax.swing.*;

public class AuthenticationController {
    private UserTypeSelectionView typeSelectionView;
    private Loginview loginview;
    private AuthenticationService authService;
    private CustomerService customerService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
        this.customerService = new CustomerService();
        initializeViews();
        setupControllers();
    }

    private void initializeViews() {
        typeSelectionView = new UserTypeSelectionView();
        loginview = new Loginview();
    }

    private void setupControllers() {
        typeSelectionView.addClientListener(e -> showClientLogin());
        typeSelectionView.addEmployeeListener(e -> showEmployeeLogin());
        loginview.addLoginListener(e -> handleLogin());
        loginview.addSigupListener(e -> handleSignup());
    }

    public void start() {
        typeSelectionView.setVisible(true);
    }

    private void showTypeSelection() {
        loginview.setVisible(false);

        typeSelectionView.setVisible(true);
    }

    private void showClientLogin() {
        typeSelectionView.setVisible(false);
        loginview.setVisible(true);
        //loginView.setVisible(true);
    }

    private void showEmployeeLogin() {
        typeSelectionView.setVisible(false);
        JOptionPane.showMessageDialog(typeSelectionView,
                "Login de funcionário em desenvolvimento!",
                "Em Desenvolvimento", JOptionPane.INFORMATION_MESSAGE);
    }

    public void handleLogin() {
        String email = loginview.getEmail();
        String password = loginview.getPassword();

        System.out.println("Tentando login com email: " + email); // DEBUG

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(loginview,
                    "Por favor, preencha todos os campos!",
                    "Campos Vazios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // VALIDAÇÃO REAL COM O BANCO DE DADOS
            User user = authService.login(email, password);
            System.out.println("Usuário retornado: " + user); // DEBUG

            if (user != null) {
                System.out.println("Tipo do usuário: " + user.getUserType()); // DEBUG
                System.out.println("É Customer? " + (user instanceof Customer)); // DEBUG
            }

            if (user != null && user instanceof Customer) {
                // Login bem-sucedido
                System.out.println("Login bem-sucedido para: " + user.getEmail()); // DEBUG
                loginview.setVisible(false);
                openCustomerDashboard((Customer) user);

            } else {
                System.out.println("Falha no login - usuário não é Customer ou é null"); // DEBUG
                JOptionPane.showMessageDialog(loginview,
                        "Credenciais inválidas ou usuário não é cliente!",
                        "Login Falhou", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            System.out.println("Erro durante login: " + e.getMessage()); // DEBUG
            e.printStackTrace(); // DEBUG
            JOptionPane.showMessageDialog(loginview,
                    "Erro ao fazer login: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleSignup() {
        try {
            // Obter dados dos campos de registro
            String firstName = loginview.getRegisterFirstName();
            String lastName = loginview.getRegisterLastName();
            String email = loginview.getRegisterEmail();
            String password = loginview.getRegisterPassword();
            String biNumber = loginview.getRegisterBiNumber();
            String passportNumber = loginview.getRegisterPassportNumber();
            String nuit = loginview.getRegisterNuit();
            String phone = loginview.getRegisterPhone();
            String address = loginview.getRegisterAddress();

            System.out.println("Validando campos..."); // DEBUG

            // Validações básicas
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(loginview,
                        "Por favor, preencha: Nome, Apelido, Email e Password!",
                        "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (password.length() < 6) {
                JOptionPane.showMessageDialog(loginview,
                        "A senha deve ter pelo menos 6 caracteres!",
                        "Senha Fraca", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Verificar se tem pelo menos um documento
            if ((biNumber == null || biNumber.isEmpty()) &&
                    (passportNumber == null || passportNumber.isEmpty())) {
                JOptionPane.showMessageDialog(loginview,
                        "É obrigatório fornecer BI ou Passaporte!",
                        "Documento Obrigatório", JOptionPane.WARNING_MESSAGE);
                return;
            }

            System.out.println("Todos os campos válidos. Criando customer...");

            // Criar objeto Customer
            Customer newCustomer = new Customer(
                    firstName, lastName, email, phone, password, address,
                    biNumber, nuit, passportNumber
            );

            // Mostrar tela para definir PIN da conta
            showCreateAccountPinDialog(newCustomer);

        } catch (Exception e) {
            System.err.println("Erro no registro: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(loginview,
                    "Erro no registro: " + e.getMessage(),
                    "Erro de Registro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showCreateAccountPinDialog(Customer customer) {
        CreateAccountPinView pinDialog = new CreateAccountPinView(loginview);

        pinDialog.addConfirmListener(e -> handlePinConfirmation(customer, pinDialog));
        pinDialog.addCancelListener(e -> pinDialog.dispose());

        pinDialog.setVisible(true);
    }

    private void handlePinConfirmation(Customer customer, CreateAccountPinView pinDialog) {
        try {
            String pin = pinDialog.getPin();
            String confirmPin = pinDialog.getConfirmPin();

            // Validar PIN
            if (pin.length() != 4 || !pin.matches("\\d+")) {
                pinDialog.showError("O PIN deve conter exatamente 4 dígitos numéricos!");
                return;
            }

            if (!pin.equals(confirmPin)) {
                pinDialog.showError("Os PINs não coincidem!");
                return;
            }

            // Fechar diálogo
            pinDialog.dispose();

            // Registrar customer e criar conta
            registerCustomerWithAccount(customer, pin);

        } catch (Exception e) {
            pinDialog.showError("Erro: " + e.getMessage());
        }
    }

    private void registerCustomerWithAccount(Customer customer, String pin) {
        try {
            System.out.println("Registrando customer: " + customer.getEmail());

            // Registrar o customer
            Customer registeredCustomer = customerService.registerCustomer(customer);

            System.out.println("Customer registrado com ID: " + registeredCustomer.getUserId());

            // Criar conta corrente automaticamente
            AccountCreationService accountCreationService = new AccountCreationService();
            boolean accountCreated = accountCreationService.createCurrentAccountForCustomer(registeredCustomer, pin);

            if (accountCreated) {
                JOptionPane.showMessageDialog(loginview,
                        "Registro realizado com sucesso!\n" +
                                "Conta corrente criada automaticamente.\n" +
                                "Você já pode fazer login com suas credenciais.",
                        "Registro Bem-sucedido", JOptionPane.INFORMATION_MESSAGE);

                // Limpar campos e voltar para login
                loginview.clearRegisterFields();
                loginview.showRegister(false);
            } else {
                throw new Exception("Falha ao criar conta corrente.");
            }

        } catch (Exception e) {
            System.err.println("Erro no registro completo: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(loginview,
                    "Erro no registro: " + e.getMessage(),
                    "Erro de Registro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openCustomerDashboard(Customer customer) {
        AccountService accountService = new AccountService();
        StatementService statementService = new StatementService();
        CustomerService customerService = new CustomerService();
        DashboardView dashboardView = new DashboardView();

        DashboardController dashboardController = new DashboardController(
                dashboardView, accountService, statementService, customerService, customer
        );

        dashboardView.setVisible(true);

        // Por enquanto, vamos usar serviços null
        // DashboardController dashboardController = new DashboardController(
        // dashboardView, null, null, customer);

        dashboardView.setVisible(true);

        JOptionPane.showMessageDialog(dashboardView,
                "Bem-vindo, " + customer.getFirstName() + "!",
                "Login Bem-sucedido", JOptionPane.INFORMATION_MESSAGE);
    }
}