package view.admin;

import net.miginfocom.swing.MigLayout;
import view.componet.MyButton;

import javax.swing.*;
import java.awt.*;

public class EmployeeDashboardView extends JFrame {
    private MyButton manageCustomersBtn;
    private MyButton manageAccountsBtn;
    private MyButton viewTransactionsBtn;
    private MyButton manageEmployeesBtn;
    private MyButton reportsBtn;
    private MyButton logoutBtn;
    private JLabel welcomeLabel;
    private JLabel userInfoLabel;
    private JPanel mainContentPanel;

    public EmployeeDashboardView() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Sistema Bancário - Dashboard Funcionário");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1900, 1000);
        setLocationRelativeTo(null);
        setResizable(true);

        setLayout(new BorderLayout());
        initializeComponents();
    }

    private void initializeComponents() {
        add(createHeader(), BorderLayout.NORTH);

        mainContentPanel = createMainContent();
        add(mainContentPanel, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new MigLayout("fill, insets 0 0 15 0", "[grow][]", "[]"));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));

        JPanel userInfoPanel = new JPanel(new MigLayout("insets 0", "[][]", "[]"));
        userInfoPanel.setBackground(Color.WHITE);

        welcomeLabel = new JLabel("Bem-vindo, Funcionário!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(15, 23, 42));

        userInfoLabel = new JLabel("Carregando informações...");
        userInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userInfoLabel.setForeground(new Color(107, 114, 128));

        userInfoPanel.add(welcomeLabel, "wrap");
        userInfoPanel.add(userInfoLabel);

        logoutBtn = new MyButton();
        logoutBtn.setText("Sair");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutBtn.setBackground(new Color(239, 68, 68));
        logoutBtn.setFocusPainted(false);

        logoutBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutBtn.setBackground(new Color(220, 38, 38));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutBtn.setBackground(new Color(239, 68, 68));
            }
        });

        header.add(userInfoPanel, "grow");
        header.add(logoutBtn, "w 100!");

        return header;
    }

    private JPanel createMainContent() {
        JPanel content = new JPanel(new MigLayout("wrap 3, fill, insets 20", "[grow][grow][grow]", "[grow][grow]"));
        content.setBackground(new Color(229, 231, 235));

        // Card 1: Gestão de Clientes
        content.add(createDashboardCard(
                "👥 Gestão de Clientes",
                "Gerencie clientes, visualize informações e edite dados cadastrais",
                new Color(59, 130, 246),
                "Gerenciar Clientes"
        ), "grow");

        // Card 2: Gestão de Contas
        content.add(createDashboardCard(
                "💳 Gestão de Contas",
                "Abra novas contas, encerre contas e ajuste limites",
                new Color(16, 185, 129),
                "Gerenciar Contas"
        ), "grow");

        // Card 3: Visualizar Transações
        content.add(createDashboardCard(
                "📊 Visualizar Transações",
                "Consulte transações, filtre por data e exporte relatórios",
                new Color(168, 85, 247),
                "Ver Transações"
        ), "grow");

        // Card 4: Gestão de Funcionários
        content.add(createDashboardCard(
                "👨‍💼 Gestão de Funcionários",
                "Gerencie equipe, defina permissões e acessos",
                new Color(245, 158, 11),
                "Gerenciar Equipe"
        ), "grow");

        return content;
    }

    private JPanel createDashboardCard(String title, String description, Color color, String buttonText) {
        JPanel card = new JPanel(new MigLayout("wrap, fill, insets 25", "[grow]", "[]10[]15[]"));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        // Ícone e Título
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(15, 23, 42));

        // Descrição
        JLabel descLabel = new JLabel("<html><div style='text-align: left; color: #6B7280; font-size: 14px;'>" + description + "</div></html>");

        // Botão de ação
        MyButton actionBtn = new MyButton();
        actionBtn.setText(buttonText);
        actionBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        actionBtn.setBackground(color);
        actionBtn.setFocusPainted(false);

        // Adicionar efeito hover ao botão
        actionBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                actionBtn.setBackground(darkenColor(color, 0.1));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                actionBtn.setBackground(color);
            }
        });

        card.add(titleLabel, "growx");
        card.add(descLabel, "growx");
        card.add(actionBtn, "growx, h 40!");

        assignButtonToField(actionBtn, buttonText);

        return card;
    }

    private void assignButtonToField(MyButton button, String buttonText) {
        switch (buttonText) {
            case "Gerenciar Clientes":
                manageCustomersBtn = button;
                break;
            case "Gerenciar Contas":
                manageAccountsBtn = button;
                break;
            case "Ver Transações":
                viewTransactionsBtn = button;
                break;
            case "Gerenciar Equipe":
                manageEmployeesBtn = button;
                break;
            case "Gerar Relatórios":
                reportsBtn = button;
                break;
        }
    }

    private Color darkenColor(Color color, double factor) {
        int r = Math.max((int)(color.getRed() * (1 - factor)), 0);
        int g = Math.max((int)(color.getGreen() * (1 - factor)), 0);
        int b = Math.max((int)(color.getBlue() * (1 - factor)), 0);
        return new Color(r, g, b);
    }

    // Getters
    public MyButton getManageCustomersBtn() {
        if (manageCustomersBtn == null) manageCustomersBtn = new MyButton();
        return manageCustomersBtn;
    }

    public MyButton getManageAccountsBtn() {
        if (manageAccountsBtn == null) manageAccountsBtn = new MyButton();
        return manageAccountsBtn;
    }

    public MyButton getViewTransactionsBtn() {
        if (viewTransactionsBtn == null) viewTransactionsBtn = new MyButton();
        return viewTransactionsBtn;
    }

    public MyButton getManageEmployeesBtn() {
        if (manageEmployeesBtn == null) manageEmployeesBtn = new MyButton();
        return manageEmployeesBtn;
    }

    public MyButton getReportsBtn() {
        if (reportsBtn == null) reportsBtn = new MyButton();
        return reportsBtn;
    }

    public MyButton getLogoutBtn() {
        return logoutBtn;
    }

    // Métodos para atualizar informações do usuário
    public void setUserInfo(String fullName, String accessLevel, String email) {
        welcomeLabel.setText("Bem-vindo, " + fullName + "!");
        userInfoLabel.setText(accessLevel + " • " + email);
    }

    public void setAdminFeaturesVisible(boolean visible) {
        if (manageEmployeesBtn != null) {
            manageEmployeesBtn.setVisible(visible);
        }
    }
}