package view;

import net.miginfocom.swing.MigLayout;
import view.componet.MyButton;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class DepositView extends JPanel {
    private JComboBox<String> accountComboBox;
    private JTextField amountField;
    private MyButton cancelButton;
    private MyButton continueButton;
    private ButtonGroup methodGroup;
    private JRadioButton transferRadio;
    private JRadioButton betweenAccountsRadio;
    private JPanel quickAmountsPanel;
    private JLabel errorLabel;

    public DepositView() {
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

        JLabel titleLabel = new JLabel("Depósito");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(15, 23, 42));

        header.add(titleLabel, "grow");

        return header;
    }

    private JScrollPane createMainContent() {
        JPanel content = new JPanel(new MigLayout("wrap, fillx, insets 20", "[grow]", "[]20[]20[]20[]"));
        content.setBackground(new Color(229, 231, 235));

        // Dedications section
        content.add(createDedicationsSection(), "growx");

        // Destination Account section
        content.add(createDestinationAccountSection(), "growx");

        // Quick Deposit section
        content.add(createQuickDepositSection(), "growx");

        // Deposit Information section
        content.add(createDepositInfoSection(), "growx");

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    private JPanel createDedicationsSection() {
        JPanel panel = createCard();
        panel.setLayout(new MigLayout("wrap, fill, insets 25", "[grow]", "[]15[]"));

        JLabel titleLabel = new JLabel("Dedicações");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(15, 23, 42));

        // Deposit value section
        JPanel depositValuePanel = new JPanel(new MigLayout("wrap, fill, insets 0", "[grow]", "[]10[]5[]"));
        depositValuePanel.setOpaque(false);

        JLabel minAmountLabel = new JLabel("Valor mínimo: MZN 100,00");
        minAmountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        minAmountLabel.setForeground(new Color(107, 114, 128));

        JLabel amountPromptLabel = new JLabel("Digite o valor:");
        amountPromptLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        amountPromptLabel.setForeground(new Color(15, 23, 42));

        amountField = new JTextField();
        amountField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        amountField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        amountField.setPreferredSize(new Dimension(200, 45));

        errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setForeground(new Color(239, 68, 68));
        errorLabel.setVisible(false);

        JPanel amountPanel = new JPanel(new BorderLayout());
        JLabel currencyLabel = new JLabel("MZN");
        currencyLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        currencyLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        currencyLabel.setForeground(new Color(107, 114, 128));

        amountPanel.add(currencyLabel, BorderLayout.WEST);
        amountPanel.add(amountField, BorderLayout.CENTER);

        depositValuePanel.add(minAmountLabel, "growx, wrap");
        depositValuePanel.add(amountPromptLabel, "growx, wrap");
        depositValuePanel.add(amountPanel, "growx, h 45!, wrap");
        depositValuePanel.add(errorLabel, "growx");

        panel.add(titleLabel, "growx, wrap");
        panel.add(depositValuePanel, "growx");

        return panel;
    }

    private JPanel createDestinationAccountSection() {
        JPanel panel = createCard();
        panel.setLayout(new MigLayout("wrap, fill, insets 25", "[grow]", "[]15[]15[]"));

        JLabel titleLabel = new JLabel("Conta Destino");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(15, 23, 42));

        // Account selection
        JPanel accountPanel = new JPanel(new MigLayout("wrap, fill, insets 0", "[grow]", "[]10[]"));
        accountPanel.setOpaque(false);

        JLabel accountLabel = new JLabel("Selecione a conta:");
        accountLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        accountLabel.setForeground(new Color(15, 23, 42));

        accountComboBox = new JComboBox<>(new String[]{"Conta Corrente - 123456", "Conta Poupança - 789012"});
        accountComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        accountComboBox.setBackground(Color.WHITE);
        accountComboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        accountPanel.add(accountLabel, "growx, wrap");
        accountPanel.add(accountComboBox, "growx, h 60!");

        // Deposit method
        JPanel methodPanel = new JPanel(new MigLayout("wrap, fill, insets 0", "[grow]", "[]10[]"));
        methodPanel.setOpaque(false);

        JLabel methodLabel = new JLabel("Método de Depósito:");
        methodLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        methodLabel.setForeground(new Color(15, 23, 42));

        methodGroup = new ButtonGroup();
        transferRadio = new JRadioButton("Transferência");
        betweenAccountsRadio = new JRadioButton("Entre contas");

        transferRadio.setSelected(true);
        methodGroup.add(transferRadio);
        methodGroup.add(betweenAccountsRadio);

        transferRadio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        betweenAccountsRadio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        transferRadio.setBackground(Color.WHITE);
        betweenAccountsRadio.setBackground(Color.WHITE);

        JPanel radioPanel = new JPanel(new MigLayout("insets 0", "[][100!]", "[]"));
        radioPanel.setOpaque(false);
        radioPanel.add(transferRadio, "gapright 20");
        radioPanel.add(betweenAccountsRadio);

        methodPanel.add(methodLabel, "growx, wrap");
        methodPanel.add(radioPanel, "growx");

        // Buttons
        JPanel buttonPanel = new JPanel(new MigLayout("insets 0", "[grow]30[grow]30", "[]25[]"));
        buttonPanel.setOpaque(false);

        cancelButton = new MyButton();
        cancelButton.setText("Cancelar");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cancelButton.setBackground(new Color(239, 68, 68));

        continueButton = new MyButton();
        continueButton.setText("Confirmar");
        continueButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        continueButton.setBackground(new Color(116, 185, 249));

        // Adicionar efeitos over aos botões
        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (cancelButton.isEnabled()) {
                    cancelButton.setBackground(new Color(220, 38, 38));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (cancelButton.isEnabled()) {
                    cancelButton.setBackground(new Color(239, 68, 68));
                }
            }
        });

        continueButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (continueButton.isEnabled()) {
                    continueButton.setBackground(new Color(14, 159, 110));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (continueButton.isEnabled()) {
                    continueButton.setBackground(new Color(16, 185, 129));
                }
            }
        });

        buttonPanel.add(new JLabel(), "growx"); // spacer
        buttonPanel.add(cancelButton, "growx");
        buttonPanel.add(continueButton, "growx");

        panel.add(titleLabel, "growx, wrap");
        panel.add(accountPanel, "growx, wrap");
        panel.add(methodPanel, "growx, wrap");
        panel.add(buttonPanel, "growx, wrap, wmax 40%, right, south");

        return panel;
    }

    private JPanel createQuickDepositSection() {
        JPanel panel = createCard();
        panel.setLayout(new MigLayout("wrap, fill, insets 0", "15[grow]15", "10[]10[]"));

        JLabel titleLabel = new JLabel("Depósito Rápido");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(15, 23, 42));

        JLabel subtitleLabel = new JLabel("Valores sugeridos:");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitleLabel.setForeground(new Color(107, 114, 128));

        quickAmountsPanel = new JPanel(new MigLayout("insets 0", "[grow][grow][grow][grow]", "[]"));
        quickAmountsPanel.setOpaque(false);

        String[] amounts = {"MZN 500", "MZN 1.000", "MZN 2.000", "MZN 5.000"};

        for (String amount : amounts) {
            JButton amountButton = createAmountButton(amount);
            quickAmountsPanel.add(amountButton, "growx, h 50!");
        }

        panel.add(titleLabel, "growx, wrap");
        panel.add(subtitleLabel, "growx, wrap");
        panel.add(quickAmountsPanel, "growx");

        return panel;
    }

    private JButton createAmountButton(String amount) {
        MyButton button = new MyButton();
        button.setText(amount);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(59, 130, 246));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(59, 130, 246)),
                BorderFactory.createEmptyBorder(10, 5, 10, 5)
        ));

        button.addActionListener(e -> {
            try {
                // Extrai apenas o valor numérico
                String numericValue = amount.replace("MZN ", "").replace(".", "").replace(",", ".");
                setAmountField(numericValue);
            } catch (Exception ex) {
                System.err.println("Erro ao processar valor rápido: " + ex.getMessage());
            }
        });

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(59, 130, 246));
                button.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
                button.setForeground(new Color(59, 130, 246));
            }
        });

        return button;
    }

    private JPanel createDepositInfoSection() {
        JPanel panel = createCard();
        panel.setLayout(new MigLayout("wrap, fill, insets 20", "[grow]25[grow]", "[]15[]15[]"));

        JLabel titleLabel = new JLabel("Informações do Depósito");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(15, 23, 42));

        // Information
        JPanel infoPanel = createInfoItem(
                "Valor Minimo",
                "MZN 100,00 por depósito"
        );

        // Limits
        JPanel limitsPanel = createInfoItem(
                "Valor Máximo",
                "MZN 50.000,00 por depósito"
        );

        panel.add(titleLabel, "growx, wrap");
        panel.add(infoPanel, "growx, wrap, wmin 80");
        panel.add(limitsPanel, "growx, wrap");

        return panel;
    }

    private JPanel createInfoItem(String title, String description) {
        JPanel panel = new JPanel(new MigLayout("fill, insets 0", "[][grow]", "[]"));
        panel.setOpaque(false);

        JLabel titleLabel = new JLabel(title + ":");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(15, 23, 42));
        titleLabel.setPreferredSize(new Dimension(120, 20));

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(new Color(107, 114, 128));

        panel.add(titleLabel, "w 120!");
        panel.add(descLabel, "growx");

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

    // Métodos públicos para validação e controle
    public void setAmountField(String value) {
        if (amountField != null) {
            amountField.setText(value);
            clearFieldError(); // Limpa erro quando setamos um valor
        }
    }

    public void showFieldError(String message) {
        if (amountField != null && errorLabel != null) {
            amountField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(239, 68, 68)),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            errorLabel.setText(message);
            errorLabel.setVisible(true);
        }
    }

    public void clearFieldError() {
        if (amountField != null && errorLabel != null) {
            amountField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(203, 213, 225)),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            errorLabel.setText(" ");
            errorLabel.setVisible(false);
        }
    }

    public void addAmountFieldListener(DocumentListener listener) {
        if (amountField != null) {
            amountField.getDocument().addDocumentListener(listener);
        }
    }

    // Getters com verificações de null
    public JComboBox<String> getAccountComboBox() {
        return accountComboBox != null ? accountComboBox : new JComboBox<>();
    }

    public JTextField getAmountField() {
        return amountField != null ? amountField : new JTextField();
    }

    public JButton getCancelButton() {
        return cancelButton != null ? cancelButton : new JButton("Cancelar");
    }

    public JButton getContinueButton() {
        return continueButton != null ? continueButton : new JButton("Continuar");
    }

    public JRadioButton getTransferRadio() {
        return transferRadio != null ? transferRadio : new JRadioButton("Transferência");
    }

    public JRadioButton getBetweenAccountsRadio() {
        return betweenAccountsRadio != null ? betweenAccountsRadio : new JRadioButton("Entre contas");
    }

    public JLabel getErrorLabel() {
        return errorLabel != null ? errorLabel : new JLabel(" ");
    }
}