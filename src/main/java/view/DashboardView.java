// view/DashboardView.java
package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class DashboardView extends JFrame {
    private JLabel balanceLabel;
    private JLabel welcomeLabel;
    private JLabel accountNumberLabel;
    private JButton depositBtn, withdrawBtn, transferBtn, statementBtn, logoutBtn;
    private JLabel dailyLimitLabel, transferLimitLabel;
    private JPanel mainContentPanel;

    public DashboardView() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Banco Digital - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        //setResizable(false);
        getContentPane().setBackground(new Color(248, 250, 252));

        // Layout principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(248, 250, 252));

        // Sidebar
        JPanel sidebar = createSidebar();
        mainPanel.add(sidebar, BorderLayout.WEST);

        // Content area
        JPanel contentPanel = createContentArea();
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(15, 23, 42));
        sidebar.setPreferredSize(new Dimension(280, 900));
        sidebar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Logo section
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(15, 23, 42));
        logoPanel.setPreferredSize(new Dimension(280, 120));
        logoPanel.setMaximumSize(new Dimension(280, 120));
        logoPanel.setLayout(new BorderLayout());

        JLabel logoLabel = new JLabel("🏦 Banco Digital", JLabel.CENTER);
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        logoPanel.add(logoLabel, BorderLayout.CENTER);

        // Menu items
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(6, 1, 0, 5));
        menuPanel.setBackground(new Color(15, 23, 42));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] menuItems = {"🏠 Início", "💰 Depósito", "💸 Levantamento", "🔄 Transferência", "📊 Extrato", "👤 Perfil"};
        Color[] menuColors = {
                new Color(59, 130, 246), // Azul para Início
                new Color(16, 185, 129), // Verde para Depósito
                new Color(239, 68, 68),  // Vermelho para Levantamento
                new Color(168, 85, 247), // Roxo para Transferência
                new Color(245, 158, 11), // Laranja para Extrato
                new Color(14, 165, 233)  // Ciano para Perfil
        };

        for (int i = 0; i < menuItems.length; i++) {
            JButton menuBtn = createMenuButton(menuItems[i], menuColors[i], i == 0);
            menuPanel.add(menuBtn);
        }

        // User info panel
        JPanel userPanel = createUserPanel();

        // Add components to sidebar
        sidebar.add(logoPanel);
        sidebar.add(menuPanel);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(userPanel);

        return sidebar;
    }

    private JButton createMenuButton(String text, Color color, boolean isActive) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBackground(isActive ? color : new Color(30, 41, 59));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efeito hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!isActive) {
                    button.setBackground(new Color(51, 65, 85));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!isActive) {
                    button.setBackground(new Color(30, 41, 59));
                }
            }
        });

        return button;
    }

    private JPanel createUserPanel() {
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BorderLayout(15, 15));
        userPanel.setBackground(new Color(30, 41, 59));
        userPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        userPanel.setMaximumSize(new Dimension(280, 140));

        // User info
        JPanel userInfoPanel = new JPanel(new BorderLayout(10, 5));
        userInfoPanel.setBackground(new Color(30, 41, 59));

        // Avatar with gradient
        JPanel avatarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(0, 0, new Color(59, 130, 246),
                        getWidth(), getHeight(), new Color(168, 85, 247));
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            }
        };
        avatarPanel.setPreferredSize(new Dimension(50, 50));
        avatarPanel.setLayout(new BorderLayout());

        JLabel avatarLabel = new JLabel("JS", JLabel.CENTER);
        avatarLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        avatarLabel.setForeground(Color.WHITE);
        avatarPanel.add(avatarLabel, BorderLayout.CENTER);

        // User details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(new Color(30, 41, 59));

        welcomeLabel = new JLabel("João Silva");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        welcomeLabel.setForeground(Color.WHITE);

        accountNumberLabel = new JLabel("Conta: 12345-X");
        accountNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        accountNumberLabel.setForeground(new Color(148, 163, 184));

        detailsPanel.add(welcomeLabel);
        detailsPanel.add(accountNumberLabel);

        userInfoPanel.add(avatarPanel, BorderLayout.WEST);
        userInfoPanel.add(detailsPanel, BorderLayout.CENTER);

        // Logout button
        logoutBtn = new JButton("🚪 Sair");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setBackground(new Color(239, 68, 68));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        userPanel.add(userInfoPanel, BorderLayout.CENTER);
        userPanel.add(logoutBtn, BorderLayout.SOUTH);

        return userPanel;
    }

    private JPanel createContentArea() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(248, 250, 252));

        // Header
        JPanel headerPanel = createHeader();
        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Main content
        mainContentPanel = createMainContent();
        contentPanel.add(mainContentPanel, BorderLayout.CENTER);

        return contentPanel;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(1120, 80));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(0, 40, 0, 40)
        ));

        JLabel titleLabel = new JLabel("Visão Geral");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(15, 23, 42));

        // Header icons
        JPanel iconsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        iconsPanel.setBackground(Color.WHITE);

        JButton notificationBtn = createHeaderIconButton("🔔", new Color(59, 130, 246));
        JButton settingsBtn = createHeaderIconButton("⚙️", new Color(107, 114, 128));

        iconsPanel.add(notificationBtn);
        iconsPanel.add(settingsBtn);

        header.add(titleLabel, BorderLayout.WEST);
        header.add(iconsPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createAccountsPanel() {
        JPanel panel = new JPanel();
        // Implementar criação do painel de contas
        panel.add(new JLabel("Accounts Panel"));
        return panel;
    }

    private JPanel createTransactionsPanel() {
        JPanel panel = new JPanel();
        // Implementar criação do painel de transações
        panel.add(new JLabel("Transactions Panel"));
        return panel;
    }


    private JPanel createOffersPanel() {
        JPanel panel = new JPanel();
        // Implementar criação do painel de ofertas
        panel.add(new JLabel("Offers Panel"));
        return panel;
    }

    private JPanel createContactPanel() {
        JPanel panel = new JPanel();
        // Implementar criação do painel de contato
        panel.add(new JLabel("Contact Panel"));
        return panel;
    }

    private JButton createHeaderIconButton(String icon, Color color) {
        JButton button = new JButton(icon);
        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        button.setPreferredSize(new Dimension(45, 45));
        button.setBackground(new Color(243, 244, 246));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
                button.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(243, 244, 246));
                button.setForeground(Color.BLACK);
            }
        });

        return button;
    }


    private JPanel createMainContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(248, 250, 252));
        content.setBorder(BorderFactory.createEmptyBorder(25, 40, 40, 40));

        // Welcome section
        content.add(createWelcomeSection());
        content.add(Box.createVerticalStrut(30));

        // Balance card
        content.add(createBalanceCard());
        content.add(Box.createVerticalStrut(30));

        // Quick actions
        content.add(createQuickActions());
        content.add(Box.createVerticalStrut(30));

        // Main content row
        JPanel rowPanel = new JPanel(new BorderLayout(20, 0));

        // Left side - Accounts and Transactions
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(createAccountsPanel());
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(createTransactionsPanel());

        // Right side - Various panels
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        //rightPanel.add(createLimitsPanel());
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(createOffersPanel());
        rightPanel.add(Box.createVerticalStrut(20));
        //rightPanel.add(createContactsPanel());

        rowPanel.add(leftPanel, BorderLayout.CENTER);
        rowPanel.add(rightPanel, BorderLayout.EAST);

        content.add(rowPanel);

        return content;
    }

    private JPanel createWelcomeSection() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 250, 252));

        JLabel welcomeText = new JLabel("Bom dia, João! 👋");
        welcomeText.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeText.setForeground(new Color(15, 23, 42));

        JLabel dateLabel = new JLabel("Segunda-feira, 15 de Junho 2024");
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setForeground(new Color(107, 114, 128));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(new Color(248, 250, 252));
        textPanel.add(welcomeText);
        textPanel.add(dateLabel);

        panel.add(textPanel, BorderLayout.WEST);
        return panel;
    }

    private JPanel createBalanceCard() {
        JPanel panel = createCard(900, 160, new Color(59, 130, 246));
        panel.setLayout(new BorderLayout());

        // Gradient background
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Saldo Disponível");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(243, 244, 246));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(25, 30, 5, 0));

        balanceLabel = new JLabel("MZN 12.345,67");
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        balanceLabel.setForeground(Color.WHITE);
        balanceLabel.setBorder(BorderFactory.createEmptyBorder(0, 30, 20, 0));

        JButton detailsBtn = new JButton("Ver Detalhes →");
        detailsBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        detailsBtn.setBackground(Color.WHITE);
        detailsBtn.setForeground(new Color(59, 130, 246));
        detailsBtn.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        detailsBtn.setFocusPainted(false);
        detailsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 30));
        buttonPanel.add(detailsBtn);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(balanceLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createQuickActions() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 250, 252));

        JLabel titleLabel = new JLabel("Ações Rápidas");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(15, 23, 42));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel actionsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        actionsPanel.setBackground(new Color(248, 250, 252));

        String[] actions = {"💰 Depositar", "💸 Levantar", "🔄 Transferir", "📊 Extrato"};
        String[] icons = {"💰", "💸", "🔄", "📊"};
        Color[] colors = {
                new Color(16, 185, 129),
                new Color(239, 68, 68),
                new Color(168, 85, 247),
                new Color(245, 158, 11)
        };

        for (int i = 0; i < actions.length; i++) {
            JButton actionBtn = createActionCard(actions[i], icons[i], colors[i]);
            actionsPanel.add(actionBtn);
        }

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(actionsPanel, BorderLayout.CENTER);

        return panel;
    }

    private JButton createActionCard(String text, String icon, Color color) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout(0, 10));
        button.setPreferredSize(new Dimension(200, 120));
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235)),
                BorderFactory.createEmptyBorder(25, 20, 25, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);

        JLabel iconLabel = new JLabel(icon, JLabel.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconLabel.setForeground(color);

        JLabel textLabel = new JLabel(text.substring(text.indexOf(' ') + 1), JLabel.CENTER);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        textLabel.setForeground(new Color(15, 23, 42));

        button.add(iconLabel, BorderLayout.CENTER);
        button.add(textLabel, BorderLayout.SOUTH);

        // Store the action button reference
        switch (text) {
            case "💰 Depositar": depositBtn = button; break;
            case "💸 Levantar": withdrawBtn = button; break;
            case "🔄 Transferir": transferBtn = button; break;
            case "📊 Extrato": statementBtn = button; break;
        }

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(color, 2),
                        BorderFactory.createEmptyBorder(23, 18, 23, 18)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(229, 231, 235)),
                        BorderFactory.createEmptyBorder(25, 20, 25, 20)
                ));
            }
        });

        return button;
    }

    // ... (os métodos createAccountsPanel, createTransactionsPanel, createLimitsPanel, 
    // createOffersPanel, createContactsPanel permanecem similares aos anteriores, 
    // mas com cores e estilos atualizados)

    private JPanel createCard(int width, int height, Color backgroundColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (backgroundColor != null) {
                    GradientPaint gradient = new GradientPaint(0, 0, backgroundColor,
                            getWidth(), getHeight(),
                            darkenColor(backgroundColor, 0.2f));
                    g2.setPaint(gradient);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                } else {
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                }
            }
        };
        card.setPreferredSize(new Dimension(width, height));
        card.setOpaque(false);
        return card;
    }

    private JPanel createCard(int width, int height) {
        return createCard(width, height, null);
    }

    private Color darkenColor(Color color, float factor) {
        return new Color(
                Math.max((int)(color.getRed() * factor), 0),
                Math.max((int)(color.getGreen() * factor), 0),
                Math.max((int)(color.getBlue() * factor), 0)
        );
    }

    // Getters for buttons
    public JButton getDepositBtn() { return depositBtn; }
    public JButton getWithdrawBtn() { return withdrawBtn; }
    public JButton getTransferBtn() { return transferBtn; }
    public JButton getStatementBtn() { return statementBtn; }
    public JButton getLogoutBtn() { return logoutBtn; }

    // Method to update balance
    public void setBalance(double balance) {
        balanceLabel.setText(String.format("MZN %,.2f", balance));
    }

    // Method to update user info
    public void setUserInfo(String name, String accountNumber) {
        welcomeLabel.setText(name);
        accountNumberLabel.setText("Conta: " + accountNumber);
    }
}