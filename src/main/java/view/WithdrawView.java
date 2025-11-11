package view;

import net.miginfocom.swing.MigLayout;
import view.componet.MyButton;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class WithdrawView extends JPanel {
    private JLabel balanceLabel;
    private JTextField amountField;
    private JPasswordField pinField;
    private MyButton cancelButton;
    private MyButton confirmButton;
    private JPanel quickAmountsPanel;
    private JLabel dailyLimitLabel;
    private JLabel usedTodayLabel;
    private JLabel remainingLabel;
    private JLabel errorLabel;

    public WithdrawView() {
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

        JLabel titleLabel = new JLabel("Levantamento");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(15, 23, 42));

        header.add(titleLabel, "grow");

        return header;
    }

    private JScrollPane createMainContent() {
        JPanel content = new JPanel(new MigLayout("wrap, fillx, insets 20", "[grow]", "[]20[]20[]"));
        content.setBackground(new Color(229, 231, 235));

        // Withdrawal section
        content.add(createWithdrawalSection(), "growx");

        // Quick amounts section
        content.add(createQuickAmountsSection(), "growx");

        // Daily limits section
        content.add(createDailyLimitsSection(), "growx");

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    private JPanel createWithdrawalSection() {
        JPanel panel = createCard();
        panel.setLayout(new MigLayout("wrap, fill, insets 25", "[grow]", "[]15[]15[]"));

        JLabel titleLabel = new JLabel("Realizar Levantamento");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(15, 23, 42));

        // Balance info
        JPanel balancePanel = new JPanel(new MigLayout("wrap, fill, insets 0", "[grow]", "[]5[]"));
        balancePanel.setOpaque(false);

        JLabel balanceTitleLabel = new JLabel("Saldo Disponível");
        balanceTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        balanceTitleLabel.setForeground(new Color(107, 114, 128));

        balanceLabel = new JLabel("MZN 0,00");
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        balanceLabel.setForeground(new Color(16, 185, 129));

        balancePanel.add(balanceTitleLabel, "growx, wrap");
        balancePanel.add(balanceLabel, "growx");

        // Amount input
        JPanel amountPanel = new JPanel(new MigLayout("wrap, fill, insets 0", "[grow]", "[]10[]5[]"));
        amountPanel.setOpaque(false);

        JLabel amountTitleLabel = new JLabel("Valor do Levantamento");
        amountTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        amountTitleLabel.setForeground(new Color(15, 23, 42));

        JLabel maxAmountLabel = new JLabel("Máximo: MZN 5.000,00 por dia");
        maxAmountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        maxAmountLabel.setForeground(new Color(107, 114, 128));

        JLabel amountPromptLabel = new JLabel("Digite o valor:");
        amountPromptLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        amountPromptLabel.setForeground(new Color(15, 23, 42));

        amountField = new JTextField();
        amountField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        amountField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Error label
        errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setForeground(new Color(239, 68, 68));
        errorLabel.setVisible(false);

        // Add currency prefix
        JPanel amountInputPanel = new JPanel(new BorderLayout());
        JLabel currencyLabel = new JLabel("MZN");
        currencyLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        currencyLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        currencyLabel.setForeground(new Color(107, 114, 128));

        amountInputPanel.add(currencyLabel, BorderLayout.WEST);
        amountInputPanel.add(amountField, BorderLayout.CENTER);

        amountPanel.add(amountTitleLabel, "growx, wrap");
        amountPanel.add(maxAmountLabel, "growx, wrap");
        amountPanel.add(amountPromptLabel, "growx, wrap");
        amountPanel.add(amountInputPanel, "growx, h 45!, wrap");
        amountPanel.add(errorLabel, "growx");

        // PIN input
        JPanel pinPanel = new JPanel(new MigLayout("wrap, fill, insets 0", "[grow]", "[]10[]"));
        pinPanel.setOpaque(false);

        JLabel pinLabel = new JLabel("Senha (4 dígitos):");
        pinLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pinLabel.setForeground(new Color(15, 23, 42));

        pinField = new JPasswordField();
        pinField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        pinField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        pinField.setDocument(new JTextFieldLimit(4)); // Limitar a 4 dígitos

        pinPanel.add(pinLabel, "growx, wrap");
        pinPanel.add(pinField, "growx, h 45!");

        // Buttons
        JPanel buttonPanel = new JPanel(new MigLayout("insets 0", "[grow]30[grow]30", "[]25[]"));
        buttonPanel.setOpaque(false);

        cancelButton = new MyButton();
        cancelButton.setText("Cancelar");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cancelButton.setBackground(new Color(239, 68, 68));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        confirmButton = new MyButton();
        confirmButton.setText("Confirmar");
        confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        confirmButton.setBackground(new Color(16, 185, 129));
        confirmButton.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        // Adicionar efeitos hover aos botões
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

        confirmButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (confirmButton.isEnabled()) {
                    confirmButton.setBackground(new Color(14, 159, 110));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (confirmButton.isEnabled()) {
                    confirmButton.setBackground(new Color(16, 185, 129));
                }
            }
        });

        buttonPanel.add(new JLabel(), "growx"); // spacer
        buttonPanel.add(cancelButton, "growx");
        buttonPanel.add(confirmButton, "growx");

        panel.add(titleLabel, "growx, wrap");
        panel.add(balancePanel, "growx, wrap");
        panel.add(amountPanel, "growx, wrap");
        panel.add(pinPanel, "growx, wrap");
        panel.add(buttonPanel, "growx, wrap, wmax 40%, right, south");

        return panel;
    }

    private JPanel createQuickAmountsSection() {
        JPanel panel = createCard();
        panel.setLayout(new MigLayout("wrap, fill, insets 25", "[grow]", "[]15[]"));

        JLabel titleLabel = new JLabel("Valores Sugeridos");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(15, 23, 42));

        quickAmountsPanel = new JPanel(new MigLayout("insets 0", "[grow][grow][grow][grow]", "[]"));
        quickAmountsPanel.setOpaque(false);

        String[] amounts = {"MZN 200", "MZN 500", "MZN 1.000", "MZN 2.000"};

        for (String amount : amounts) {
            JButton amountButton = createAmountButton(amount);
            quickAmountsPanel.add(amountButton, "growx, h 50!");
        }

        panel.add(titleLabel, "growx, wrap");
        panel.add(quickAmountsPanel, "growx");

        return panel;
    }

    private JButton createAmountButton(String amount) {
        MyButton button = new MyButton();
        button.setText(amount);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(new Color(59, 130, 246));
        button.setBackground(Color.WHITE);
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

    private JPanel createDailyLimitsSection() {
        JPanel panel = createCard();
        panel.setLayout(new MigLayout("wrap, fill, insets 25", "[grow]", "[]15[]"));

        JLabel titleLabel = new JLabel("Limites do Dia");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(15, 23, 42));

        JPanel limitsPanel = new JPanel(new MigLayout("fill, insets 0", "[grow][grow][grow]", "[]10[]"));
        limitsPanel.setOpaque(false);

        // Daily Limit
        JPanel dailyLimitPanel = createLimitCard("Limite Diário", "Máximo: MZN 5.000,00");
        dailyLimitLabel = new JLabel("MZN 5.000,00");
        dailyLimitLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dailyLimitLabel.setForeground(new Color(15, 23, 42));
        dailyLimitPanel.add(dailyLimitLabel, "growx, wrap");

        // Used Today
        JPanel usedTodayPanel = createLimitCard("Já Utilizado Hoje", "Valor levantado hoje");
        usedTodayLabel = new JLabel("MZN 2.250,00");
        usedTodayLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        usedTodayLabel.setForeground(new Color(239, 68, 68));
        usedTodayPanel.add(usedTodayLabel, "growx, wrap");

        // Remaining
        JPanel remainingPanel = createLimitCard("Restante Disponível", "Pode levantar hoje");
        remainingLabel = new JLabel("MZN 2.750,00");
        remainingLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        remainingLabel.setForeground(new Color(16, 185, 129));
        remainingPanel.add(remainingLabel, "growx, wrap");

        limitsPanel.add(dailyLimitPanel, "growx");
        limitsPanel.add(usedTodayPanel, "growx");
        limitsPanel.add(remainingPanel, "growx");

        panel.add(titleLabel, "growx, wrap");
        panel.add(limitsPanel, "growx");

        return panel;
    }

    private JPanel createLimitCard(String title, String subtitle) {
        JPanel panel = new JPanel(new MigLayout("wrap, fill, insets 15", "[grow]", "[]5[]"));
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(15, 23, 42));

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(107, 114, 128));

        panel.add(titleLabel, "growx, wrap");
        panel.add(subtitleLabel, "growx");

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

    private class JTextFieldLimit extends javax.swing.text.PlainDocument {
        private int limit;

        JTextFieldLimit(int limit) {
            super();
            this.limit = limit;
        }

        public void insertString(int offset, String str, javax.swing.text.AttributeSet attr)
                throws javax.swing.text.BadLocationException {
            if (str == null) return;

            if ((getLength() + str.length()) <= limit) {
                // Aceitar apenas dígitos
                if (str.matches("\\d+")) {
                    super.insertString(offset, str, attr);
                }
            }
        }
    }

    // Métodos públicos para controle
    public void setAmountField(String value) {
        if (amountField != null) {
            amountField.setText(value);
            clearFieldError();
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

    public void setBalance(double balance) {
        if (balanceLabel != null) {
            balanceLabel.setText(String.format("MZN %,.2f", balance));
        }
    }

    public void setLimits(double dailyLimit, double usedToday, double remaining) {
        if (dailyLimitLabel != null) {
            dailyLimitLabel.setText(String.format("MZN %,.2f", dailyLimit));
        }
        if (usedTodayLabel != null) {
            usedTodayLabel.setText(String.format("MZN %,.2f", usedToday));
        }
        if (remainingLabel != null) {
            remainingLabel.setText(String.format("MZN %,.2f", remaining));
        }
    }

    // Getters
    public JTextField getAmountField() { return amountField; }
    public JPasswordField getPinField() { return pinField; }
    public JButton getCancelButton() { return cancelButton; }
    public JButton getConfirmButton() { return confirmButton; }
    public JLabel getErrorLabel() { return errorLabel; }
}