package view;

import model.entities.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientDashboardFrame extends JFrame {

    private final User loggedInUser;
    private final JPanel mainContentPanel; // Painel que muda de conteúdo (Início, Depósito, etc.)
    private final JLabel headerLabel;    // Campo para o título que será atualizado

    // Constantes de Estilo
    private static final Color PRIMARY_COLOR = new Color(50, 150, 250);
    private static final Color SIDEBAR_COLOR = new Color(30, 30, 50);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);

    public ClientDashboardFrame(User user) {
        this.loggedInUser = user;

        // 1. Configuração da Janela
        setTitle("Banco Digital - Dashboard | Cliente: " + user.getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);

        // 2. Layout Principal (BorderLayout)
        setLayout(new BorderLayout());

        // 3. Sidebar (WEST) - Largura fixa
        JPanel sidebarPanel = createSidebar();
        add(sidebarPanel, BorderLayout.WEST);

        // 4. Content Container (CENTER) - Painel que se expande
        JPanel contentContainer = new JPanel(new BorderLayout());
        contentContainer.setBackground(BACKGROUND_COLOR);
        contentContainer.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Título Fixo (Inicialização do campo)
        headerLabel = new JLabel("Dashboard Principal", SwingConstants.LEFT);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        contentContainer.add(headerLabel, BorderLayout.NORTH);

        // Painel Mutável de Conteúdo (Inicialização do campo)
        mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(BACKGROUND_COLOR);
        mainContentPanel.add(createDashboardBody(), BorderLayout.CENTER);

        contentContainer.add(mainContentPanel, BorderLayout.CENTER);
        add(contentContainer, BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }

    // =========================================================================
    // --- SIDEBAR (Largura Fixa) ---
    // =========================================================================
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(220, 700));

        JLabel logo = new JLabel("Banco Digital");
        logo.setFont(new Font("Arial", Font.BOLD, 20));
        logo.setForeground(Color.WHITE);
        logo.setBorder(new EmptyBorder(20, 10, 30, 10));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(logo);

        // Botões de Navegação
        sidebar.add(createSidebarButton("Início", true, "HOME"));
        sidebar.add(createSidebarButton("Depósito", false, "DEPOSIT"));
        sidebar.add(createSidebarButton("Levantamento", false, "WITHDRAW"));
        sidebar.add(createSidebarButton("Transferência", false, "TRANSFER"));
        sidebar.add(createSidebarButton("Extrato", false, "STATEMENT"));

        // Perfil do Usuário
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(createProfilePanel());

        return sidebar;
    }

    private JButton createSidebarButton(String text, boolean isActive, String command) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setActionCommand(command);

        // Estilo
        if (isActive) {
            button.setBackground(PRIMARY_COLOR);
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(SIDEBAR_COLOR);
            button.setForeground(Color.LIGHT_GRAY);
        }

        // Listener de Navegação
        button.addActionListener(e -> {
            switchPanel(e.getActionCommand());
        });

        return button;
    }

    private JPanel createProfilePanel() {
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        profilePanel.setBackground(new Color(40, 40, 70));
        profilePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel initials = new JLabel(loggedInUser.getFirstName().substring(0, 1) + loggedInUser.getLastName().substring(0, 1));
        initials.setFont(new Font("Arial", Font.BOLD, 18));
        initials.setForeground(Color.WHITE);

        JLabel nameLabel = new JLabel(loggedInUser.getFullName());
        nameLabel.setForeground(Color.WHITE);

        JButton logoutButton = new JButton("Sair");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 12));
        logoutButton.setBackground(Color.RED);
        logoutButton.setForeground(Color.WHITE);

        profilePanel.add(initials);
        profilePanel.add(nameLabel);

        // Ação de Sair
        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Sessão Terminada. Redirecionando para Login.");
            dispose();
            // A lógica de reinjeção dos serviços deve estar no App.java
            // Exemplo: new LoginFrame(authService, customerService).setVisible(true);
        });

        profilePanel.add(logoutButton);

        return profilePanel;
    }

    // =========================================================================
    // --- ÁREA PRINCIPAL: DASHBOARD HOME ---
    // =========================================================================
    private JPanel createDashboardBody() {
        JPanel bodyPanel = new JPanel(new GridBagLayout());
        bodyPanel.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;

        // Linha 0: Cartão de Saldo e Limites
        gbc.gridy = 0;
        gbc.weighty = 0.4;

        gbc.gridx = 0;
        gbc.weightx = 0.5;
        bodyPanel.add(createBalanceCard(), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.5;
        bodyPanel.add(createLimitsCard(), gbc);

        // Linha 1: Cartão de Transações e Contas
        gbc.gridy = 1;
        gbc.weighty = 0.6;

        gbc.gridx = 0;
        gbc.weightx = 0.5;
        bodyPanel.add(createTransactionHistoryCard(), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.5;
        bodyPanel.add(createAccountsCard(), gbc);

        return bodyPanel;
    }

    // --- Métodos de Cartão (Placeholders) ---

    private JPanel createBalanceCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createTitledBorder(
                        BorderFactory.createEmptyBorder(),
                        "Saldo Disponível",
                        javax.swing.border.TitledBorder.LEADING,
                        javax.swing.border.TitledBorder.TOP,
                        new Font("Arial", Font.BOLD, 14),
                        PRIMARY_COLOR)
        ));
        card.setBackground(Color.WHITE);

        JLabel balance = new JLabel("MZN 12.345,67", SwingConstants.CENTER);
        balance.setFont(new Font("Arial", Font.BOLD, 36));
        balance.setBorder(new EmptyBorder(20, 0, 20, 0));

        card.add(balance, BorderLayout.CENTER);

        return card;
    }

    private JPanel createLimitsCard() {
        JPanel card = new JPanel(new GridLayout(2, 1, 5, 5));
        card.setBorder(BorderFactory.createTitledBorder("Limites de Operação"));
        card.setBackground(Color.WHITE);

        card.add(new JLabel("  • Limite Diário de Levantamento: MZN 3.250,00"));
        card.add(new JLabel("  • Limite de Transferência: MZN 10.000,00"));

        return card;
    }

    private JPanel createTransactionHistoryCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createTitledBorder("Últimas Transações"));
        card.setBackground(Color.WHITE);

        JTextArea history = new JTextArea();
        history.setText("15/06/2023 - Transferência recebida: +MZN 1.500,00\n" +
                "14/06/2023 - Pagamento Electricidade: -MZN 850,50\n" +
                "12/06/2023 - Depósito em caixa: +MZN 2.000,00");
        history.setEditable(false);
        card.add(new JScrollPane(history), BorderLayout.CENTER);

        return card;
    }

    private JPanel createAccountsCard() {
        JPanel card = new JPanel(new GridLayout(3, 1));
        card.setBorder(BorderFactory.createTitledBorder("Minhas Contas"));
        card.setBackground(Color.WHITE);

        card.add(new JLabel("  Conta Corrente 12345-X: MZN 5.000,00"));
        card.add(new JLabel("  Conta Poupança 67890-Y: MZN 7.345,67"));

        return card;
    }

    // =========================================================================
    // --- LÓGICA DE NAVEGAÇÃO ---
    // =========================================================================
    private void switchPanel(String command) {
        // 1. Limpa o painel de conteúdo
        mainContentPanel.removeAll();

        String newTitle = "";
        JPanel newPanel;

        switch (command) {
            case "HOME":
                newPanel = createDashboardBody();
                newTitle = "Dashboard Principal";
                break;
            case "DEPOSIT":
                // DepositPanel já deve existir
                newPanel = new DepositPanel();
                newTitle = "Realizar Depósito";
                break;
            case "TRANSFER":
                // TransferPanel já deve existir
                newPanel = new TransferPanel();
                newTitle = "Realizar Transferência";
                break;
            case "WITHDRAW":
                // WithdrawPanel já deve existir
                newPanel = new WithdrawPanel();
                newTitle = "Realizar Levantamento";
                break;
            case "STATEMENT":
                // StatementPanel (Ainda não criado)
                newPanel = createPlaceholderPanel("Placeholder: Formulário de Extrato");
                newTitle = "Extrato de Conta";
                break;

            default:
                newPanel = createPlaceholderPanel("Página não encontrada");
                newTitle = "Erro";
        }

        // 2. Atualiza o título (Usa o campo da classe para evitar casting)
        headerLabel.setText(newTitle);

        // 3. Adiciona o novo painel e redesenha
        mainContentPanel.add(newPanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    // --- MÉTODO AUXILIAR PARA CRIAR PLACEHOLDERS (Correção do erro de tipagem) ---
    private JPanel createPlaceholderPanel(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.ITALIC, 20));
        label.setForeground(Color.GRAY);
        panel.add(label, BorderLayout.CENTER);
        panel.setBackground(BACKGROUND_COLOR);
        return panel;
    }
}