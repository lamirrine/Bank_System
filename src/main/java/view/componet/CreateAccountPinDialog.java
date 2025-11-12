package view.componet;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CreateAccountPinDialog extends JDialog {
    private JPasswordField pinField;
    private JPasswordField confirmPinField;
    private MyButton confirmButton;
    private MyButton cancelButton;
    private String accountType;

    public CreateAccountPinDialog(JFrame parent, String accountType) {
        super(parent, "Definir PIN - " + getAccountTypeDisplay(accountType), true);
        this.accountType = accountType;
        initComponents();
        setLocationRelativeTo(parent);
    }

    private static String getAccountTypeDisplay(String accountType) {
        return accountType.equals("CORRENTE") ? "Conta Corrente" : "Conta Poupança";
    }

    private void initComponents() {
        setLayout(new MigLayout("wrap, fill, insets 20", "[grow]", "[]10[]5[]10[]10[]"));
        setSize(400, 300);
        setResizable(false);

        // Título
        JLabel titleLabel = new JLabel("Abrir " + getAccountTypeDisplay(accountType));
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(30, 40, 112));
        add(titleLabel, "align center, wrap");

        // Instruções
        JLabel instructionLabel = new JLabel("Digite um PIN de 4 dígitos para sua nova conta");
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        instructionLabel.setForeground(Color.GRAY);
        add(instructionLabel, "align center, wrap");

        // Campo PIN
        JLabel pinLabel = new JLabel("PIN (4 dígitos):");
        pinLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        add(pinLabel, "growx");

        pinField = new JPasswordField();
        pinField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        pinField.setHorizontalAlignment(JTextField.CENTER);
        add(pinField, "growx, h 40!, wrap");

        // Campo Confirmar PIN
        JLabel confirmPinLabel = new JLabel("Confirmar PIN:");
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
        confirmButton.setText("Criar Conta");
        confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confirmButton.setBackground(new Color(16, 185, 129));
        confirmButton.setFocusPainted(false);
        buttonPanel.add(confirmButton, "growx");

        add(buttonPanel, "growx, span");

        // Adicionar efeitos hover
        addButtonHoverEffects();

        // Focar no campo PIN
        SwingUtilities.invokeLater(() -> pinField.requestFocus());
    }

    private void addButtonHoverEffects() {
        confirmButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                confirmButton.setBackground(new Color(14, 159, 110));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                confirmButton.setBackground(new Color(16, 185, 129));
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

    public String getPin() {
        return new String(pinField.getPassword());
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
        pinField.setText("");
        confirmPinField.setText("");
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
        pinField.requestFocus();
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
}
