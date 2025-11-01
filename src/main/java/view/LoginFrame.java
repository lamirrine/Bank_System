package view;

import model.entities.User;
import model.enums.UserType;
import model.exceptions.InvalidCredentialsException;
import model.services.AuthenticationService;
import model.services.CustomerService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

    private final AuthenticationService authService;
    private final CustomerService customerService;

    private final JComboBox<String> roleSelector;
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JButton loginButton;

    private static final Color PRIMARY_COLOR = new Color(50, 150, 250);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);

    // Construtor deve aceitar AMBOS os serviços
    public LoginFrame(AuthenticationService authService, CustomerService customerService) {
        this.authService = authService;
        this.customerService = customerService;

        // Inicialização dos campos (Correção do erro 5 - Image 4)
        roleSelector = new JComboBox<>(new String[]{"CLIENTE", "FUNCIONÁRIO"});
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = createStyledButton("ENTRAR NA CONTA");

        // 1. Configuração da Janela
        setTitle("Banco Digital - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(550, 450));
        setSize(550, 450);

        // 2. Painel de Centramento (GridBagLayout para manter o formulário no centro)
        JPanel centeringWrapper = new JPanel(new GridBagLayout());
        centeringWrapper.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 40, 30, 40);
        centeringWrapper.add(createFormPanel(), gbc);

        add(centeringWrapper, BorderLayout.CENTER);

        loginButton.addActionListener(new LoginActionListener());

        setLocationRelativeTo(null);
    }

    private JPanel createFormPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(30, 40, 30, 40)
        ));

        // Título e Sub-título
        JLabel titleLabel = new JLabel("Bem-vindo de volta", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Entre na sua conta para continuar", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        mainPanel.add(subtitleLabel);

        // Painel de Inputs (GridLayout)
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBackground(Color.WHITE);

        roleSelector.setBackground(BACKGROUND_COLOR);
        inputPanel.add(new JLabel("Acessar como:"));
        inputPanel.add(roleSelector);

        inputPanel.add(new JLabel("Email/Conta:"));
        inputPanel.add(emailField);

        inputPanel.add(new JLabel("Senha:"));
        inputPanel.add(passwordField);

        inputPanel.add(new JLabel(""));
        inputPanel.add(loginButton);

        mainPanel.add(inputPanel);

        // Link de Registo
        JLabel registerLink = new JLabel("<html>Não tem uma conta? <u>Cadastre-se aqui</u></html>");
        registerLink.setFont(new Font("Arial", Font.PLAIN, 12));
        registerLink.setForeground(PRIMARY_COLOR);
        registerLink.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        registerLink.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Passa a instância injetada (Corrige NullPointerException - Image 3)
                if (customerService != null) {
                    new CustomerRegistrationFrame(customerService).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Serviço de Registo não inicializado. Verifique App.java", "Erro Crítico", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(registerLink);

        return mainPanel;
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


    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedRole = (String) roleSelector.getSelectedItem();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginFrame.this, "Por favor, preencha o Email/Nº de Conta e a Senha.", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                User user = authService.login(email, password);
                UserType userType = user.getUserType(); // DB retorna CUSTOMER ou EMPLOYEE

                // Lógica de Autorização (Correção do erro de mapeamento - Image 2)
                String expectedType = selectedRole.equals("CLIENTE") ? "CUSTOMER" : "EMPLOYEE";

                if (!expectedType.equalsIgnoreCase(String.valueOf(userType))) {
                    JOptionPane.showMessageDialog(LoginFrame.this,
                            "A autenticação foi bem-sucedida, mas o seu perfil de '" + userType + "' não corresponde ao acesso escolhido ('" + selectedRole + "').",
                            "Erro de Autorização: Perfil Incorreto",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                dispose();

                if ("CLIENTE".equalsIgnoreCase(selectedRole)) {
                    new ClientDashboardFrame(user).setVisible(true);

                } else if ("FUNCIONÁRIO".equalsIgnoreCase(selectedRole)) {
                    // Substitua por new EmployeeManagementFrame(user).setVisible(true); quando estiver pronto
                    JOptionPane.showMessageDialog(LoginFrame.this, "Redirecionando para Painel de Funcionário (Placeholder).", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                }

            } catch (InvalidCredentialsException ex) {
                JOptionPane.showMessageDialog(LoginFrame.this, "Credenciais inválidas: Email ou senha incorreta.", "Falha de Login", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(LoginFrame.this, "Erro interno. Verifique a consola.", "Erro Crítico", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}