package view;

import model.entities.Customer;
import model.services.CustomerService;
import model.exceptions.CustomerRegistrationException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerRegistrationFrame extends JFrame {

    private final CustomerService customerService;

    // Campos de Input
    private final JTextField firstNameField = new JTextField(20);
    private final JTextField lastNameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JTextField phoneField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JTextField addressField = new JTextField(20);
    private final JTextField biNumberField = new JTextField(20);
    private final JTextField nuitField = new JTextField(20);
    private final JTextField passportField = new JTextField(20); // Assumindo que o passport é opcional

    // Constantes de Estilo
    private static final Color PRIMARY_COLOR = new Color(50, 150, 250);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);

    public CustomerRegistrationFrame(CustomerService customerService) {
        this.customerService = customerService;

        // 1. Configuração da Janela
        setTitle("Banco Digital - Cadastro de Novo Cliente");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha só esta janela
        setSize(600, 750);

        // 2. Painel Principal (Com Scroll, caso a janela seja pequena)
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);

        // 3. Título
        JLabel titleLabel = new JLabel("Registo de Novo Cliente", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // 4. Painel do Formulário
        JPanel formPanel = createFormPanel();

        // Adiciona Scroll Pane ao formulário, se necessário
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 5. Botão de Registo
        JButton registerButton = createStyledButton("Registar Cliente");
        registerButton.addActionListener(new RegisterActionListener());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 20, 0));
        buttonPanel.add(registerButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setLocationRelativeTo(null);
    }

    // =========================================================================
    // --- 4. CRIAÇÃO DOS CAMPOS DO FORMULÁRIO ---
    // =========================================================================
    private JPanel createFormPanel() {
        // Usamos GridBagLayout para um controlo mais fino sobre os rótulos e campos
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 50, 10, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5); // Espaçamento entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Função auxiliar para adicionar Rótulo e Campo
        row = addField(panel, gbc, row, "Nome Próprio:", firstNameField);
        row = addField(panel, gbc, row, "Apelido:", lastNameField);
        row = addField(panel, gbc, row, "Email:", emailField);
        row = addField(panel, gbc, row, "Telefone:", phoneField);
        row = addField(panel, gbc, row, "Endereço (Morada):", addressField);

        // Separador para Credenciais
        row = addSeparator(panel, gbc, row, "Credenciais de Acesso");
        row = addField(panel, gbc, row, "Senha (Mín. 8 dígitos):", passwordField);

        // Separador para Documentos de Cliente
        row = addSeparator(panel, gbc, row, "Documentos de Identificação");
        row = addField(panel, gbc, row, "Nº do BI/ID:", biNumberField);
        row = addField(panel, gbc, row, "Nº do NUIT:", nuitField);
        row = addField(panel, gbc, row, "Nº do Passaporte (Opcional):", passportField);

        return panel;
    }

    // Método auxiliar para adicionar Label e Campo
    private int addField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent component) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1.0;
        panel.add(component, gbc);

        return row + 1;
    }

    // Método auxiliar para adicionar um Separador/Título
    private int addSeparator(JPanel panel, GridBagConstraints gbc, int row, String title) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2; // Ocupa duas colunas
        gbc.weightx = 1.0;

        JLabel separator = new JLabel(title);
        separator.setFont(new Font("Arial", Font.BOLD, 14));
        separator.setBorder(new EmptyBorder(15, 0, 5, 0));
        panel.add(separator, gbc);

        gbc.gridwidth = 1; // Volta ao normal
        return row + 1;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setFocusPainted(false);
        return button;
    }

    // =========================================================================
    // --- LÓGICA DE AÇÃO (Registo) ---
    // =========================================================================
    private class RegisterActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            // 1. Coletar dados
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String password = new String(passwordField.getPassword());
            String address = addressField.getText();
            String biNumber = biNumberField.getText();
            String nuit = nuitField.getText();
            String passportNumber = passportField.getText();

            // 2. Validação simples (pode ser mais robusta)
            if (firstName.isEmpty() || email.isEmpty() || password.length() < 8 || biNumber.isEmpty()) {
                JOptionPane.showMessageDialog(CustomerRegistrationFrame.this,
                        "Por favor, preencha todos os campos obrigatórios (Nome, Email, Senha, BI) e a senha deve ter 8 ou mais caracteres.",
                        "Erro de Validação",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 3. Criar objeto Customer
            Customer newCustomer = new Customer(
                    firstName, lastName, email, phone, password, address,
                    biNumber, nuit, passportNumber.isEmpty() ? null : passportNumber // Passa null se vazio
            );

            // 4. Chamar o Serviço de Registo
            try {
                customerService.registerCustomer(newCustomer);

                JOptionPane.showMessageDialog(CustomerRegistrationFrame.this,
                        "Registo concluído! O Cliente " + newCustomer.getFullName() + " foi criado.",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);

                dispose(); // Fechar a janela de registo

            } catch (CustomerRegistrationException ex) {
                JOptionPane.showMessageDialog(CustomerRegistrationFrame.this,
                        "Falha no registo: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(CustomerRegistrationFrame.this,
                        "Erro Crítico no Sistema: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}