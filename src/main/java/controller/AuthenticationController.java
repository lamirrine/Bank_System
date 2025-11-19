package controller;

import model.entities.*;
import model.services.*;
import view.*;
import view.admin.*;
import view.login.componet.CreateAccountPinView;
import view.login.componet.EmployeeLoginView;
import view.login.componet.Loginview;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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
    }

    private void showEmployeeLogin() {
        typeSelectionView.setVisible(false);

        EmployeeLoginView employeeLoginView = new EmployeeLoginView();

        // Listener para voltar
        employeeLoginView.addBackListener(e -> {
            employeeLoginView.dispose();
            typeSelectionView.setVisible(true);
        });

        // Listener para login REAL
        employeeLoginView.addLoginListener(e -> {
            handleEmployeeLogin(employeeLoginView);
        });

        employeeLoginView.setVisible(true);
    }

    private void showCustomerManagement(EmployeeDashboardView dashboard, CustomerManagementView customerView) {
        try {
            // Carregar dados
            CustomerService customerService = new CustomerService();
            java.util.List<Customer> customers = customerService.findAllCustomers();
            customerView.setCustomers(customers);
            customerView.setStats(customers.size(), customers.size());

            // Configurar botão voltar
            customerView.getBackBtn().addActionListener(e -> {
                dashboard.getContentPane().remove(customerView);
                dashboard.revalidate();
                dashboard.repaint();
            });

            // Mostrar no dashboard
            dashboard.getContentPane().removeAll();
            dashboard.getContentPane().add(customerView, "grow");
            dashboard.revalidate();
            dashboard.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(dashboard, "Erro ao carregar clientes: " + e.getMessage());
        }
    }

    private void showAccountManagement(EmployeeDashboardView dashboard, AccountManagementView accountView) {
        try {
            // Carregar dados
            AccountService accountService = new AccountService();
            java.util.List<Account> accounts = accountService.getAllAccounts();
            accountView.setAccounts(accounts);

            double totalBalance = 0;
            int activeAccounts = 0;
            for (Account acc : accounts) {
                totalBalance += acc.getBalance();
                if (acc.getStatus() == model.enums.AccountStatus.ATIVA) {
                    activeAccounts++;
                }
            }
            accountView.setStats(accounts.size(), activeAccounts, totalBalance);

            // Configurar botão voltar
            accountView.getBackBtn().addActionListener(e -> {
                dashboard.getContentPane().remove(accountView);
                dashboard.revalidate();
                dashboard.repaint();
            });

            // Mostrar no dashboard
            dashboard.getContentPane().removeAll();
            dashboard.getContentPane().add(accountView, "grow");
            dashboard.revalidate();
            dashboard.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(dashboard, "Erro ao carregar contas: " + e.getMessage());
        }
    }

    private void showTransactionView(EmployeeDashboardView dashboard, TransactionView transactionView) {
        try {
            // Carregar dados
            StatementService statementService = new StatementService();
            java.util.List<Transaction> transactions = statementService.getAllTransactions();
            transactionView.setTransactions(transactions);

            double totalVolume = 0;
            for (Transaction trans : transactions) {
                totalVolume += trans.getAmount();
            }
            transactionView.setStats(transactions.size(), totalVolume);

            // Configurar botão voltar
            transactionView.getBackBtn().addActionListener(e -> {
                dashboard.getContentPane().remove(transactionView);
                dashboard.revalidate();
                dashboard.repaint();
            });

            // Mostrar no dashboard
            dashboard.getContentPane().removeAll();
            dashboard.getContentPane().add(transactionView, "grow");
            dashboard.revalidate();
            dashboard.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(dashboard, "Erro ao carregar transações: " + e.getMessage());
        }
    }

    private void showEmployeeManagement(EmployeeDashboardView dashboard, EmployeeManagementView employeeView) {
        try {
            // Carregar dados
            EmployeeService employeeService = new EmployeeService();
            java.util.List<Employee> employees = employeeService.getAllEmployees();
            employeeView.setEmployees(employees);

            int admins = 0, managers = 0, staff = 0;
            for (Employee emp : employees) {
                switch (emp.getAccessLevel()) {
                    case ADMIN: admins++; break;
                    case MANAGER: managers++; break;
                    case STAFF: staff++; break;
                }
            }
            employeeView.setStats(employees.size(), admins, managers, staff);

            // Configurar botão voltar
            employeeView.getBackBtn().addActionListener(e -> {
                dashboard.getContentPane().remove(employeeView);
                dashboard.revalidate();
                dashboard.repaint();
            });

            // Mostrar no dashboard
            dashboard.getContentPane().removeAll();
            dashboard.getContentPane().add(employeeView, "grow");
            dashboard.revalidate();
            dashboard.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(dashboard, "Erro ao carregar funcionários: " + e.getMessage());
        }
    }

    private void handleEmployeeLogin(EmployeeLoginView loginView) {
        String email = loginView.getEmail();
        String password = loginView.getPassword();

        if (email.isEmpty() || password.isEmpty()) {
            loginView.showError("Por favor, preencha todos os campos!");
            return;
        }

        try {
            // Criar serviço de funcionário
            EmployeeService employeeService = new EmployeeService();

            // Fazer login
            Employee employee = employeeService.login(email, password);

            if (employee != null) {
                // Login bem-sucedido - abrir dashboard
                loginView.dispose();
                openEmployeeDashboard(employee);
            } else {
                loginView.showError("Email ou senha incorretos!");
            }

        } catch (Exception ex) {
            loginView.showError("Erro: " + ex.getMessage());
        }
    }

    private void openEmployeeDashboard(Employee employee) {
        EmployeeDashboardView dashboardView = new EmployeeDashboardView();
        dashboardView.setVisible(true);

        dashboardView.setUserInfo(
                employee.getFullName(),
                employee.getAccessLevel().toString(),
                employee.getEmail()
        );

        // LOGOUT - Voltar para seleção de tipo
        dashboardView.getLogoutBtn().addActionListener(e -> {
            dashboardView.dispose();
            typeSelectionView.setVisible(true);
        });

        // GESTÃO DE CLIENTES - Abrir em dialog
        dashboardView.getManageCustomersBtn().addActionListener(e -> {
            openCustomerManagement(dashboardView);
        });

        // GESTÃO DE CONTAS - Abrir em dialog
        dashboardView.getManageAccountsBtn().addActionListener(e -> {
            openAccountManagement(dashboardView);
        });

        // TRANSAÇÕES - Abrir em dialog
        dashboardView.getViewTransactionsBtn().addActionListener(e -> {
            openTransactionView(dashboardView);
        });

        if (employee.getAccessLevel() == model.enums.AccessLevel.ADMIN) {
            dashboardView.getManageEmployeesBtn().addActionListener(e -> {
                // ✅ PASSAR O EMPLOYEE ATUAL PARA O CONTROLLER
                openEmployeeManagement(dashboardView, employee);
            });
        } else {
            dashboardView.getManageEmployeesBtn().setVisible(false);
        }

        dashboardView.setVisible(true);
    }

    private void openEmployeeManagement(JFrame parent, Employee currentEmployee) {
        JDialog dialog = new JDialog(parent, "Gestão de Funcionários", true);
        dialog.setSize(1920, 1080);
        dialog.setLocationRelativeTo(parent);

        EmployeeManagementView employeeView = new EmployeeManagementView();

        try {
            // CARREGAR DADOS REAIS
            EmployeeService employeeService = new EmployeeService();

            // ✅ CRIAR O CONTROLLER PASSANDO O EMPLOYEE ATUAL
            EmployeeManagementController employeeController = new EmployeeManagementController(
                    employeeView,
                    employeeService,
                    currentEmployee  // Passar o employee atual
            );

            java.util.List<Employee> employees = employeeService.getAllEmployees();
            employeeView.setEmployees(employees);

            int admins = 0, managers = 0, staff = 0;
            for (Employee emp : employees) {
                switch (emp.getAccessLevel()) {
                    case ADMIN: admins++; break;
                    case MANAGER: managers++; break;
                    case STAFF: staff++; break;
                }
            }
            employeeView.setStats(employees.size(), admins, managers, staff);
        } catch (Exception ex) {
            employeeView.setEmployees(new ArrayList<>());
            employeeView.setStats(0, 0, 0, 0);
            ex.printStackTrace();
        }

        // ✅ ADICIONAR LISTENER EXTRA PARA FECHAR O DIALOG
        employeeView.addBackListener(e -> dialog.dispose());

        dialog.add(employeeView);
        dialog.setVisible(true);
    }

    private void openCustomerManagement(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Gestão de Clientes", true);
        dialog.setSize(1980, 1080);
        dialog.setLocationRelativeTo(parent);

        CustomerManagementView customerView = new CustomerManagementView();

        try {
            CustomerService customerService = new CustomerService();
            java.util.List<Customer> customers = customerService.findAllCustomers();
            customerView.setCustomers(customers);
            customerView.setStats(customers.size(), customers.size());
        } catch (Exception ex) {
            customerView.setCustomers(new ArrayList<>());
            customerView.setStats(0, 0);
        }

        customerView.getBackBtn().addActionListener(e -> dialog.dispose());

        customerView.getSearchBtn().addActionListener(e -> {
            String searchText = customerView.getSearchText();
            if (!searchText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Buscando por: " + searchText);
            }
        });

        customerView.getViewDetailsBtn().addActionListener(e -> {
            int customerId = customerView.getSelectedCustomerId();
            if (customerId != -1) {
                JOptionPane.showMessageDialog(dialog, "Visualizando detalhes do cliente ID: " + customerId);
            } else {
                JOptionPane.showMessageDialog(dialog, "Selecione um cliente primeiro");
            }
        });

        dialog.add(customerView);
        dialog.setVisible(true);
    }

    private void openAccountManagement(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Gestão de Contas", true);
        dialog.setSize(1980, 1080);
        dialog.setLocationRelativeTo(parent);

        AccountManagementView accountView = new AccountManagementView();

        try {
            // CARREGAR DADOS REAIS
            AccountService accountService = new AccountService();
            java.util.List<Account> accounts = accountService.getAllAccounts();
            accountView.setAccounts(accounts);

            double totalBalance = 0;
            int activeAccounts = 0;
            for (Account acc : accounts) {
                totalBalance += acc.getBalance();
                if (acc.getStatus() == model.enums.AccountStatus.ATIVA) {
                    activeAccounts++;
                }
            }
            accountView.setStats(accounts.size(), activeAccounts, totalBalance);
        } catch (Exception ex) {
            accountView.setAccounts(new ArrayList<>());
            accountView.setStats(0, 0, 0);
        }

        // Configurar botão voltar
        accountView.getBackBtn().addActionListener(e -> dialog.dispose());

        // Configurar botão filtrar
        accountView.getSearchBtn().addActionListener(e -> {
            String tipo = accountView.getTypeFilter();
            String status = accountView.getStatusFilter();
            JOptionPane.showMessageDialog(dialog, "Filtrando - Tipo: " + tipo + ", Status: " + status);
        });

        // Configurar botão nova conta
        accountView.getOpenAccountBtn().addActionListener(e -> {
            JOptionPane.showMessageDialog(dialog, "Abrindo nova conta...");
        });

        dialog.add(accountView);
        dialog.setVisible(true);
    }

    private void openTransactionView(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Visualizar Transações", true);
        dialog.setSize(1920, 1080);
        dialog.setLocationRelativeTo(parent);

        TransactionView transactionView = new TransactionView();

        try {
            // CARREGAR DADOS REAIS
            StatementService statementService = new StatementService();
            java.util.List<Transaction> transactions = statementService.getAllTransactions();
            transactionView.setTransactions(transactions);

            double totalVolume = 0;
            for (Transaction trans : transactions) {
                totalVolume += trans.getAmount();
            }
            transactionView.setStats(transactions.size(), totalVolume);
        } catch (Exception ex) {
            transactionView.setTransactions(new ArrayList<>());
            transactionView.setStats(0, 0);
        }

        // Configurar botão voltar
        transactionView.getBackBtn().addActionListener(e -> dialog.dispose());

        // Configurar botão filtrar
        transactionView.getSearchBtn().addActionListener(e -> {
            String tipo = transactionView.getTypeFilter();
            String status = transactionView.getStatusFilter();
            JOptionPane.showMessageDialog(dialog, "Filtrando transações...");
        });

        // Configurar botão exportar
        transactionView.getExportBtn().addActionListener(e -> {
            JOptionPane.showMessageDialog(dialog, "Exportando relatório...");
        });

        dialog.add(transactionView);
        dialog.setVisible(true);
    }

    private void openEmployeeManagement(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Gestão de Funcionários", true);
        dialog.setSize(1920, 1080);
        dialog.setLocationRelativeTo(parent);

        EmployeeManagementView employeeView = new EmployeeManagementView();

        try {
            // CARREGAR DADOS REAIS
            EmployeeService employeeService = new EmployeeService();

            // CRIAR O CONTROLLER - ISSO É O QUE ESTAVA FALTANDO!
            EmployeeManagementController employeeController = new EmployeeManagementController(
                    employeeView,
                    employeeService
            );

            java.util.List<Employee> employees = employeeService.getAllEmployees();
            employeeView.setEmployees(employees);

            int admins = 0, managers = 0, staff = 0;
            for (Employee emp : employees) {
                switch (emp.getAccessLevel()) {
                    case ADMIN: admins++; break;
                    case MANAGER: managers++; break;
                    case STAFF: staff++; break;
                }
            }
            employeeView.setStats(employees.size(), admins, managers, staff);
        } catch (Exception ex) {
            employeeView.setEmployees(new ArrayList<>());
            employeeView.setStats(0, 0, 0, 0);
        }

        employeeView.addBackListener(e -> dialog.dispose());

        dialog.add(employeeView);
        dialog.setVisible(true);
    }

    private void showView(EmployeeDashboardView dashboard, JPanel view, String title) {
        // Limpar o conteúdo atual
        dashboard.getContentPane().removeAll();

        // Adicionar a nova tela
        dashboard.getContentPane().setLayout(new BorderLayout());
        dashboard.getContentPane().add(view, BorderLayout.CENTER);

        // Atualizar título
        dashboard.setTitle("Sistema Bancário - " + title);

        // Atualizar a interface
        dashboard.revalidate();
        dashboard.repaint();

        System.out.println("Mostrando tela: " + title);
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