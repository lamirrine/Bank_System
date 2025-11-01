// view/ClientDashboardView.java
package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ClientDashboardView extends JFrame {
    private JLabel welcomeLabel;
    private JLabel balanceLabel;
    private JButton depositButton, withdrawButton, transferButton, statementButton, logoutButton;
    private JTable recentTransactionsTable;

    public ClientDashboardView(String clientName) {
        initializeUI(clientName);
    }

    private void initializeUI(String clientName) {
        setTitle("Banco Digital - Dashboard do Cliente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        // Layout principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Cabeçalho
        JPanel headerPanel = createHeaderPanel(clientName);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Corpo
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(createLeftPanel());
        splitPane.setRightComponent(createRightPanel());
        splitPane.setDividerLocation(600);

        mainPanel.add(splitPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createHeaderPanel(String clientName) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Saudação
        JPanel greetingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        welcomeLabel = new JLabel("Bem-vindo, " + clientName + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        greetingPanel.add(welcomeLabel);

        // Botão logout
        logoutButton = new JButton("Sair");
        logoutButton.setBackground(new Color(204, 0, 0));
        logoutButton.setForeground(Color.WHITE);

        panel.add(greetingPanel, BorderLayout.WEST);
        panel.add(logoutButton, BorderLayout.EAST);

        return panel;
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Saldo e ações rápidas
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        // Saldo
        JPanel balancePanel = new JPanel();
        balancePanel.setLayout(new BoxLayout(balancePanel, BoxLayout.Y_AXIS));
        balancePanel.setBorder(BorderFactory.createTitledBorder("Saldo Disponível"));
        balanceLabel = new JLabel("MZN 12.345,67");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 28));
        balanceLabel.setForeground(new Color(0, 102, 0));
        balanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        balancePanel.add(balanceLabel);

        // Ações rápidas
        JPanel actionsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        actionsPanel.setBorder(BorderFactory.createTitledBorder("Ações Rápidas"));

        depositButton = createActionButton("Depositar", new Color(0, 102, 204));
        withdrawButton = createActionButton("Levantar", new Color(255, 153, 0));
        transferButton = createActionButton("Transferir", new Color(0, 153, 76));
        statementButton = createActionButton("Extrato", new Color(102, 102, 102));

        actionsPanel.add(depositButton);
        actionsPanel.add(withdrawButton);
        actionsPanel.add(transferButton);
        actionsPanel.add(statementButton);

        topPanel.add(balancePanel, BorderLayout.NORTH);
        topPanel.add(actionsPanel, BorderLayout.CENTER);

        // Transações recentes
        JPanel transactionsPanel = new JPanel(new BorderLayout());
        transactionsPanel.setBorder(BorderFactory.createTitledBorder("Últimas Transações"));

        String[] columns = {"Data", "Descrição", "Valor", "Saldo"};
        Object[][] data = {
                {"15/06/2023", "Transferência recebida - Maria Santos", "+ MZN 1.000,90", "MZN 12.345,67"},
                {"14/06/2023", "Pagamento de serviços - Electricidade", "- MZN 88,50", "MZN 10.845,67"}
        };

        recentTransactionsTable = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(recentTransactionsTable);

        transactionsPanel.add(scrollPane);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(transactionsPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));

        // Minhas contas
        JPanel accountsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        accountsPanel.setBorder(BorderFactory.createTitledBorder("Minhas Contas"));
        accountsPanel.add(new JLabel("Conta Corrente"));
        accountsPanel.add(new JLabel("MZN 5.000,00"));
        accountsPanel.add(new JLabel("Conta Poupança 6789/97"));
        accountsPanel.add(new JLabel("MZN 7.345,67"));

        // Limites
        JPanel limitsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        limitsPanel.setBorder(BorderFactory.createTitledBorder("Limites de Operação"));
        limitsPanel.add(new JLabel("Limite Diário de Levantamento:"));
        limitsPanel.add(new JLabel("MZN 3.250,00"));
        limitsPanel.add(new JLabel("Restante Disponível Hoje:"));
        limitsPanel.add(new JLabel("MZN 2.750,00"));
        limitsPanel.add(new JLabel("Limite de Transferência:"));
        limitsPanel.add(new JLabel("MZN 10.000,00"));

        // Contactos
        JPanel contactsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        contactsPanel.setBorder(BorderFactory.createTitledBorder("Contactos Rápidos"));
        contactsPanel.add(createContactLabel("📞 Apoio ao Cliente: 84 123 4567"));
        contactsPanel.add(createContactLabel("🔒 Bloquear Cartão: 84 999 9999"));
        contactsPanel.add(createContactLabel("🏦 Encontrar Agências"));
        contactsPanel.add(createContactLabel("ℹ️ Ver Mais"));

        panel.add(accountsPanel);
        panel.add(limitsPanel);
        panel.add(contactsPanel);

        return panel;
    }

    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JLabel createContactLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(0, 102, 204));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return label;
    }

    // Métodos públicos para controle
    public void setBalance(double balance) {
        balanceLabel.setText(String.format("MZN %,.2f", balance));
    }

    public void setWelcomeMessage(String name) {
        welcomeLabel.setText("Bem-vindo, " + name + "!");
    }

    // Listeners
    public void addDepositListener(ActionListener listener) {
        depositButton.addActionListener(listener);
    }

    public void addWithdrawListener(ActionListener listener) {
        withdrawButton.addActionListener(listener);
    }

    public void addTransferListener(ActionListener listener) {
        transferButton.addActionListener(listener);
    }

    public void addStatementListener(ActionListener listener) {
        statementButton.addActionListener(listener);
    }

    public void addLogoutListener(ActionListener listener) {
        logoutButton.addActionListener(listener);
    }
}