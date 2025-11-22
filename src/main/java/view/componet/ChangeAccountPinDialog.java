package view.componet;

import model.entities.Account;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class ChangeAccountPinDialog extends JDialog {
    private JComboBox<Account> accountComboBox;
    private JPasswordField currentPinField;
    private JPasswordField newPinField;
    private JPasswordField confirmPinField;
    private MyButton confirmButton;
    private MyButton cancelButton;
    private List<Account> customerAccounts;

    public ChangeAccountPinDialog(JFrame parent, List<Account> accounts) {
        super(parent, "Alterar PIN da Conta", true);
        this.customerAccounts = accounts;
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new MigLayout("wrap, fill, insets 20", "[grow]", "[]10[]5[]10[]10[]10[]10[]"));
        setSize(500, 550);
        setResizable(false);

        // Título
        JLabel titleLabel = new JLabel("Alterar PIN da Conta");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(30, 40, 112));
        add(titleLabel, "align center, wrap");

        // Instruções
        JLabel instructionLabel = new JLabel("Selecione a conta e digite o PIN atual e o novo PIN");
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        instructionLabel.setForeground(Color.GRAY);
        add(instructionLabel, "align center, wrap");

        // Seletor de Conta
        JLabel accountLabel = new JLabel("Selecionar Conta:");
        accountLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        add(accountLabel, "growx");

        accountComboBox = new JComboBox<>();
        accountComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        accountComboBox.setRenderer(new AccountListRenderer());
        loadAccounts();
        add(accountComboBox, "growx, h 40!, wrap");

        // Campo PIN Atual
        JLabel currentPinLabel = new JLabel("PIN Atual (4 dígitos):");
        currentPinLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        add(currentPinLabel, "growx");

        currentPinField = new JPasswordField();
        currentPinField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        currentPinField.setHorizontalAlignment(JTextField.CENTER);
        add(currentPinField, "growx, h 40!, wrap");

        // Campo Novo PIN
        JLabel newPinLabel = new JLabel("Novo PIN (4 dígitos):");
        newPinLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        add(newPinLabel, "growx");

        newPinField = new JPasswordField();
        newPinField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        newPinField.setHorizontalAlignment(JTextField.CENTER);
        add(newPinField, "growx, h 40!, wrap");

        // Campo Confirmar Novo PIN
        JLabel confirmPinLabel = new JLabel("Confirmar Novo PIN:");
        confirmPinLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        add(confirmPinLabel, "growx");

        confirmPinField = new JPasswordField();
        confirmPinField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        confirmPinField.setHorizontalAlignment(JTextField.CENTER);
        add(confirmPinField, "growx, h 40!, wrap");

        // Painel de botões
        JPanel buttonPanel = new JPanel(new MigLayout("insets 0", "[grow][grow]", "[]"));
        buttonPanel.setOpaque(false);

        cancelButton = new MyButton();
        cancelButton.setText("Cancelar");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setBackground(new Color(107, 114, 128));
        cancelButton.setFocusPainted(false);
        buttonPanel.add(cancelButton, "growx");

        confirmButton = new MyButton();
        confirmButton.setText("Alterar PIN");
        confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confirmButton.setBackground(new Color(59, 130, 246));
        confirmButton.setFocusPainted(false);
        buttonPanel.add(confirmButton, "growx");

        add(buttonPanel, "growx, span");

        addButtonHoverEffects();

        // Focar no campo PIN atual
        SwingUtilities.invokeLater(() -> currentPinField.requestFocus());
    }

    private void loadAccounts() {
        if (customerAccounts != null && !customerAccounts.isEmpty()) {
            for (Account account : customerAccounts) {
                accountComboBox.addItem(account);
            }
        }
    }

    private void addButtonHoverEffects() {
        confirmButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                confirmButton.setBackground(new Color(37, 99, 235));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                confirmButton.setBackground(new Color(59, 130, 246));
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
    }

    public Account getSelectedAccount() {
        return (Account) accountComboBox.getSelectedItem();
    }

    public String getCurrentPin() {
        return new String(currentPinField.getPassword());
    }

    public String getNewPin() {
        return new String(newPinField.getPassword());
    }

    public String getConfirmPin() {
        return new String(confirmPinField.getPassword());
    }

    public void addConfirmListener(ActionListener listener) {
        confirmButton.addActionListener(listener);
    }

    public void addCancelListener(ActionListener listener) {
        cancelButton.addActionListener(listener);
    }

    public void clearFields() {
        currentPinField.setText("");
        newPinField.setText("");
        confirmPinField.setText("");
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
        currentPinField.requestFocus();
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    // Renderer personalizado para mostrar informações da conta no combobox
    private class AccountListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Account) {
                Account account = (Account) value;
                String type = account.getAccountType().toString().equals("CORRENTE") ? "Corrente" : "Poupança";
                setText(account.getAccountNumber() + " - " + type + " (MZN " +
                        String.format("%,.2f", account.getBalance()) + ")");
            }

            return this;
        }
    }
}
