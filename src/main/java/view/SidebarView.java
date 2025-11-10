// view/SidebarView.java
package view;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;

public class SidebarView extends JPanel {
    private JButton depositBtn, withdrawBtn, transferBtn, statementBtn, logoutBtn;
    private JLabel welcomeLabel;
    private JLabel accountNumberLabel;

    public SidebarView() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new MigLayout("wrap, fill, insets 0", "[grow]", "[]0[]0[]push[]"));
        setBackground(new Color(12, 16, 42));
        setPreferredSize(new Dimension(280, 800));

        // Logo section
        add(createLogoPanel(), "growx, h 120!");

        // Menu items
        add(createMenuPanel(), "growx");

        // User info panel
        add(createUserPanel(), "growx, dock south");
    }

    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel(new MigLayout("fill, insets 0", "[grow]", "[]"));
        logoPanel.setBackground(new Color(30, 41, 60));

        JLabel logoLabel = new JLabel("🏦 Banco Digital", JLabel.CENTER);
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logoLabel.setForeground(Color.WHITE);

        logoPanel.add(logoLabel, "align center, gaptop 40");
        return logoPanel;
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel(new MigLayout("wrap, fill, insets 20 20 20 20", "[grow]", "[]25[]25[]25[]25[]25[]"));
        menuPanel.setBackground(new Color(12, 16, 45));

        String[] menuItems = {"🏠 Início", "💰 Depósito", "💸 Levantamento", "🔄 Transferência", "📊 Extrato", "👤 Perfil"};
        Color[] menuColors = {
                new Color(59, 130, 246),
                new Color(16, 185, 129),
                new Color(239, 68, 68),
                new Color(168, 85, 247),
                new Color(245, 158, 11),
                new Color(14, 165, 233)
        };

        for (int i = 0; i < menuItems.length; i++) {
            JButton menuBtn = createMenuButton(menuItems[i], menuColors[i], i == 0);
            menuPanel.add(menuBtn, "growx, h 50!");

            switch (menuItems[i]) {
                case "💰 Depósito": depositBtn = menuBtn; break;
                case "💸 Levantamento": withdrawBtn = menuBtn; break;
                case "🔄 Transferência": transferBtn = menuBtn; break;
                case "📊 Extrato": statementBtn = menuBtn; break;
            }
        }
        return menuPanel;
    }

    private JButton createMenuButton(String text, Color color, boolean isActive) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int cornerRadius = 15;

                if (isActive) {
                    GradientPaint gradient = new GradientPaint(
                            0, 0, color.brighter(),
                            0, getHeight(), color
                    );
                    g2.setPaint(gradient);
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(51, 65, 85));
                } else {
                    g2.setColor(new Color(30, 41, 59));
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

                // Borda do botão
                if (isActive) {
                    g2.setColor(color.brighter());
                    g2.setStroke(new BasicStroke(2));
                } else {
                    g2.setColor(new Color(70, 85, 110));
                    g2.setStroke(new BasicStroke(1));
                }

                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, cornerRadius, cornerRadius);

                g2.dispose();

                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
            }
        };

        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.repaint();
            }
        });
        return button;
    }

    private JPanel createUserPanel() {
        JPanel userPanel = new JPanel(new MigLayout("wrap, fill, insets 20 20 20 20", "[grow]", "[]10[]"));
        userPanel.setBackground(new Color(30, 41, 59));

        // User info
        JPanel userInfoPanel = new JPanel(new MigLayout("fill, insets 0", "[50!][grow]", "[]"));
        userInfoPanel.setBackground(new Color(30, 41, 59));

        // Avatar
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
        avatarPanel.setLayout(new BorderLayout());

        JLabel avatarLabel = new JLabel("UT", JLabel.CENTER);
        avatarLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        avatarLabel.setForeground(Color.WHITE);
        avatarPanel.add(avatarLabel, BorderLayout.CENTER);

        // User details
        JPanel detailsPanel = new JPanel(new MigLayout("wrap, fill, insets 0", "[grow]", "[]5[]"));
        detailsPanel.setBackground(new Color(30, 41, 59));

        welcomeLabel = new JLabel("Utilizador");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        welcomeLabel.setForeground(Color.WHITE);

        accountNumberLabel = new JLabel("Conta: 12345");
        accountNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        accountNumberLabel.setForeground(new Color(148, 163, 184));

        detailsPanel.add(welcomeLabel, "growx");
        detailsPanel.add(accountNumberLabel, "growx");

        userInfoPanel.add(avatarPanel, "w 50!, h 50!");
        userInfoPanel.add(detailsPanel, "grow");

        // Logout button
        logoutBtn = new JButton("Sair da Conta");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setBackground(new Color(239, 68, 68));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        userPanel.add(userInfoPanel, "growx");
        userPanel.add(logoutBtn, "growx, h 40!");

        return userPanel;
    }

    // Getters for buttons
    public JButton getDepositBtn() { return depositBtn; }
    public JButton getWithdrawBtn() { return withdrawBtn; }
    public JButton getTransferBtn() { return transferBtn; }
    public JButton getStatementBtn() { return statementBtn; }
    public JButton getLogoutBtn() { return logoutBtn; }

    // Method to update user info


    public void setUserInfo(String name, String accountNumber) {
        if (welcomeLabel != null) {
            welcomeLabel.setText(name);
        }
        if (accountNumberLabel != null) {
            accountNumberLabel.setText("Conta: " + accountNumber);
        }

        if (name != null && !name.trim().isEmpty()) {
            String[] names = name.split(" ");
            String initials = "";
            if (names.length >= 1) {
                initials += names[0].charAt(0);
            }
            if (names.length >= 2) {
                initials += names[1].charAt(0);
            }

            // CORREÇÃO: Acessar o avatarLabel corretamente
            updateAvatarLabel(initials);
        }
    }

    // Novo método para atualizar o avatar de forma segura
    private void updateAvatarLabel(String initials) {
        // Encontrar o avatarLabel de forma segura
        Component[] userPanelComponents = ((JPanel) getComponent(2)).getComponents();
        for (Component comp : userPanelComponents) {
            if (comp instanceof JPanel) {
                JPanel userInfoPanel = (JPanel) comp;
                Component[] infoComponents = userInfoPanel.getComponents();
                for (Component infoComp : infoComponents) {
                    if (infoComp instanceof JPanel) {
                        JPanel avatarPanel = (JPanel) infoComp;
                        Component[] avatarComponents = avatarPanel.getComponents();
                        for (Component avatarComp : avatarComponents) {
                            if (avatarComp instanceof JLabel) {
                                JLabel avatarLabel = (JLabel) avatarComp;
                                avatarLabel.setText(initials);
                                return;
                            }
                        }
                    }
                }
            }
        }

        // Método alternativo - buscar por nome se o anterior não funcionar
        try {
            JPanel userPanel = (JPanel) getComponent(2);
            JPanel userInfoPanel = (JPanel) userPanel.getComponent(0);
            JPanel avatarPanel = (JPanel) userInfoPanel.getComponent(0);
            JLabel avatarLabel = (JLabel) avatarPanel.getComponent(0);
            avatarLabel.setText(initials);
        } catch (Exception e) {
            System.err.println("Não foi possível atualizar o avatar: " + e.getMessage());
        }
    }
}