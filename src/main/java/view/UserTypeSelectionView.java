// view/UserTypeSelectionView.java
package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class UserTypeSelectionView extends JFrame {
    private JButton clientButton;
    private JButton employeeButton;

    public UserTypeSelectionView() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Banco Digital - Entrar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 560);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 900, 560, 40, 40));

        getContentPane().setBackground(new Color(13, 36, 56));

        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(new Color(13, 36, 56));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Header
        JPanel headerPanel = createHeaderPanel();

        // Selection Cards
        JPanel selectionPanel = createSelectionPanel();

        // Footer
        JPanel footerPanel = createFooterPanel();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(selectionPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(13, 36, 56));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Logo/Bank Name
        JLabel bankName = new JLabel("BANCO DIGITAL");
        bankName.setFont(new Font("Segoe UI", Font.BOLD, 32));
        bankName.setForeground(Color.WHITE);
        bankName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel slogan = new JLabel("Sistema Bancário Integrado");
        slogan.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        slogan.setForeground(new Color(173, 181, 189));
        slogan.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(bankName);
        panel.add(Box.createVerticalStrut(10));
        panel.add(slogan);
        panel.add(Box.createVerticalStrut(40));

        return panel;
    }

    private JPanel createSelectionPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 40, 40));
        panel.setBackground(new Color(13, 36, 56));

        // Client Card
        JPanel clientCard = createUserCard(
                "👤 CLIENTE",
                "Acesse sua conta pessoal",
                new Color(41, 128, 185),
                "Gerencie suas finanças, faça transferências e consulte extratos"
        );
        clientButton = new JButton();
        clientButton.setLayout(new BorderLayout());
        clientButton.add(clientCard, BorderLayout.CENTER);
        styleSelectionButton(clientButton);

        // Employee Card
        JPanel employeeCard = createUserCard(
                "👨‍💼 FUNCIONÁRIO",
                "Acesso administrativo",
                new Color(39, 174, 96),
                "Gerencie clientes, contas e operações do banco"
        );
        employeeButton = new JButton();
        employeeButton.setLayout(new BorderLayout());
        employeeButton.add(employeeCard, BorderLayout.CENTER);
        styleSelectionButton(employeeButton);

        panel.add(clientButton);
        panel.add(employeeButton);

        return panel;
    }

    private JPanel createUserCard(String title, String subtitle, Color color, String description) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Title Section
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(color);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(240, 240, 240));

        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);

        // Description
        JLabel descLabel = new JLabel("<html><div style='text-align: left; color: #E8F4FD; font-size: 11px; margin-top: 10px;'>" + description + "</div></html>");

        card.add(titlePanel, BorderLayout.NORTH);
        card.add(descLabel, BorderLayout.CENTER);

        return card;
    }

    private void styleSelectionButton(JButton button) {
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBorder(BorderFactory.createEmptyBorder());
            }
        });
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(13, 36, 56));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JLabel securityLabel = new JLabel("🔒 Sistema 100% seguro • Criptografia avançada", JLabel.CENTER);
        securityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        securityLabel.setForeground(new Color(173, 181, 189));

        panel.add(securityLabel, BorderLayout.CENTER);
        return panel;
    }

    public void addClientListener(ActionListener listener) {
        clientButton.addActionListener(listener);
    }

    public void addEmployeeListener(ActionListener listener) {
        employeeButton.addActionListener(listener);
    }
}