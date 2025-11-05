package controller;

import model.entities.Customer;
import model.entities.User;
import model.services.AuthenticationService;
import view.login.componet.Loginview;
import view.UserTypeSelectionView;
import view.DashboardView;
import javax.swing.*;

public class AuthenticationController {
    private UserTypeSelectionView typeSelectionView;
    private Loginview loginview;
    private AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
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

    private void openCustomerDashboard(Customer customer) {
        DashboardView dashboardView = new DashboardView();

        // Por enquanto, vamos usar serviços null
        DashboardController dashboardController = new DashboardController(
                dashboardView, null, null, customer);

        dashboardView.setVisible(true);

        JOptionPane.showMessageDialog(dashboardView,
                "Bem-vindo, " + customer.getFirstName() + "!",
                "Login Bem-sucedido", JOptionPane.INFORMATION_MESSAGE);
    }
}