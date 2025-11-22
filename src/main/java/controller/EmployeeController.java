package controller;

import model.entities.Employee;
import model.services.EmployeeService;
import model.services.CustomerService;
import model.services.AccountService;
import model.services.StatementService;
import view.admin.EmployeeDashboardView;
import view.UserTypeSelectionView;
import view.admin.EmployeeManagementView;
import view.login.componet.EmployeeLoginView;

import javax.swing.*;
import java.util.List;

public class EmployeeController {
    private EmployeeLoginView loginView;
    private EmployeeDashboardView dashboardView;
    private EmployeeService employeeService;
    private CustomerService customerService;
    private AccountService accountService;
    private StatementService statementService;
    private Employee currentEmployee;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
        this.customerService = new CustomerService();
        this.accountService = new AccountService();
        this.statementService = new StatementService();

        initializeViews();
        setupControllers();
    }

    private void initializeViews() {
        // Criar login view com parent frame
        loginView = new EmployeeLoginView();

        // Criar dashboard
        dashboardView = new EmployeeDashboardView();
        dashboardView.setVisible(true);
        dashboardView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void setupControllers() {
        // Login view listeners
        loginView.addLoginListener(e -> handleEmployeeLogin());
        loginView.addBackListener(e -> handleBackToSelection());

        // Dashboard listeners
        dashboardView.getLogoutBtn().addActionListener(e -> handleLogout());
        dashboardView.getManageCustomersBtn().addActionListener(e -> showCustomerManagement());
        dashboardView.getManageAccountsBtn().addActionListener(e -> showAccountManagement());
        dashboardView.getViewTransactionsBtn().addActionListener(e -> showTransactionView());
        dashboardView.getManageEmployeesBtn().addActionListener(e -> showEmployeeManagement());
        dashboardView.getReportsBtn().addActionListener(e -> showReports());
    }

    public void showLogin() {
        System.out.println("Abrindo tela de login do funcionário...");
        loginView.setVisible(true);
    }

    private void handleEmployeeLogin() {
        try {
            String email = loginView.getEmail();
            String password = loginView.getPassword();

            System.out.println("Tentando login de funcionário: " + email);

            if (email.isEmpty() || password.isEmpty()) {
                loginView.showError("Por favor, preencha todos os campos.");
                return;
            }

            currentEmployee = employeeService.login(email, password);

            if (currentEmployee != null) {
                System.out.println("Login bem-sucedido: " + currentEmployee.getFullName());
                loginView.setVisible(false);
                openEmployeeDashboard();
            } else {
                loginView.showError("Credenciais inválidas.");
            }

        } catch (Exception e) {
            System.err.println("Erro no login de funcionário: " + e.getMessage());
            e.printStackTrace();
            loginView.showError("Erro no login: " + e.getMessage());
        }
    }

    private void openEmployeeDashboard() {
        try {
            // Configurar informações do usuário
            dashboardView.setUserInfo(
                    currentEmployee.getFullName(),
                    currentEmployee.getAccessLevel().toString(),
                    currentEmployee.getEmail()
            );

            // Mostrar/ocultar recursos administrativos
            boolean isAdmin = currentEmployee.getAccessLevel() == model.enums.AccessLevel.ADMIN;
            dashboardView.setAdminFeaturesVisible(isAdmin);

            // Mostrar dashboard
            dashboardView.setVisible(true);
            System.out.println("Dashboard do funcionário aberto com sucesso!");

        } catch (Exception e) {
            System.err.println("Erro ao abrir dashboard: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Erro ao abrir dashboard: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleBackToSelection() {
        try {
            System.out.println("Voltando para seleção de tipo de usuário...");
            loginView.setVisible(false);
            loginView.dispose();

            // Reabrir a tela de seleção
            SwingUtilities.invokeLater(() -> {
                UserTypeSelectionView selectionView = new UserTypeSelectionView();
                selectionView.setVisible(true);
            });
        } catch (Exception e) {
            System.err.println("Erro ao voltar para seleção: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(dashboardView,
                "Tem certeza que deseja sair?", "Confirmar Saída",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            System.out.println("Efetuando logout do funcionário...");
            dashboardView.setVisible(false);
            dashboardView.dispose();
            currentEmployee = null;

            // Voltar para o login
            showLogin();
        }
    }

    private void showCustomerManagement() {
        try {
            List<model.entities.Customer> customers = customerService.findAllCustomers();
            JOptionPane.showMessageDialog(dashboardView,
                    "Gestão de Clientes\nTotal de clientes: " + customers.size() + "\nFuncionalidade em desenvolvimento completo.",
                    "Gestão de Clientes", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dashboardView,
                    "Erro ao carregar clientes: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAccountManagement() {
        try {
            List<model.entities.Account> accounts = accountService.getAllAccounts();
            JOptionPane.showMessageDialog(dashboardView,
                    "Gestão de Contas\nTotal de contas: " + accounts.size() + "\nFuncionalidade em desenvolvimento completo.",
                    "Gestão de Contas", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dashboardView,
                    "Erro ao carregar contas: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showTransactionView() {
        try {
            List<model.entities.Transaction> transactions = statementService.getAllTransactions();
            JOptionPane.showMessageDialog(dashboardView,
                    "Visualização de Transações\nTotal de transações: " + transactions.size() + "\nFuncionalidade em desenvolvimento completo.",
                    "Transações", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dashboardView,
                    "Erro ao carregar transações: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showEmployeeManagement() {
        if (currentEmployee.getAccessLevel() != model.enums.AccessLevel.ADMIN) {
            JOptionPane.showMessageDialog(dashboardView,
                    "Acesso negado. Apenas administradores podem gerir funcionários.",
                    "Acesso Negado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            System.out.println("=== INICIANDO GESTÃO DE FUNCIONÁRIOS ===");

            // Criar JFrame
            JFrame managementFrame = new JFrame("Gestão de Funcionários");
            managementFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            managementFrame.setSize(1200, 800);
            managementFrame.setLocationRelativeTo(dashboardView);

            // Criar a view
            EmployeeManagementView managementView = new EmployeeManagementView();

            // Criar o controlador - IMPORTANTE: passar a view e o service
            EmployeeManagementController managementController = new EmployeeManagementController(
                    managementView,
                    employeeService,
                    currentEmployee
            );

            // Adicionar ao frame
            managementFrame.setContentPane(managementView);
            managementFrame.setVisible(true);

            System.out.println("=== GESTÃO DE FUNCIONÁRIOS PRONTA ===");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(dashboardView,
                    "Erro: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showReports() {
        JOptionPane.showMessageDialog(dashboardView,
                "Relatórios e Estatísticas\nFuncionalidade em desenvolvimento completo.",
                "Relatórios", JOptionPane.INFORMATION_MESSAGE);
    }

    public Employee getCurrentEmployee() {
        return currentEmployee;
    }
}