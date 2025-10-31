package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WithdrawPanel extends JPanel {

    private static final Color PRIMARY_COLOR = new Color(50, 150, 250);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);

    public WithdrawPanel() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Painel Central (Fundo Branco para o formulário)
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(BACKGROUND_COLOR);

        // Formulário Principal
        JPanel formPanel = createWithdrawForm();
        centerWrapper.add(formPanel);

        add(centerWrapper, BorderLayout.CENTER);
    }

    private JPanel createWithdrawForm() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 50, 30, 50));
        panel.setPreferredSize(new Dimension(500, 450)); // Tamanho fixo para centrar

        // Título e Saldo
        JLabel title = new JLabel("Realizar Levantamento", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel balance = new JLabel("Saldo Disponível: MZN 5.000,00", SwingConstants.CENTER);
        balance.setFont(new Font("Arial", Font.PLAIN, 16));
        balance.setForeground(Color.GRAY);

        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));
        header.add(title);
        header.add(balance);
        panel.add(header, BorderLayout.NORTH);


        // Campos do Formulário
        JPanel inputGrid = new JPanel(new GridLayout(4, 1, 10, 10));
        inputGrid.setBackground(Color.WHITE);

        // 1. Campo de Valor
        JTextField valueField = new JTextField("Digite o valor", 15);
        inputGrid.add(new JLabel("Valor do Levantamento (Máximo: MZN 5.000,00)"));
        inputGrid.add(valueField);

        // 2. Campo de Senha
        JPasswordField pinField = new JPasswordField(4);
        inputGrid.add(new JLabel("Senha de 4 dígitos:"));
        inputGrid.add(pinField);

        panel.add(inputGrid, BorderLayout.CENTER);

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
        southPanel.add(suggestedValues, BorderLayout.NORTH);


        // Botão Confirmar
        JButton confirmButton = new JButton("Confirmar Levantamento");
        confirmButton.setBackground(PRIMARY_COLOR);
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setBorder(new EmptyBorder(10, 30, 10, 30));

        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonWrapper.setBackground(Color.WHITE);
        buttonWrapper.add(confirmButton);
        southPanel.add(buttonWrapper, BorderLayout.CENTER);

        panel.add(southPanel, BorderLayout.SOUTH);

        return panel;
    }
}