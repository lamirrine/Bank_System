package view;

import net.miginfocom.swing.MigLayout;
import view.componet.MyButton;

import javax.swing.*;
import java.awt.*;

public class ProfileView extends JPanel {
    private JLabel welcomeLabel;
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel phoneLabel;
    private JLabel addressLabel;
    private JLabel biLabel;
    private JLabel nuitLabel;
    private JLabel passportLabel;
    private JLabel registrationDateLabel;
    private MyButton openCurrentAccountBtn;
    private MyButton openSavingsAccountBtn;
    private MyButton changePasswordBtn;
    private MyButton cancelButton;
    private JPanel accountsPanel;

    public ProfileView() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new MigLayout("fill, insets 20", "[grow]", "[][grow]"));
        setBackground(new Color(229, 231, 235));

        // Header
        add(createHeader(), "growx, wrap");

        // Main content with scroll
        JScrollPane scrollPane = createMainContent();
        add(scrollPane, "grow");
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new MigLayout("fill, insets 0 0 15 0", "[grow]", "[]"));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));

        JLabel titleLabel = new JLabel("Meu Perfil");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(15, 23, 42));

        header.add(titleLabel, "grow");

        return header;
    }

    private JScrollPane createMainContent() {
        JPanel content = new JPanel(new MigLayout("wrap, fillx, insets 20", "[grow]", "[]20[]20[]"));
        content.setBackground(new Color(229, 231, 235));

        // Personal info section
        content.add(createPersonalInfoSection(), "growx");

        // Documents section
        content.add(createDocumentsSection(), "growx");

        // Accounts section
        content.add(createAccountsSection(), "growx");

        // Actions section
        content.add(createActionsSection(), "growx");

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    private JPanel createPersonalInfoSection() {
        JPanel panel = createCard();
        panel.setLayout(new MigLayout("wrap, fill, insets 25", "[grow]", "[]15[]"));

        JLabel titleLabel = new JLabel("Informações Pessoais");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(15, 23, 42));

        // Personal info content
        JPanel infoPanel = new JPanel(new MigLayout("wrap, fill, insets 0", "[grow][grow]", "[]10[]10[]10[]"));
        infoPanel.setOpaque(false);

        // Welcome message
        welcomeLabel = new JLabel("Olá, Utilizador!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        welcomeLabel.setForeground(new Color(59, 130, 246));
        infoPanel.add(welcomeLabel, "span 2, growx, wrap");

        // Name
        JLabel nameTitle = new JLabel("Nome Completo:");
        nameTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nameTitle.setForeground(new Color(107, 114, 128));

        nameLabel = new JLabel("Carregando...");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameLabel.setForeground(new Color(15, 23, 42));

        infoPanel.add(nameTitle, "growx");
        infoPanel.add(nameLabel, "growx, wrap");

        // Email
        JLabel emailTitle = new JLabel("Email:");
        emailTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        emailTitle.setForeground(new Color(107, 114, 128));

        emailLabel = new JLabel("Carregando...");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailLabel.setForeground(new Color(15, 23, 42));

        infoPanel.add(emailTitle, "growx");
        infoPanel.add(emailLabel, "growx, wrap");

        // Phone
        JLabel phoneTitle = new JLabel("Telefone:");
        phoneTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        phoneTitle.setForeground(new Color(107, 114, 128));

        phoneLabel = new JLabel("Carregando...");
        phoneLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        phoneLabel.setForeground(new Color(15, 23, 42));

        infoPanel.add(phoneTitle, "growx");
        infoPanel.add(phoneLabel, "growx, wrap");

        // Address
        JLabel addressTitle = new JLabel("Endereço:");
        addressTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addressTitle.setForeground(new Color(107, 114, 128));

        addressLabel = new JLabel("Carregando...");
        addressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        addressLabel.setForeground(new Color(15, 23, 42));

        infoPanel.add(addressTitle, "growx");
        infoPanel.add(addressLabel, "growx, wrap");

        // Registration date
        JLabel regDateTitle = new JLabel("Data de Registo:");
        regDateTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        regDateTitle.setForeground(new Color(107, 114, 128));

        registrationDateLabel = new JLabel("Carregando...");
        registrationDateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        registrationDateLabel.setForeground(new Color(15, 23, 42));

        infoPanel.add(regDateTitle, "growx");
        infoPanel.add(registrationDateLabel, "growx, wrap");

        panel.add(titleLabel, "growx, wrap");
        panel.add(infoPanel, "growx");

        return panel;
    }

    private JPanel createDocumentsSection() {
        JPanel panel = createCard();
        panel.setLayout(new MigLayout("wrap, fill, insets 25", "[grow]", "[]15[]"));

        JLabel titleLabel = new JLabel("Documentos de Identificação");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(15, 23, 42));

        // Documents content
        JPanel docsPanel = new JPanel(new MigLayout("wrap, fill, insets 0", "[grow][grow]", "[]10[]10[]"));
        docsPanel.setOpaque(false);

        // BI Number
        JLabel biTitle = new JLabel("Número do BI:");
        biTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        biTitle.setForeground(new Color(107, 114, 128));

        biLabel = new JLabel("Carregando...");
        biLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        biLabel.setForeground(new Color(15, 23, 42));

        docsPanel.add(biTitle, "growx");
        docsPanel.add(biLabel, "growx, wrap");

        // NUIT
        JLabel nuitTitle = new JLabel("NUIT:");
        nuitTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nuitTitle.setForeground(new Color(107, 114, 128));

        nuitLabel = new JLabel("Carregando...");
        nuitLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nuitLabel.setForeground(new Color(15, 23, 42));

        docsPanel.add(nuitTitle, "growx");
        docsPanel.add(nuitLabel, "growx, wrap");

        // Passport
        JLabel passportTitle = new JLabel("Passaporte:");
        passportTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passportTitle.setForeground(new Color(107, 114, 128));

        passportLabel = new JLabel("Carregando...");
        passportLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passportLabel.setForeground(new Color(15, 23, 42));

        docsPanel.add(passportTitle, "growx");
        docsPanel.add(passportLabel, "growx, wrap");

        panel.add(titleLabel, "growx, wrap");
        panel.add(docsPanel, "growx");

        return panel;
    }

    private JPanel createAccountsSection() {
        JPanel panel = createCard();
        panel.setLayout(new MigLayout("wrap, fill, insets 25", "[grow]", "[]15[]15[]"));

        JLabel titleLabel = new JLabel("Minhas Contas");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(15, 23, 42));

        // Accounts list
        accountsPanel = new JPanel(new MigLayout("wrap, fill, insets 0", "[grow, fill]", "[]10[]"));
        accountsPanel.setOpaque(false);

        // Placeholder inicial
        JLabel loadingLabel = new JLabel("Carregando contas...", JLabel.CENTER);
        loadingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loadingLabel.setForeground(new Color(107, 114, 128));
        accountsPanel.add(loadingLabel, "grow, h 100!");

        // Open new accounts buttons
        JPanel newAccountsPanel = new JPanel(new MigLayout("fill, insets 0", "[grow][grow]", "[]"));
        newAccountsPanel.setOpaque(false);

        openCurrentAccountBtn = new MyButton();
        openCurrentAccountBtn.setText("Abrir Conta Corrente");
        openCurrentAccountBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        openCurrentAccountBtn.setBackground(new Color(59, 130, 246));
        //openCurrentAccountBtn.setFocusPainted(false);

        openSavingsAccountBtn = new MyButton();
        openSavingsAccountBtn.setText("Abrir Conta Poupança");
        openSavingsAccountBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        openSavingsAccountBtn.setBackground(new Color(16, 185, 129));

        // Adicionar efeitos hover
        openCurrentAccountBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                openCurrentAccountBtn.setBackground(new Color(37, 99, 235));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                openCurrentAccountBtn.setBackground(new Color(59, 130, 246));
            }
        });

        openSavingsAccountBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                openSavingsAccountBtn.setBackground(new Color(14, 159, 110));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                openSavingsAccountBtn.setBackground(new Color(16, 185, 129));
            }
        });

        newAccountsPanel.add(openCurrentAccountBtn, "growx");
        newAccountsPanel.add(openSavingsAccountBtn, "growx");

        panel.add(titleLabel, "growx, wrap");
        panel.add(accountsPanel, "growx, wrap");
        panel.add(newAccountsPanel, "growx, wmax 45%");

        return panel;
    }

    private JPanel createActionsSection() {
        JPanel panel = createCard();
        panel.setLayout(new MigLayout("wrap, fill, insets 25", "[grow]", "[]15[]"));

        JLabel titleLabel = new JLabel("Ações da Conta");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(15, 23, 42));

        // Actions buttons
        JPanel actionsPanel = new JPanel(new MigLayout("fill, insets 1", "[grow][grow][grow]", "[]"));
        actionsPanel.setOpaque(false);

        changePasswordBtn = new MyButton();
        changePasswordBtn.setText("Alterar Senha");
        changePasswordBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        changePasswordBtn.setBackground(new Color(168, 85, 247));
        changePasswordBtn.setFocusPainted(false);

        cancelButton = new MyButton();
        cancelButton.setText("     Voltar      ");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setBackground(new Color(107, 114, 128));
        cancelButton.setFocusPainted(false);

        changePasswordBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                changePasswordBtn.setBackground(new Color(147, 51, 234));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                changePasswordBtn.setBackground(new Color(168, 85, 247));
            }
        });

        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cancelButton.setBackground(new Color(75, 85, 99));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                cancelButton.setBackground(new Color(107, 114, 128));
            }
        });

        actionsPanel.add(changePasswordBtn, "growx");
        actionsPanel.add(cancelButton, "growx");

        panel.add(titleLabel, "growx, wrap");
        panel.add(actionsPanel, "growx, wmax 45%");

        return panel;
    }

    private JPanel createCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        card.setLayout(new MigLayout("fill, insets 0", "[grow]", "[grow]"));
        card.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        card.setOpaque(false);
        return card;
    }

    // Getters
    public JButton getOpenCurrentAccountBtn() { return openCurrentAccountBtn; }
    public JButton getOpenSavingsAccountBtn() { return openSavingsAccountBtn; }
    public JButton getChangePasswordBtn() { return changePasswordBtn; }
    public JButton getCancelButton() { return cancelButton; }
    public JPanel getAccountsPanel() { return accountsPanel; }

    // Métodos para atualizar dados
    public void setCustomerInfo(String fullName, String email, String phone, String address,
                                String biNumber, String nuit, String passportNumber,
                                String registrationDate) {
        if (welcomeLabel != null) {
            welcomeLabel.setText("Olá, " + fullName.split(" ")[0] + "!");
        }
        if (nameLabel != null) {
            nameLabel.setText(fullName);
        }
        if (emailLabel != null) {
            emailLabel.setText(email != null ? email : "Não informado");
        }
        if (phoneLabel != null) {
            phoneLabel.setText(phone != null ? phone : "Não informado");
        }
        if (addressLabel != null) {
            addressLabel.setText(address != null ? address : "Não informado");
        }
        if (biLabel != null) {
            biLabel.setText(biNumber != null ? biNumber : "Não informado");
        }
        if (nuitLabel != null) {
            nuitLabel.setText(nuit != null ? nuit : "Não informado");
        }
        if (passportLabel != null) {
            passportLabel.setText(passportNumber != null ? passportNumber : "Não informado");
        }
        if (registrationDateLabel != null) {
            registrationDateLabel.setText(registrationDate != null ? registrationDate : "Não informado");
        }
    }

    public void updateAccounts(java.util.List<model.entities.Account> accounts) {
        accountsPanel.removeAll();
        accountsPanel.setLayout(new MigLayout("wrap, fill, insets 0", "[grow, fill]", "[]10[]"));

        if (accounts == null || accounts.isEmpty()) {
            JLabel noAccountsLabel = new JLabel("Nenhuma conta encontrada", JLabel.CENTER);
            noAccountsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            noAccountsLabel.setForeground(new Color(107, 114, 128));
            accountsPanel.add(noAccountsLabel, "grow, h 100!");
        } else {
            for (model.entities.Account account : accounts) {
                JPanel accountCard = createAccountCard(account);
                accountsPanel.add(accountCard, "growx");
            }
        }

        accountsPanel.revalidate();
        accountsPanel.repaint();
    }

    private JPanel createAccountCard(model.entities.Account account) {
        JPanel card = new JPanel(new MigLayout("fill, insets 15", "[grow]", "[]5[]"));
        card.setBackground(new Color(248, 250, 252));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
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
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        balanceLabel.setForeground(new Color(16, 185, 129));

        JLabel statusLabel = new JLabel(account.getStatus() != null ?
                account.getStatus().toString() : "ATIVA");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(account.getStatus() == model.enums.AccountStatus.ATIVA ?
                new Color(16, 185, 129) : new Color(239, 68, 68));

        JPanel leftPanel = new JPanel(new MigLayout("wrap, insets 0", "[grow]", "[]5[]"));
        leftPanel.setBackground(new Color(248, 250, 252));
        leftPanel.add(titleLabel, "growx");
        leftPanel.add(numberLabel, "growx");

        JPanel rightPanel = new JPanel(new MigLayout("wrap, insets 0, align right", "[grow]", "[]5[]"));
        rightPanel.setBackground(new Color(248, 250, 252));
        rightPanel.add(balanceLabel, "growx");
        rightPanel.add(statusLabel, "growx");

        card.add(leftPanel, "grow");
        card.add(rightPanel, "top");

        return card;
    }
}