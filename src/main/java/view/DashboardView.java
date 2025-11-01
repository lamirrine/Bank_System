// view/DashboardView.java
package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class DashboardView extends JFrame {
    private JLabel balanceLabel;
    private JButton depositBtn, withdrawBtn, transferBtn, statementBtn, logoutBtn;
    private JLabel dailyLimitLabel, transferLimitLabel;

    public DashboardView() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Banco Digital - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(241, 245, 249));

        // Layout principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(241, 245, 249));

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
        sidebar.setBackground(new Color(30, 41, 59));
        sidebar.setPreferredSize(new Dimension(280, 900));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Logo
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(51, 65, 85));
        logoPanel.setPreferredSize(new Dimension(200, 60));
        logoPanel.setMaximumSize(new Dimension(200, 60));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        JLabel logoLabel = new JLabel("Banco Digital", JLabel.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        logoLabel.setForeground(Color.WHITE);
        logoPanel.add(logoLabel);

        // Menu items
        String[] menuItems = {"🏠 Início", "💰 Depósito", "💸 Levantamento", "🔄 Transferência", "📊 Extrato", "👤 Cadastro"};
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(6, 1, 10, 10));
        menuPanel.setBackground(new Color(30, 41, 59));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        for (int i = 0; i < menuItems.length; i++) {
            JButton menuBtn = new JButton(menuItems[i]);
            menuBtn.setFont(new Font("Arial", Font.PLAIN, 14));
            menuBtn.setHorizontalAlignment(SwingConstants.LEFT);
            menuBtn.setBackground(i == 0 ? new Color(15, 23, 42) : new Color(51, 65, 85));
            menuBtn.setForeground(Color.WHITE);
            menuBtn.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
            menuBtn.setFocusPainted(false);
            menuBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            menuPanel.add(menuBtn);
        }

        // User info panel
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BorderLayout(10, 10));
        userPanel.setBackground(new Color(51, 65, 85));
        userPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        userPanel.setMaximumSize(new Dimension(240, 120));

        // User avatar and info
        JPanel userInfoPanel = new JPanel(new BorderLayout(10, 5));
        userInfoPanel.setBackground(new Color(51, 65, 85));

        // Avatar
        JPanel avatarPanel = new JPanel();
        avatarPanel.setPreferredSize(new Dimension(50, 50));
        avatarPanel.setBackground(new Color(16, 185, 129));
        avatarPanel.setLayout(new BorderLayout());
        JLabel avatarLabel = new JLabel("JS", JLabel.CENTER);
        avatarLabel.setFont(new Font("Arial", Font.BOLD, 14));
        avatarLabel.setForeground(Color.WHITE);
        avatarPanel.add(avatarLabel, BorderLayout.CENTER);

        // User details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(new Color(51, 65, 85));

        JLabel nameLabel = new JLabel("João Silva");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setForeground(Color.WHITE);

        JLabel accountLabel = new JLabel("Conta: 12345-X");
        accountLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        accountLabel.setForeground(new Color(148, 163, 184));

        detailsPanel.add(nameLabel);
        detailsPanel.add(accountLabel);

        userInfoPanel.add(avatarPanel, BorderLayout.WEST);
        userInfoPanel.add(detailsPanel, BorderLayout.CENTER);

        // Logout button
        logoutBtn = new JButton("Sair");
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 12));
        logoutBtn.setBackground(new Color(71, 85, 105));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        userPanel.add(userInfoPanel, BorderLayout.CENTER);
        userPanel.add(logoutBtn, BorderLayout.SOUTH);

        // Add components to sidebar
        sidebar.add(logoPanel);
        sidebar.add(menuPanel);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(userPanel);

        return sidebar;
    }

    private JPanel createContentArea() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = createHeader();
        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Main content with scroll
        JScrollPane scrollPane = createMainContent();
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        return contentPanel;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(1120, 80));
        header.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        JLabel titleLabel = new JLabel("Início");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 41, 59));

        // Header icons
        JPanel iconsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        iconsPanel.setBackground(Color.WHITE);

        JButton notificationBtn = createIconButton("🔔");
        JButton settingsBtn = createIconButton("⚙️");

        iconsPanel.add(notificationBtn);
        iconsPanel.add(settingsBtn);

        header.add(titleLabel, BorderLayout.WEST);
        header.add(iconsPanel, BorderLayout.EAST);

        return header;
    }

    private JButton createIconButton(String icon) {
        JButton button = new JButton(icon);
        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        button.setPreferredSize(new Dimension(50, 50));
        button.setBackground(new Color(226, 232, 240));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JScrollPane createMainContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(241, 245, 249));
        content.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));

        // Balance card
        content.add(createBalanceCard());
        content.add(Box.createVerticalStrut(30));

        // Quick actions
        content.add(createQuickActions());
        content.add(Box.createVerticalStrut(30));

        // Accounts and transactions row
        JPanel rowPanel = new JPanel(new BorderLayout());

        // Left side - Accounts and Transactions
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(createAccountsPanel());
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(createTransactionsPanel());

        // Right side - Various panels
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(createLimitsPanel());
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(createOffersPanel());
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(createContactsPanel());

        rowPanel.add(leftPanel, BorderLayout.CENTER);
        rowPanel.add(Box.createHorizontalStrut(20));
        rowPanel.add(rightPanel, BorderLayout.EAST);

        content.add(rowPanel);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private JPanel createBalanceCard() {
        JPanel panel = createCard(900, 180);
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Saldo Disponível");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(30, 41, 59));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0));

        balanceLabel = new JLabel("MZN 12.345,67");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 36));
        balanceLabel.setForeground(new Color(30, 41, 59));
        balanceLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 0));

        JButton detailsBtn = new JButton("Ver Detalhes");
        detailsBtn.setFont(new Font("Arial", Font.BOLD, 14));
        detailsBtn.setBackground(new Color(59, 130, 246));
        detailsBtn.setForeground(Color.WHITE);
        detailsBtn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        detailsBtn.setFocusPainted(false);
        detailsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 20));
        buttonPanel.add(detailsBtn);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(balanceLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createQuickActions() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(241, 245, 249));

        JLabel titleLabel = new JLabel("Ações Rápidas");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(30, 41, 59));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel actionsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        actionsPanel.setBackground(new Color(241, 245, 249));

        String[] actions = {"💰 Depositar", "💸 Levantar", "🔄 Transferir", "📊 Extrato"};
        Color[] colors = {new Color(59, 130, 246), new Color(239, 68, 68),
                new Color(16, 185, 129), new Color(107, 114, 128)};

        for (int i = 0; i < actions.length; i++) {
            JButton actionBtn = createActionCard(actions[i], colors[i]);
            actionsPanel.add(actionBtn);
        }

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(actionsPanel, BorderLayout.CENTER);

        return panel;
    }

    private JButton createActionCard(String text, Color color) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(200, 120));
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);

        JPanel iconPanel = new JPanel();
        iconPanel.setPreferredSize(new Dimension(60, 60));
        iconPanel.setBackground(new Color(226, 232, 240));
        iconPanel.setLayout(new BorderLayout());

        String icon = text.substring(0, text.indexOf(' '));
        JLabel iconLabel = new JLabel(icon, JLabel.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconPanel.add(iconLabel, BorderLayout.CENTER);

        JLabel textLabel = new JLabel(text.substring(text.indexOf(' ') + 1), JLabel.CENTER);
        textLabel.setFont(new Font("Arial", Font.BOLD, 14));
        textLabel.setForeground(new Color(30, 41, 59));

        button.add(iconPanel, BorderLayout.CENTER);
        button.add(textLabel, BorderLayout.SOUTH);

        // Store the action button reference
        switch (text) {
            case "💰 Depositar": depositBtn = button; break;
            case "💸 Levantar": withdrawBtn = button; break;
            case "🔄 Transferir": transferBtn = button; break;
            case "📊 Extrato": statementBtn = button; break;
        }

        return button;
    }

    private JPanel createAccountsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(241, 245, 249));

        JLabel titleLabel = new JLabel("Minhas Contas");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(30, 41, 59));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel accountsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        accountsPanel.setBackground(new Color(241, 245, 249));

        // Account 1
        JPanel account1 = createCard(430, 100);
        account1.setLayout(new BorderLayout());

        JLabel acc1Title = new JLabel("Conta Corrente");
        acc1Title.setFont(new Font("Arial", Font.BOLD, 14));
        acc1Title.setForeground(new Color(30, 41, 59));

        JLabel acc1Number = new JLabel("12345-X");
        acc1Number.setFont(new Font("Arial", Font.PLAIN, 12));
        acc1Number.setForeground(new Color(100, 116, 139));

        JLabel acc1Balance = new JLabel("MZN 5.000,00", JLabel.RIGHT);
        acc1Balance.setFont(new Font("Arial", Font.BOLD, 18));
        acc1Balance.setForeground(new Color(30, 41, 59));

        JPanel acc1Left = new JPanel();
        acc1Left.setLayout(new BoxLayout(acc1Left, BoxLayout.Y_AXIS));
        acc1Left.setBackground(Color.WHITE);
        acc1Left.add(acc1Title);
        acc1Left.add(acc1Number);

        account1.add(acc1Left, BorderLayout.WEST);
        account1.add(acc1Balance, BorderLayout.EAST);

        // Account 2
        JPanel account2 = createCard(430, 100);
        account2.setLayout(new BorderLayout());

        JLabel acc2Title = new JLabel("Conta Poupança");
        acc2Title.setFont(new Font("Arial", Font.BOLD, 14));
        acc2Title.setForeground(new Color(30, 41, 59));

        JLabel acc2Number = new JLabel("67890-Y");
        acc2Number.setFont(new Font("Arial", Font.PLAIN, 12));
        acc2Number.setForeground(new Color(100, 116, 139));

        JLabel acc2Balance = new JLabel("MZN 7.345,67", JLabel.RIGHT);
        acc2Balance.setFont(new Font("Arial", Font.BOLD, 18));
        acc2Balance.setForeground(new Color(30, 41, 59));

        JPanel acc2Left = new JPanel();
        acc2Left.setLayout(new BoxLayout(acc2Left, BoxLayout.Y_AXIS));
        acc2Left.setBackground(Color.WHITE);
        acc2Left.add(acc2Title);
        acc2Left.add(acc2Number);

        account2.add(acc2Left, BorderLayout.WEST);
        account2.add(acc2Balance, BorderLayout.EAST);

        accountsPanel.add(account1);
        accountsPanel.add(account2);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(accountsPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTransactionsPanel() {
        JPanel panel = createCard(900, 400);
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Últimas Transações");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(30, 41, 59));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 0));

        // Table headers
        String[] headers = {"Data", "Descrição", "Valor", "Saldo"};
        JPanel headerPanel = new JPanel(new GridLayout(1, 4));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 10, 30));

        for (String header : headers) {
            JLabel headerLabel = new JLabel(header);
            headerLabel.setFont(new Font("Arial", Font.BOLD, 12));
            headerLabel.setForeground(new Color(100, 116, 139));
            headerPanel.add(headerLabel);
        }

        // Transactions data
        String[][] transactions = {
                {"15/06/2023", "Transferência recebida - Maria Santos", "+ MZN 1.500,00", "MZN 12.345,67"},
                {"14/06/2023", "Pagamento de serviços - Electricidade", "- MZN 850,50", "MZN 10.845,67"},
                {"12/06/2023", "Depósito em caixa automático", "+ MZN 2.000,00", "MZN 11.696,17"},
                {"10/06/2023", "Compra com cartão - Supermercado", "- MZN 1.250,35", "MZN 9.696,17"},
                {"08/06/2023", "Transferência para - Carlos Lima", "- MZN 500,00", "MZN 10.946,52"}
        };

        JPanel transactionsPanel = new JPanel(new GridLayout(transactions.length, 1, 0, 15));
        transactionsPanel.setBackground(Color.WHITE);
        transactionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        for (String[] transaction : transactions) {
            JPanel transactionRow = new JPanel(new GridLayout(1, 4));
            transactionRow.setBackground(Color.WHITE);

            for (int i = 0; i < transaction.length; i++) {
                JLabel cell = new JLabel(transaction[i]);
                cell.setFont(new Font("Arial", Font.PLAIN, 12));
                if (i == 2) {
                    // Color values
                    if (transaction[i].startsWith("+")) {
                        cell.setForeground(new Color(16, 185, 129)); // Green for positive
                    } else {
                        cell.setForeground(new Color(239, 68, 68)); // Red for negative
                    }
                } else if (i == 0) {
                    cell.setForeground(new Color(100, 116, 139)); // Gray for dates
                } else {
                    cell.setForeground(new Color(30, 41, 59)); // Dark for other text
                }
                transactionRow.add(cell);
            }
            transactionsPanel.add(transactionRow);
        }

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(headerPanel, BorderLayout.CENTER);
        panel.add(transactionsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createLimitsPanel() {
        JPanel panel = createCard(560, 300);
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Limites de Operação");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(30, 41, 59));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 0));

        JPanel limitsPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        limitsPanel.setBackground(Color.WHITE);
        limitsPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 40, 40));

        // Daily limit
        JPanel dailyLimit = createLimitCard(
                new Color(240, 249, 255),
                "Limite Diário de Levantamento",
                "Restante hoje",
                "MZN 3.250,00"
        );

        // Transfer limit
        JPanel transferLimit = createLimitCard(
                new Color(240, 253, 244),
                "Limite de Transferência",
                "Máximo por transação",
                "MZN 10.000,00"
        );

        limitsPanel.add(dailyLimit);
        limitsPanel.add(transferLimit);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(limitsPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createLimitCard(Color bgColor, String title, String subtitle, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(30, 41, 59));

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(100, 116, 139));

        JLabel valueLabel = new JLabel(value, JLabel.RIGHT);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 20));
        valueLabel.setForeground(new Color(30, 41, 59));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(bgColor);
        leftPanel.add(titleLabel);
        leftPanel.add(subtitleLabel);

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(valueLabel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createOffersPanel() {
        JPanel panel = createCard(560, 300);
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Ofertas Especiais");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(30, 41, 59));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 0));

        JPanel offersPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        offersPanel.setBackground(Color.WHITE);
        offersPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 40, 40));

        // Offer 1
        JPanel offer1 = createOfferCard(
                new Color(254, 247, 237),
                "Conta Poupança Plus",
                "Rendimento adicional de 1.5% a.a.",
                "Saber Mais",
                new Color(16, 185, 129)
        );

        // Offer 2
        JPanel offer2 = createOfferCard(
                new Color(239, 246, 255),
                "Cartão de Crédito Gold",
                "Cashback de 2% em todas as compras",
                "Aplicar",
                new Color(59, 130, 246)
        );

        offersPanel.add(offer1);
        offersPanel.add(offer2);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(offersPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createOfferCard(Color bgColor, String title, String subtitle, String buttonText, Color buttonColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(30, 41, 59));

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(100, 116, 139));

        JButton actionBtn = new JButton(buttonText);
        actionBtn.setFont(new Font("Arial", Font.BOLD, 12));
        actionBtn.setBackground(buttonColor);
        actionBtn.setForeground(Color.WHITE);
        actionBtn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        actionBtn.setFocusPainted(false);
        actionBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(bgColor);
        leftPanel.add(titleLabel);
        leftPanel.add(subtitleLabel);

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(actionBtn, BorderLayout.EAST);

        return panel;
    }

    private JPanel createContactsPanel() {
        JPanel panel = createCard(560, 300);
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Contactos Rápidos");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(30, 41, 59));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 0));

        JPanel contactsPanel = new JPanel(new GridLayout(3, 1, 20, 20));
        contactsPanel.setBackground(Color.WHITE);
        contactsPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 40, 40));

        // Contact 1
        JPanel contact1 = createContactCard("📞 Apoio ao Cliente: 84 123 4567", null);

        // Contact 2
        JPanel contact2 = createContactCard("🚨 Bloquear Cartão: 84 999 9999", null);

        // Contact 3
        JPanel contact3 = createContactCard("🏢 Encontrar Agências", "Ver Mapas");

        contactsPanel.add(contact1);
        contactsPanel.add(contact2);
        contactsPanel.add(contact3);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(contactsPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createContactCard(String text, String buttonText) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel contactLabel = new JLabel(text);
        contactLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        contactLabel.setForeground(new Color(30, 41, 59));

        panel.add(contactLabel, BorderLayout.WEST);

        if (buttonText != null) {
            JButton mapBtn = new JButton(buttonText);
            mapBtn.setFont(new Font("Arial", Font.BOLD, 12));
            mapBtn.setBackground(new Color(59, 130, 246));
            mapBtn.setForeground(Color.WHITE);
            mapBtn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
            mapBtn.setFocusPainted(false);
            mapBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            panel.add(mapBtn, BorderLayout.EAST);
        }

        return panel;
    }

    private JPanel createCard(int width, int height) {
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(width, height));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        return card;
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
}