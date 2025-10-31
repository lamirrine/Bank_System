package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DepositPanel extends JPanel {

    private static final Color PRIMARY_COLOR = new Color(50, 150, 250);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);

    public DepositPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. Painel Central (Formulário + Sugestões)
        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setBackground(BACKGROUND_COLOR);
        mainContent.add(createDepositForm());

        add(mainContent, BorderLayout.CENTER);

        // 2. Sidebar de Informações (EAST)
        add(createInfoSidebar(), BorderLayout.EAST);
    }

    // =========================================================================
    // --- Formulário de Depósito (Centro) ---
    // =========================================================================
    private JPanel createDepositForm() {
        JPanel panel = new JPanel(new BorderLayout(0, 30));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(30, 40, 30, 40)
        ));
        panel.setPreferredSize(new Dimension(550, 450));

        // Subtítulo
        JLabel subtitle = new JLabel("Selecione a conta e o valor a depositar.", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(subtitle, BorderLayout.NORTH);


        // Painel de Inputs (GridBagLayout para organização limpa)
        JPanel gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // 1. Conta Destino
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        gridPanel.add(new JLabel("Conta Destino:"), gbc);

        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1.0;
        JComboBox<String> accountSelector = new JComboBox<>(new String[]{"12345-X (Corrente)", "67890-Y (Poupança)"});
        gridPanel.add(accountSelector, gbc);

        // 2. Valor do Depósito
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        gridPanel.add(new JLabel("Valor do Depósito (MZN):"), gbc);

        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1.0;
        JTextField valueField = new JTextField("0.00 MZN", 15);
        gridPanel.add(valueField, gbc);

        // 3. Método de Depósito
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        gridPanel.add(new JLabel("Método de Depósito:"), gbc);

        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1.0;
        JComboBox<String> methodSelector = new JComboBox<>(new String[]{"Transferência (Entre Contas)", "Dinheiro (Em Agência)", "Cheque (Em Agência)"});
        gridPanel.add(methodSelector, gbc);

        panel.add(gridPanel, BorderLayout.CENTER);

        // Botões (Sugeridos + Confirmar)
        JPanel southPanel = new JPanel(new BorderLayout(0, 15));
        southPanel.setBackground(Color.WHITE);

        // Valores Sugeridos
        JPanel suggestedValues = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        suggestedValues.add(new JLabel("Valores Sugeridos:"));
        suggestedValues.add(new JButton("MZN 500"));
        suggestedValues.add(new JButton("MZN 1.000"));
        suggestedValues.add(new JButton("MZN 2.000"));
        suggestedValues.setBackground(BACKGROUND_COLOR); // Fundo para destacar
        suggestedValues.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        southPanel.add(suggestedValues, BorderLayout.NORTH);


        // Botão Confirmar
        JButton continueButton = new JButton("Confirmar Depósito");
        continueButton.setBackground(PRIMARY_COLOR);
        continueButton.setForeground(Color.WHITE);
        continueButton.setFont(new Font("Arial", Font.BOLD, 14));
        continueButton.setBorder(BorderFactory.createEmptyBorder(12, 35, 12, 35));

        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonWrapper.setBackground(Color.WHITE);
        buttonWrapper.add(continueButton);
        southPanel.add(buttonWrapper, BorderLayout.CENTER);

        panel.add(southPanel, BorderLayout.SOUTH);

        return panel;
    }

    // =========================================================================
    // --- Sidebar de Informações e Recentes (EAST) ---
    // =========================================================================
    private JPanel createInfoSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.WHITE);
        sidebar.setPreferredSize(new Dimension(280, 500));
        sidebar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        // 1. Informações
        JLabel infoTitle = new JLabel("Informações do Depósito");
        infoTitle.setFont(new Font("Arial", Font.BOLD, 16));
        infoTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        sidebar.add(infoTitle);

        JPanel infoDetailsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoDetailsPanel.setBackground(Color.WHITE);

        infoDetailsPanel.add(new JLabel("• Taxa: Em dinheiro/Transferência são gratuitas."));
        infoDetailsPanel.add(new JLabel("• Limite: Máximo MZN 50.000,00 por depósito."));
        infoDetailsPanel.add(new JLabel("• Disponibilidade: Valor disponível imediatamente."));

        sidebar.add(infoDetailsPanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 25)));

        // 2. Últimos Depósitos
        JLabel recentTitle = new JLabel("Últimos Depósitos");
        recentTitle.setFont(new Font("Arial", Font.BOLD, 16));
        recentTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        sidebar.add(recentTitle);

        JPanel recentPanel = new JPanel(new GridLayout(0, 1, 0, 5));
        recentPanel.setBackground(BACKGROUND_COLOR);

        recentPanel.add(new JLabel("15/06 - MZN 1.500,00 (Transferência)"));
        recentPanel.add(new JLabel("10/06 - MZN 2.000,00 (Caixa Automático)"));
        recentPanel.add(new JLabel("05/06 - MZN 800,00 (Agência)"));

        sidebar.add(recentPanel);
        sidebar.add(Box.createVerticalGlue()); // Empurra o conteúdo para cima

        return sidebar;
    }
}