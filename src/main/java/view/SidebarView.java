package view;

import net.miginfocom.swing.MigLayout;
import view.componet.MyButton;
import javax.swing.*;
import java.awt.*;

public class SidebarView extends JPanel {
    private JButton homeBtn, depositBtn, withdrawBtn, transferBtn, statementBtn, profileBtn;
    private MyButton logoutBtn;
    private JLabel welcomeLabel;
    private JLabel accountNumberLabel;
    private JButton activeButton;
    private Color activeColor;

    public SidebarView() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new MigLayout("wrap, fill, insets 0", "[grow]", "[]0[]0[]push[]"));
        setBackground(new Color(12, 16, 42));
        setPreferredSize(new Dimension(280, 800));

        // Logo section
        add(createLogoPanel(), "growx, h 100!");

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

        logoPanel.add(logoLabel, "align center, gaptop 15");
        return logoPanel;
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel(new MigLayout("wrap, fill, insets 20 20 20 20", "[grow]", "[]25[]25[]25[]25[]25[]"));
        menuPanel.setBackground(new Color(12, 16, 45));

        String[] menuItems = {"🏠 Início", "💰 Depósito", "\uD83E\uDE99 Levantamento", "\uD83D\uDCB1 Transferência","📊 Extrato", "👤 Perfil"};
        Color[] menuColors = {
                new Color(28, 90, 255),
                new Color(28, 90, 255),
                new Color(28, 90, 255),
                new Color(28, 90, 255),
                new Color(28, 90, 255),
                new Color(28, 90, 255)
        };

        for (int i = 0; i < menuItems.length; i++) {
            JButton menuBtn = createMenuButton(menuItems[i], menuColors[i], i == 0);
            menuPanel.add(menuBtn, "growx, h 50!");

            // Configurar os botões específicos
            switch (menuItems[i]) {
                case "🏠 Início":
                    homeBtn = menuBtn;
                    if (i == 0) {
                        activeButton = homeBtn;
                        activeColor = menuColors[i];
                    }
                    break;
                case "💰 Depósito": depositBtn = menuBtn; break;
                case "\uD83E\uDE99 Levantamento": withdrawBtn = menuBtn; break;
                case "\uD83D\uDCB1 Transferência": transferBtn = menuBtn; break;
                case "📊 Extrato": statementBtn = menuBtn; break;
                case "👤 Perfil": profileBtn = menuBtn; break;
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

                // Verificar se este botão é o ativo
                boolean isThisButtonActive = (this == activeButton);

                if (isThisButtonActive) {
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
                if (isThisButtonActive) {
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
                // Não pintar borda padrão
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

        logoutBtn = new MyButton();
        logoutBtn.setBackground(new Color(239, 68, 68));
        logoutBtn.setForeground(new Color(250, 250, 250));
        logoutBtn.setText("SAIR DA CONTA");

        userPanel.add(userInfoPanel, "growx");
        userPanel.add(logoutBtn, "growx, h 40!");

        return userPanel;
    }

    public void setActiveButton(JButton button) {
        if (activeButton != null && activeButton != button) {
            activeButton.repaint();
        }

        activeButton = button;
        if (activeButton != null) {
            activeButton.repaint();
        }
    }

    public void setHomeActive() {
        setActiveButton(homeBtn);
    }

    // Getters for buttons
    public JButton getHomeButton() { return homeBtn; }
    public JButton getDepositBtn() { return depositBtn; }
    public JButton getWithdrawBtn() { return withdrawBtn; }
    public JButton getTransferBtn() { return transferBtn; }
    public JButton getStatementBtn() { return statementBtn; }
    public JButton getProfileBtn(){return profileBtn;}
    public JButton getLogoutBtn() { return logoutBtn; }

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

            updateAvatarLabel(initials);
        }
    }

    private void updateAvatarLabel(String initials) {
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