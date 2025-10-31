package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TransferPanel extends JPanel {

    private static final Color PRIMARY_COLOR = new Color(50, 150, 250);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);

    public TransferPanel() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. Título
        JLabel titleLabel = new JLabel("Realizar Transferência", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // 2. Painel Central (Formulário + Transferências Recentes)
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplit.setResizeWeight(0.7); // 70% para o formulário, 30% para info
        mainSplit.setDividerSize(10);
        mainSplit.setDividerLocation(650);
        mainSplit.setBorder(null);

        // Lado Esquerdo: Formulário
        JPanel formPanel = createTransferForm();
        mainSplit.setLeftComponent(formPanel);

        // Lado Direito: Informação e Transferências Recentes
        JPanel infoPanel = createInfoSidebar();
        mainSplit.setRightComponent(infoPanel);

        add(mainSplit, BorderLayout.CENTER);
    }

    // =========================================================================
    // --- Formulário de Transferência (Lado Esquerdo) ---
    // =========================================================================
    private JPanel createTransferForm() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // 1. Conta de Origem
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        gridPanel.add(new JLabel("Conta de Origem:"), gbc);

        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1.0;
        JComboBox<String> originAccount = new JComboBox<>(new String[]{"12345-X (Conta Corrente) Saldo: MZN 5.000,00"});
        gridPanel.add(originAccount, gbc);

        // 2. Conta de Destino
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        gridPanel.add(new JLabel("Conta de Destino:"), gbc);

        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1.0;
        JTextField destinationField = new JTextField("Número da conta ou email");
        gridPanel.add(destinationField, gbc);

        // 3. Valor da Transferência
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        gridPanel.add(new JLabel("Valor (Mínimo: MZN 50,00):"), gbc);

        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1.0;
        JTextField valueField = new JTextField("0.00 MZN");
        gridPanel.add(valueField, gbc);

        // 4. Senha de Confirmação
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        gridPanel.add(new JLabel("Senha de Confirmação:"), gbc);

        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1.0;
        JPasswordField passwordField = new JPasswordField(10);
        gridPanel.add(passwordField, gbc);

        // Botão Transferir (Rodapé)
        JButton transferButton = new JButton("Transferir");
        transferButton.setBackground(PRIMARY_COLOR);
        transferButton.setForeground(Color.WHITE);
        transferButton.setFont(new Font("Arial", Font.BOLD, 14));
        transferButton.setBorder(new EmptyBorder(10, 30, 10, 30));

        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonWrapper.setBackground(Color.WHITE);
        buttonWrapper.add(transferButton);

        // Adicionar tudo ao painel
        panel.add(gridPanel, BorderLayout.CENTER);
        panel.add(buttonWrapper, BorderLayout.SOUTH);

        return panel;
    }

    // =========================================================================
    // --- Informação e Histórico (Lado Direito) ---
    // =========================================================================
    private JPanel createInfoSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(BACKGROUND_COLOR);
        sidebar.setBorder(new EmptyBorder(0, 10, 0, 0));

        // Transferências Recentes
        JPanel recentPanel = new JPanel(new GridLayout(0, 1, 0, 5));
        recentPanel.setBorder(BorderFactory.createTitledBorder("Transferências Recentes"));
        recentPanel.setBackground(Color.WHITE);
        recentPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        recentPanel.add(new JLabel("<html><b>Maria Santos</b><br>1234-5678 - MZN 1.500,00</html>"));
        recentPanel.add(new JLabel("<html><b>Carlos Lima</b><br>8765-4321 - MZN 500,00</html>"));
        recentPanel.add(new JLabel("<html><b>Supermercado</b><br>9999-8888 - MZN 850,50</html>"));

        // Informações
        JPanel infoDetailsPanel = new JPanel(new GridLayout(3, 1));
        infoDetailsPanel.setBorder(BorderFactory.createTitledBorder("Informações"));
        infoDetailsPanel.setBackground(Color.WHITE);

        infoDetailsPanel.add(new JLabel("  • Taxa: Transferências entre contas do mesmo banco são gratuitas."));
        infoDetailsPanel.add(new JLabel("  • Limite Diário: Máximo MZN 10.000,00 por dia."));
        infoDetailsPanel.add(new JLabel("  • Processamento: Imediato para o mesmo banco."));

        sidebar.add(recentPanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebar.add(infoDetailsPanel);
        sidebar.add(Box.createVerticalGlue()); // Empurra o conteúdo para cima

        return sidebar;
    }
}