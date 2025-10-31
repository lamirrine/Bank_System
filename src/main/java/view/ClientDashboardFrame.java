package view;

import model.entities.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ClientDashboardFrame extends JFrame {

    private final User loggedInUser;

    // Constantes de Estilo (Consistentes com o LoginFrame)
    private static final Color PRIMARY_COLOR = new Color(50, 150, 250); // Azul principal para botões/destaque
    private static final Color SIDEBAR_COLOR = new Color(30, 30, 50); // Fundo escuro para a sidebar
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Fundo claro da área principal

    public ClientDashboardFrame(User user) {
        this.loggedInUser = user;

        // 1. Configuração da Janela
        setTitle("Banco Digital - Dashboard | Cliente: " + user.getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);

        // Usamos BorderLayout para dividir o frame em Sidebar (WEST) e Conteúdo (CENTER)
        setLayout(new BorderLayout());

        // 2. Criar a Barra Lateral (Sidebar)
        JPanel sidebarPanel = createSidebar();
        add(sidebarPanel, BorderLayout.WEST);

        // 3. Criar a Área de Conteúdo Principal (Dashboard)
        JPanel contentPanel = createDashboardContent();
        add(contentPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }

    // =========================================================================
    // --- 2. IMPLEMENTAÇÃO DA SIDEBAR (Menu Lateral) ---
    // =========================================================================
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        // Usamos BoxLayout vertical para empilhar os elementos
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(200, 700)); // Largura fixa

        // Título/Logo
        JLabel logo = new JLabel("Banco Digital");
        logo.setFont(new Font("Arial", Font.BOLD, 18));
        logo.setForeground(Color.WHITE);
        logo.setBorder(new EmptyBorder(20, 10, 30, 10)); // Padding
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(logo);

        // Navegação (Botões/Links)
        sidebar.add(createSidebarButton("Início", true)); // Botão ativo [cite: 4]
        sidebar.add(createSidebarButton("Depósito", false)); // [cite: 6]
        sidebar.add(createSidebarButton("Levantamento", false)); // [cite: 7]
        sidebar.add(createSidebarButton("Transferência", false)); // [cite: 9]
        sidebar.add(createSidebarButton("Extrato", false)); // [cite: 12]

        // Perfil do Usuário
        sidebar.add(Box.createVerticalGlue()); // Empurra o perfil para o fundo
        sidebar.add(createProfilePanel());

        return sidebar;
    }

    private JButton createSidebarButton(String text, boolean isActive) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // Estica a largura
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);

        // Estilo baseado na atividade
        if (isActive) {
            button.setBackground(PRIMARY_COLOR);
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(SIDEBAR_COLOR);
            button.setForeground(Color.LIGHT_GRAY);
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(40, 40, 70));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(SIDEBAR_COLOR);
                }
            });
        }

        return button;
    }

    private JPanel createProfilePanel() {
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        profilePanel.setBackground(new Color(40, 40, 70)); // Cor ligeiramente diferente
        profilePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Letras Iniciais (JS para João Silva) [cite: 57]
        JLabel initials = new JLabel(loggedInUser.getFirstName().substring(0, 1) + loggedInUser.getLastName().substring(0, 1));
        initials.setFont(new Font("Arial", Font.BOLD, 18));
        initials.setForeground(Color.WHITE);

        // Nome e Conta
        JLabel nameLabel = new JLabel(loggedInUser.getFullName()); // [cite: 56]
        nameLabel.setForeground(Color.WHITE);

        JButton logoutButton = new JButton("Sair"); // [cite: 59]
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 12));
        logoutButton.setBackground(Color.RED);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "A sair...");
            new LoginFrame(null).setVisible(true); // Reabre o login (necessário passar o authService real)
            dispose();
        });

        profilePanel.add(initials);
        profilePanel.add(nameLabel);
        profilePanel.add(logoutButton);

        return profilePanel;
    }

    // =========================================================================
    // --- 3. IMPLEMENTAÇÃO DA ÁREA PRINCIPAL (Dashboard Content) ---
    // =========================================================================
    private JPanel createDashboardContent() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(BACKGROUND_COLOR);
        content.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Título e Subtítulo (Topo da área de conteúdo)
        JLabel header = new JLabel("Dashboard Principal", SwingConstants.LEFT);
        header.setFont(new Font("Arial", Font.BOLD, 28));

        // Adicionamos o cabeçalho ao Norte (topo)
        content.add(header, BorderLayout.NORTH);

        // Grid para o corpo do dashboard (Centro)
        JPanel bodyPanel = new JPanel(new GridLayout(2, 2, 20, 20)); // Grid 2x2 para cartões
        bodyPanel.setBackground(BACKGROUND_COLOR);

        // 1. Cartão Saldo Disponível
        bodyPanel.add(createBalanceCard()); // [cite: 3, 5]

        // 2. Cartão Limites de Operação
        bodyPanel.add(createLimitsCard()); // [cite: 23]

        // 3. Cartão Últimas Transações
        bodyPanel.add(createTransactionHistoryCard()); // [cite: 27]

        // 4. Cartão Contas
        bodyPanel.add(createAccountsCard()); // [cite: 17]

        content.add(bodyPanel, BorderLayout.CENTER);

        return content;
    }

    // --- Métodos de Cartão (Placeholders) ---

    private JPanel createBalanceCard() {
        // Simulação do card de Saldo Disponível [cite: 3, 5]
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createTitledBorder("Saldo Disponível"));
        card.setBackground(Color.WHITE);

        JLabel balance = new JLabel("MZN 12.345,67", SwingConstants.CENTER); // [cite: 5]
        balance.setFont(new Font("Arial", Font.BOLD, 30));

        card.add(balance, BorderLayout.CENTER);

        return card;
    }

    private JPanel createLimitsCard() {
        // Simulação do card de Limites de Operação [cite: 23]
        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBorder(BorderFactory.createTitledBorder("Limites de Operação")); // [cite: 23]
        card.setBackground(Color.WHITE);

        card.add(new JLabel("  Limite Diário de Levantamento: MZN 3.250,00")); // [cite: 24]
        card.add(new JLabel("  Limite de Transferência: MZN 10.000,00")); // [cite: 25, 26]

        return card;
    }

    private JPanel createTransactionHistoryCard() {
        // Simulação do card Últimas Transações [cite: 27]
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createTitledBorder("Últimas Transações")); // [cite: 27]
        card.setBackground(Color.WHITE);

        // Simular histórico [cite: 34, 35, 36, 38, 39, 40]
        JTextArea history = new JTextArea();
        history.setText("15/06/2023 - Transferência recebida: +MZN 1.500,00\n" + "[cite: 34, 35, 36]\n" +
                "14/06/2023 - Pagamento Electricidade: -MZN 850,50\n" + "[cite: 38, 39, 40]");
        history.setEditable(false);
        card.add(new JScrollPane(history), BorderLayout.CENTER);

        return card;
    }

    private JPanel createAccountsCard() {
        // Simulação do card Minhas Contas [cite: 17]
        JPanel card = new JPanel(new GridLayout(3, 1));
        card.setBorder(BorderFactory.createTitledBorder("Minhas Contas")); // [cite: 17]
        card.setBackground(Color.WHITE);

        card.add(new JLabel("  Conta Corrente 12345-X: MZN 5.000,00")); // [cite: 19, 20]
        card.add(new JLabel("  Conta Poupança 67890-Y: MZN 7.345,67")); // [cite: 21, 22]

        return card;
    }

    // --- FIM DOS MÉTODOS DE CARTÃO ---
}