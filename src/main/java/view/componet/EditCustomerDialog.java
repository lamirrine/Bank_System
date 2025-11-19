package view.componet;

import model.entities.Customer;
import net.miginfocom.swing.MigLayout;
import view.login.fiels.MyTextField;

import javax.swing.*;
import java.awt.*;

public class EditCustomerDialog extends JDialog {
    private MyTextField firstNameField;
    private MyTextField lastNameField;
    private MyTextField emailField;
    private MyTextField phoneField;
    private MyTextField addressField;
    private MyTextField biField;
    private MyTextField nuitField;
    private MyTextField passportField;
    private MyButton saveBtn;
    private MyButton cancelBtn;

    private Customer customer;
    private boolean saved = false;

    public EditCustomerDialog(JFrame parent, Customer customer) {
        super(parent, "Editar Cliente - " + customer.getFullName(), true);
        this.customer = customer;
        initializeUI();
        loadCustomerData();
        setLocationRelativeTo(parent);
    }

    private void initializeUI() {
        setLayout(new MigLayout("wrap, fill, insets 20", "[grow]", "[]20[]10[]10[]"));
        setSize(500, 600);
        setResizable(false);

        // Título
        JLabel titleLabel = new JLabel("Editar Cliente");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(30, 40, 112));
        add(titleLabel, "align center, wrap");

        // Campos do formulário
        add(createFormPanel(), "grow, wrap");

        // Botões
        JPanel buttonPanel = new JPanel(new MigLayout("insets 0", "[grow][grow]", "[]"));
        buttonPanel.setOpaque(false);

        cancelBtn = new MyButton();
        cancelBtn.setText("Cancelar");
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelBtn.setBackground(new Color(107, 114, 128));
        cancelBtn.setFocusPainted(false);

        saveBtn = new MyButton();
        saveBtn.setText("Salvar Alterações");
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveBtn.setBackground(new Color(16, 185, 129));
        saveBtn.setFocusPainted(false);

        buttonPanel.add(cancelBtn, "growx");
        buttonPanel.add(saveBtn, "growx");

        add(buttonPanel, "growx");

        // Listeners
        cancelBtn.addActionListener(e -> dispose());
        saveBtn.addActionListener(e -> saveCustomer());
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new MigLayout("wrap 2, fillx", "[][grow]", "[]10[]10[]10[]10[]10[]10[]10[]"));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Nome
        JLabel firstNameLabel = new JLabel("Nome:*");
        firstNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        firstNameField = new MyTextField();
        firstNameField.setHint("Primeiro nome");
        formPanel.add(firstNameLabel);
        formPanel.add(firstNameField, "growx, wrap");

        // Apelido
        JLabel lastNameLabel = new JLabel("Apelido:*");
        lastNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lastNameField = new MyTextField();
        lastNameField.setHint("Último nome");
        formPanel.add(lastNameLabel);
        formPanel.add(lastNameField, "growx, wrap");

        // Email
        JLabel emailLabel = new JLabel("Email:*");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        emailField = new MyTextField();
        emailField.setHint("email@exemplo.com");
        formPanel.add(emailLabel);
        formPanel.add(emailField, "growx, wrap");

        // Telefone
        JLabel phoneLabel = new JLabel("Telefone:");
        phoneLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        phoneField = new MyTextField();
        phoneField.setHint("+258...");
        formPanel.add(phoneLabel);
        formPanel.add(phoneField, "growx, wrap");

        // Endereço
        JLabel addressLabel = new JLabel("Endereço:");
        addressLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addressField = new MyTextField();
        addressField.setHint("Endereço completo");
        formPanel.add(addressLabel);
        formPanel.add(addressField, "growx, wrap");

        // BI
        JLabel biLabel = new JLabel("Nº do BI:");
        biLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        biField = new MyTextField();
        biField.setHint("Número do bilhete de identidade");
        formPanel.add(biLabel);
        formPanel.add(biField, "growx, wrap");

        // NUIT
        JLabel nuitLabel = new JLabel("NUIT:");
        nuitLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nuitField = new MyTextField();
        nuitField.setHint("Número de identificação única");
        formPanel.add(nuitLabel);
        formPanel.add(nuitField, "growx, wrap");

        // Passaporte
        JLabel passportLabel = new JLabel("Passaporte:");
        passportLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passportField = new MyTextField();
        passportField.setHint("Para estrangeiros");
        formPanel.add(passportLabel);
        formPanel.add(passportField, "growx, wrap");

        return formPanel;
    }

    private void loadCustomerData() {
        firstNameField.setText(customer.getFirstName());
        lastNameField.setText(customer.getLastName());
        emailField.setText(customer.getEmail());
        phoneField.setText(customer.getPhone() != null ? customer.getPhone() : "");
        addressField.setText(customer.getAddress() != null ? customer.getAddress() : "");
        biField.setText(customer.getBiNumber() != null ? customer.getBiNumber() : "");
        nuitField.setText(customer.getNuit() != null ? customer.getNuit() : "");
        passportField.setText(customer.getPassportNumber() != null ? customer.getPassportNumber() : "");
    }

    private void saveCustomer() {
        // Validações
        if (firstNameField.getText().trim().isEmpty() || lastNameField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome, apelido e email são obrigatórios!", "Campos Obrigatórios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Atualizar objeto customer
            customer.setFirstName(firstNameField.getText().trim());
            customer.setLastName(lastNameField.getText().trim());
            customer.setEmail(emailField.getText().trim());
            customer.setPhone(phoneField.getText().trim());
            customer.setAddress(addressField.getText().trim());
            customer.setBiNumber(biField.getText().trim());
            customer.setNuit(nuitField.getText().trim());
            customer.setPassportNumber(passportField.getText().trim());

            saved = true;
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() {
        return saved;
    }

    public Customer getUpdatedCustomer() {
        return customer;
    }
}