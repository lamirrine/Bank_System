// CreateAccountDialog.java - Diálogo para criar nova conta
package view.componet;

import model.entities.Customer;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.List;

public class CreateAccountDialog extends JDialog {
    private JComboBox<String> customerCombo;
    private JComboBox<String> accountTypeCombo;
    private JPasswordField pinField;
    private JPasswordField confirmPinField;
    private MyButton confirmBtn;
    private MyButton cancelBtn;
    private JLabel errorLabel;
    private List<Customer> customers;
    private Customer selectedCustomer;

    public CreateAccountDialog(List<Customer> customerList) {
        super((JFrame) null, "Criar Nova Conta", true);
        this.customers = customerList;
        this.selectedCustomer = null;

        initializeUI();
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(550, 600);
        setLocationRelativeTo(null); // Centralizar na tela
    }

    private void initializeUI() {
        setLayout(new MigLayout("fill, insets 20", "[grow]", "[][][][] 20[][]"));
        setBackground(new java.awt.Color(229, 231, 235));

        // Título
        JLabel titleLabel = new JLabel("Criar Nova Conta");
        titleLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        titleLabel.setForeground(new java.awt.Color(15, 23, 42));
        add(titleLabel, "growx, wrap");

        // Label de erro
        errorLabel = new JLabel("");
        errorLabel.setForeground(new java.awt.Color(239, 68, 68));
        errorLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        add(errorLabel, "growx, wrap");

        // --- CLIENTE ---
        JLabel customerLabel = new JLabel("Selecione o Cliente:");
        customerLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        add(customerLabel, "growx, wrap");

        // Preencher combo com clientes
        String[] customerNames = new String[customers.size()];
        for (int i = 0; i < customers.size(); i++) {
            Customer c = customers.get(i);
            customerNames[i] = "[ID: " + c.getUserId() + "] " + c.getFirstName() + " " + c.getLastName() +
                    " (" + c.getEmail() + ")";
        }

        customerCombo = new JComboBox<>(customerNames);
        customerCombo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        add(customerCombo, "growx, wrap");

        // --- TIPO DE CONTA ---
        JLabel typeLabel = new JLabel("Tipo de Conta:");
        typeLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        add(typeLabel, "growx, wrap");

        accountTypeCombo = new JComboBox<>(new String[]{"Corrente", "Poupança"});
        accountTypeCombo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        add(accountTypeCombo, "growx, wrap");

        // --- PIN ---
        JLabel pinLabel = new JLabel("PIN da Transação (4 dígitos):");
        pinLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        add(pinLabel, "growx, wrap");

        pinField = new JPasswordField();
        pinField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        add(pinField, "growx, wrap");

        // --- CONFIRMAR PIN ---
        JLabel confirmPinLabel = new JLabel("Confirmar PIN:");
        confirmPinLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        add(confirmPinLabel, "growx, wrap");

        confirmPinField = new JPasswordField();
        confirmPinField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        add(confirmPinField, "growx, wrap");

        // --- BOTÕES ---
        confirmBtn = new MyButton();
        confirmBtn.setText("Criar Conta");
        confirmBtn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        confirmBtn.setBackground(new java.awt.Color(16, 185, 129));

        cancelBtn = new MyButton();
        cancelBtn.setText("Cancelar");
        cancelBtn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        cancelBtn.setBackground(new java.awt.Color(107, 114, 128));

        add(confirmBtn, "growx, split 2");
        add(cancelBtn, "growx");
    }

    public void addConfirmListener(java.awt.event.ActionListener listener) {
        confirmBtn.addActionListener(listener);
    }

    public void addCancelListener(java.awt.event.ActionListener listener) {
        cancelBtn.addActionListener(listener);
    }

    public int getSelectedCustomerId() {
        if (customerCombo.getSelectedIndex() >= 0) {
            return customers.get(customerCombo.getSelectedIndex()).getUserId();
        }
        return -1;
    }

    public Customer getSelectedCustomer() {
        if (customerCombo.getSelectedIndex() >= 0) {
            return customers.get(customerCombo.getSelectedIndex());
        }
        return null;
    }

    public String getAccountType() {
        String selected = (String) accountTypeCombo.getSelectedItem();
        return selected.equals("Corrente") ? "CORRENTE" : "POUPANCA";
    }

    public String getPin() {
        return new String(pinField.getPassword());
    }

    public String getConfirmPin() {
        return new String(confirmPinField.getPassword());
    }

    public void showError(String message) {
        errorLabel.setText("❌ " + message);
        errorLabel.setForeground(new java.awt.Color(239, 68, 68));
    }

    public void showSuccess(String message) {
        errorLabel.setText("✓ " + message);
        errorLabel.setForeground(new java.awt.Color(16, 185, 129));
    }

    public void clearFields() {
        pinField.setText("");
        confirmPinField.setText("");
        errorLabel.setText("");
    }
}