package view;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import model.entities.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class HomepageView extends JPanel {
    private JLabel balanceLabel;
    private JLabel welcomeLabel;
    private JPanel accountsPanel;
    private JPanel transactionsPanel;

    public HomepageView() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(229, 231, 235));

        // Header
        add(createHeader(), BorderLayout.NORTH);

        // Main content with scroll
        JScrollPane scrollPane = createMainContent();
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new MigLayout("fill, insets 0 40 0 40", "[grow]", "[]"));
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(100, 80));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));

        JLabel titleLabel = new JLabel("Visão Geral");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(15, 23, 42));

        header.add(titleLabel, "grow");

        return header;
    }

    private JScrollPane createMainContent() {
        JPanel content = new JPanel(new MigLayout("fillx, wrap, insets 25 40 40 40", "[grow]", "[]20[]20[]20[]"));
        content.setBackground(new Color(229, 231, 235));

        // Welcome section
        content.add(createWelcomeSection(), "growx");

        // Balance card
        content.add(createBalanceCard(), "growx");

        // Main content area
        content.add(createMainContentArea(), "grow, push");

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    private JPanel createWelcomeSection() {
        JPanel panel = new JPanel(new MigLayout("fillx", "[grow]", "[]"));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JPanel textPanel = new JPanel(new MigLayout("wrap, insets 0", "[grow]", "[]5[]"));
        textPanel.setBackground(Color.WHITE);

        welcomeLabel = new JLabel("Olá, Utilizador! 👋");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(15, 23, 42));

        JLabel dateLabel = new JLabel(getCurrentDate());
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setForeground(new Color(107, 114, 128));

        textPanel.add(welcomeLabel, "growx");
        textPanel.add(dateLabel, "growx");

        panel.add(textPanel, "grow");

        return panel;
    }

    private String getCurrentDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE, dd 'de' MMMM yyyy", new java.util.Locale("pt", "PT"));
        return sdf.format(new java.util.Date());
    }

    private JPanel createBalanceCard() {
        JPanel panel = createCard(new Color(59, 130, 246));
        panel.setLayout(new MigLayout("fill, insets 25 30 25 30", "[grow]0[]", "center"));

        JPanel leftPanel = new JPanel(new MigLayout("wrap, insets 0", "[grow]", "[]10[]"));
        leftPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Saldo Disponível");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(243, 244, 246));

        balanceLabel = new JLabel("MZN 0,00");
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        balanceLabel.setForeground(Color.WHITE);

        leftPanel.add(titleLabel, "growx");
        leftPanel.add(balanceLabel, "growx");

        JButton detailsBtn = new JButton("Ver Detalhes");
        detailsBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        detailsBtn.setForeground(Color.WHITE);
        detailsBtn.setBackground(new Color(37, 99, 235));
        detailsBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        detailsBtn.setFocusPainted(false);
        detailsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efeito hover no botão
        detailsBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                detailsBtn.setBackground(new Color(29, 78, 216));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                detailsBtn.setBackground(new Color(37, 99, 235));
            }
        });

        panel.add(leftPanel, "growx, wmax 60%, wmin 0");
        panel.add(detailsBtn, "gapleft 30, w 150!, h 50!");

        return panel;
    }

    private JPanel createMainContentArea() {
        JPanel panel = new JPanel(new MigLayout("fill", "[grow 95]15[grow 25]", "[grow]"));
        panel.setBackground(new Color(229, 231, 235));

        // Left column
        JPanel leftColumn = new JPanel(new MigLayout("wrap, fill", "[grow]", "[]20[]"));
        leftColumn.setBackground(new Color(229, 231, 235));

        leftColumn.add(createAccountsPanel(), "growx");
        leftColumn.add(createTransactionsPanel(), "grow, push");

        // Right column
        JPanel rightColumn = createCard();
        rightColumn.setLayout(new MigLayout("wrap, fill", "[grow]", "[]20[]20[]"));
        rightColumn.setBackground(Color.WHITE);

        rightColumn.add(createLimitsPanel(), "growx");

        panel.add(leftColumn, "grow, wmax 80%");
        panel.add(rightColumn, "grow");

        return panel;
    }

    private JPanel createAccountsPanel() {
        JPanel panel = createCard();
        panel.setLayout(new MigLayout("fill, wrap, insets 20", "[grow]", "[]20[grow]"));

        JLabel titleLabel = new JLabel("Minhas Contas");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(15, 23, 42));

        accountsPanel = new JPanel(new MigLayout("fillx, wrap, insets 0", "[grow, fill]", "[]10[]"));
        accountsPanel.setOpaque(false);

        // Placeholder inicial
        JLabel loadingLabel = new JLabel("Carregando contas...", JLabel.CENTER);
        loadingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loadingLabel.setForeground(new Color(107, 114, 128));
        accountsPanel.add(loadingLabel, "grow, h 100!");

        panel.add(titleLabel, "growx");
        panel.add(accountsPanel, "growx");

        return panel;
    }

    private JPanel createTransactionsPanel() {
        JPanel panel = createCard();
        panel.setLayout(new BorderLayout(0, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Últimas Transações");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(15, 23, 42));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        panel.add(titleLabel, BorderLayout.NORTH);

        // Painel principal com header e conteúdo
        JPanel contentPanel = new JPanel(new BorderLayout());

        // Header das colunas
        contentPanel.add(createTransactionsHeader(), BorderLayout.NORTH);

        // Área das transações com scroll
        transactionsPanel = createTransactionsContent();
        JScrollPane scrollPane = new JScrollPane(transactionsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        contentPanel.add(scrollPane, BorderLayout.CENTER);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTransactionsHeader() {
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(new Color(248, 250, 252));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 15);

        // Data - 20%
        gbc.gridx = 0;
        gbc.weightx = 0.2;
        JLabel dateHeader = new JLabel("Data");
        dateHeader.setFont(new Font("Segoe UI", Font.BOLD, 12));
        dateHeader.setForeground(new Color(107, 114, 128));
        headerPanel.add(dateHeader, gbc);

        // Descrição - 45%
        gbc.gridx = 1;
        gbc.weightx = 0.45;
        JLabel descHeader = new JLabel("Descrição");
        descHeader.setFont(new Font("Segoe UI", Font.BOLD, 12));
        descHeader.setForeground(new Color(107, 114, 128));
        headerPanel.add(descHeader, gbc);

        // Valor - 17.5%
        gbc.gridx = 2;
        gbc.weightx = 0.175;
        gbc.insets = new Insets(0, 0, 0, 15);
        JLabel amountHeader = new JLabel("Valor");
        amountHeader.setFont(new Font("Segoe UI", Font.BOLD, 12));
        amountHeader.setForeground(new Color(107, 114, 128));
        amountHeader.setHorizontalAlignment(SwingConstants.RIGHT);
        headerPanel.add(amountHeader, gbc);

        // Saldo - 17.5%
        gbc.gridx = 3;
        gbc.weightx = 0.175;
        gbc.insets = new Insets(0, 0, 0, 0);
        JLabel balanceHeader = new JLabel("Saldo");
        balanceHeader.setFont(new Font("Segoe UI", Font.BOLD, 12));
        balanceHeader.setForeground(new Color(107, 114, 128));
        balanceHeader.setHorizontalAlignment(SwingConstants.RIGHT);
        headerPanel.add(balanceHeader, gbc);

        return headerPanel;
    }

    private JPanel createTransactionsContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        // Placeholder inicial
        JLabel loadingLabel = new JLabel("Carregando transações...", JLabel.CENTER);
        loadingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loadingLabel.setForeground(new Color(107, 114, 128));
        loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadingLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
        panel.add(loadingLabel);

        return panel;
    }

    private JPanel createTransactionRow(Transaction transaction) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(243, 244, 246)),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 15);

        // Data - 20%
        gbc.gridx = 0;
        gbc.weightx = 0.2;
        JLabel dateLabel = new JLabel(formatDate(transaction.getTimestamp()));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(107, 114, 128));
        row.add(dateLabel, gbc);

        // Descrição - 45%
        gbc.gridx = 1;
        gbc.weightx = 0.45;
        JLabel descLabel = new JLabel(transaction.getDescription() != null ?
                transaction.getDescription() : "Transação");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(new Color(15, 23, 42));
        row.add(descLabel, gbc);

        // Valor - 17.5%
        gbc.gridx = 2;
        gbc.weightx = 0.175;
        gbc.insets = new Insets(0, 0, 0, 15);
        String amountText = String.format("%s MZN %,.2f",
                transaction.getAmount() >= 0 ? "+" : "-",
                Math.abs(transaction.getAmount()));
        JLabel amountLabel = new JLabel(amountText);
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        amountLabel.setForeground(transaction.getAmount() >= 0 ?
                new Color(16, 185, 129) : new Color(239, 68, 68));
        amountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        row.add(amountLabel, gbc);

        // Saldo - 17.5%
        gbc.gridx = 3;
        gbc.weightx = 0.175;
        gbc.insets = new Insets(0, 0, 0, 0);
        JLabel balanceLabel = new JLabel(String.format("MZN %,.2f", transaction.getResultingBalance()));
        balanceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        balanceLabel.setForeground(new Color(15, 23, 42));
        balanceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        row.add(balanceLabel, gbc);

        return row;
    }

    public void updateTransactions(List<Transaction> transactions) {
        System.out.println("Atualizando transações no HomepageView: " + (transactions != null ? transactions.size() : "null"));

        transactionsPanel.removeAll();
        transactionsPanel.setLayout(new BoxLayout(transactionsPanel, BoxLayout.Y_AXIS));
        transactionsPanel.setBackground(Color.WHITE);

        if (transactions == null || transactions.isEmpty()) {
            JLabel noTransactionsLabel = new JLabel("Nenhuma transação recente", JLabel.CENTER);
            noTransactionsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            noTransactionsLabel.setForeground(new Color(107, 114, 128));
            noTransactionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            noTransactionsLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
            transactionsPanel.add(noTransactionsLabel);
        } else {
            for (Transaction transaction : transactions) {
                JPanel transactionRow = createTransactionRow(transaction);
                transactionRow.setAlignmentX(Component.LEFT_ALIGNMENT);
                transactionsPanel.add(transactionRow);
            }
        }

        transactionsPanel.revalidate();
        transactionsPanel.repaint();
    }

    private JPanel createLimitsPanel() {
        JPanel panel = createCard();
        panel.setLayout(new MigLayout("fill, wrap, insets 20", "[grow]", "[]20[]"));

        JLabel titleLabel = new JLabel("Limites de Operação");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(15, 23, 42));

        JPanel limitsContent = new JPanel(new MigLayout("wrap, fill, insets 0", "[grow]", "[]15[]"));
        limitsContent.setOpaque(false);

        // Daily limit
        JPanel dailyLimitPanel = createLimitCard(
                new Color(230, 239, 255),
                "Limite Diário de Levantamento",
                "Restante hoje",
                "MZN 3.250,00"
        );

        // Transfer limit
        JPanel transferLimitPanel = createLimitCard(
                new Color(230, 255, 234),
                "Limite de Transferência",
                "Máximo por transação",
                "MZN 10.000,00"
        );

        limitsContent.add(dailyLimitPanel, "growx");
        limitsContent.add(transferLimitPanel, "growx");

        panel.add(titleLabel, "growx");
        panel.add(limitsContent, "growx");

        return panel;
    }

    private JPanel createLimitCard(Color bgColor, String title, String subtitle, String value) {
        JPanel panel = new JPanel(new MigLayout("fill, insets 15", "[grow]", "[]5[]"));
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200, 50)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(15, 23, 42));

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(107, 114, 128));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(new Color(15, 23, 42));

        panel.add(titleLabel, "growx, wrap");
        panel.add(subtitleLabel, "growx, split 2");
        panel.add(valueLabel, "gapleft push");

        return panel;
    }

    // MÉTODOS PARA ATUALIZAR DADOS
    public void updateUserInfo(String firstName) {
        if (welcomeLabel != null) {
            welcomeLabel.setText("Olá, " + firstName + "! 👋");
        }
    }

    public void updateAccounts(List<Account> accounts) {
        System.out.println("Atualizando contas no HomepageView: " + (accounts != null ? accounts.size() : "null"));

        accountsPanel.removeAll();
        accountsPanel.setLayout(new MigLayout("fillx, wrap, insets 0", "[grow, fill]", "[]10[]"));

        if (accounts == null || accounts.isEmpty()) {
            JLabel noAccountsLabel = new JLabel("Nenhuma conta encontrada", JLabel.CENTER);
            noAccountsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            noAccountsLabel.setForeground(new Color(107, 114, 128));
            accountsPanel.add(noAccountsLabel, "grow, h 100!");
        } else {
            for (Account account : accounts) {
                JPanel accountCard = createAccountCard(account);
                accountsPanel.add(accountCard, "growx");
            }
        }

        accountsPanel.revalidate();
        accountsPanel.repaint();
    }

    private JPanel createAccountCard(Account account) {
        JPanel card = new JPanel(new MigLayout("fill, insets 15", "[grow]", "[]5[]"));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        String accountType = account.getAccountType() != null ?
                account.getAccountType().toString() : "CORRENTE";
        String typeDisplay = accountType.equals("CORRENTE") ? "Conta Corrente" : "Conta Poupança";

        JLabel titleLabel = new JLabel(typeDisplay);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(15, 23, 42));

        JLabel numberLabel = new JLabel(account.getAccountNumber() != null ?
                account.getAccountNumber() : "N/A");
        numberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        numberLabel.setForeground(new Color(107, 114, 128));

        JLabel balanceLabel = new JLabel(String.format("MZN %,.2f", account.getBalance()));
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        balanceLabel.setForeground(new Color(15, 23, 42));

        JPanel leftPanel = new JPanel(new MigLayout("wrap, insets 0", "[grow]", "[]5[]"));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.add(titleLabel, "growx");
        leftPanel.add(numberLabel, "growx");

        card.add(leftPanel, "grow");
        card.add(balanceLabel, "top");

        return card;
    }

    // MÉTODO SIMPLES PARA ATUALIZAR LIMITES
    // MÉTODO SIMPLES PARA ATUALIZAR LIMITES
    public void updateLimits(double dailyWithdrawLimit, double dailyTransferLimit) {
        System.out.println("Atualizando limites: " + dailyWithdrawLimit + ", " + dailyTransferLimit);

        // Buscar todos os labels que contêm "MZN" nos cards de limite
        List<JLabel> limitValueLabels = findAllLimitValueLabels();

        System.out.println("Labels de limite encontrados: " + limitValueLabels.size());

        if (limitValueLabels.size() >= 2) {
            // Primeiro label é limite de levantamento, segundo é limite de transferência
            String withdrawText = String.format("MZN %,.2f", dailyWithdrawLimit);
            String transferText = String.format("MZN %,.2f", dailyTransferLimit);

            limitValueLabels.get(0).setText(withdrawText);
            limitValueLabels.get(1).setText(transferText);

            System.out.println("Limites atualizados: " + withdrawText + ", " + transferText);
        } else {
            System.out.println("Não foram encontrados labels de limite suficientes: " + limitValueLabels.size());
            // Debug: mostrar quais labels foram encontrados
            for (int i = 0; i < limitValueLabels.size(); i++) {
                System.out.println("Label " + i + ": " + limitValueLabels.get(i).getText());
            }
        }
    }

    private List<JLabel> findAllLimitValueLabels() {
        List<JLabel> limitLabels = new ArrayList<>();
        findLimitValueLabelsRecursive(this, limitLabels);

        // Filtrar apenas os labels que estão nos cards coloridos de limite
        List<JLabel> filteredLabels = new ArrayList<>();
        for (JLabel label : limitLabels) {
            Container parent = label.getParent();
            while (parent != null && !(parent instanceof JPanel)) {
                parent = parent.getParent();
            }
            if (parent instanceof JPanel) {
                Color bg = ((JPanel) parent).getBackground();
                // Verificar se é um card colorido (limite) - azul claro ou verde claro
                if (bg != null &&
                        (bg.equals(new Color(230, 239, 255)) ||  // Azul claro do limite diário
                                bg.equals(new Color(230, 255, 234)))) { // Verde claro do limite de transferência
                    filteredLabels.add(label);
                }
            }
        }

        System.out.println("Labels filtrados (cards coloridos): " + filteredLabels.size());
        return filteredLabels;
    }

    private void findLimitValueLabelsRecursive(Container container, List<JLabel> limitLabels) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                // Verificar se é um label de valor de limite (contém "MZN" e está em card colorido)
                if (label.getText() != null && label.getText().contains("MZN")) {
                    Container parent = label.getParent();
                    while (parent != null && !(parent instanceof JPanel)) {
                        parent = parent.getParent();
                    }
                    if (parent instanceof JPanel) {
                        Color bg = ((JPanel) parent).getBackground();
                        // Verificar se é um card colorido (limite)
                        if (bg != null && !bg.equals(Color.WHITE) &&
                                !bg.equals(new Color(229, 231, 235)) &&
                                !bg.equals(new Color(248, 250, 252))) {
                            limitLabels.add(label);
                        }
                    }
                }
            } else if (comp instanceof Container) {
                findLimitValueLabelsRecursive((Container) comp, limitLabels);
            }
        }
    }

    public void setBalance(double balance) {
        if (balanceLabel != null) {
            balanceLabel.setText(String.format("MZN %,.2f", balance));
        }
    }

    private String formatDate(java.util.Date date) {
        if (date == null) return "N/A";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    // Métodos auxiliares para criar cards
    private JPanel createCard(Color backgroundColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (backgroundColor != null) {
                    g2.setColor(backgroundColor);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                } else {
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                }
            }
        };
        card.setOpaque(false);
        return card;
    }

    private JPanel createCard() {
        return createCard(null);
    }
}