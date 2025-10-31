package view;

import model.entities.User;
import model.exceptions.InvalidCredentialsException;
import model.services.AuthenticationService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;

public class LoginFrame extends JFrame {

    private final AuthenticationService authService;

    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JButton loginButton;

    // Cores inspiradas no design moderno do Banco Digital
    private static final Color PRIMARY_COLOR = new Color(50, 150, 250); // Azul
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Fundo Claro
    private static final String EMAIL_PLACEHOLDER = "Email ou Nº de Conta";
    private static final String PASSWORD_PLACEHOLDER = "Senha (4 ou mais dígitos)";

    public LoginFrame(AuthenticationService authService) {
        this.authService = authService;

        // 1. Configuração da Janela
        setTitle("Banco Digital - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 300);

        // 2. Painel Principal (Layout Vertical)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // 3. Título
        JLabel titleLabel = new JLabel("Bem-vindo de volta", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // 4. Painel de Input (GridLayout 4x1)
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        formPanel.setBackground(BACKGROUND_COLOR);

        emailField = createStyledTextField(EMAIL_PLACEHOLDER);
        passwordField = createStyledPasswordField(PASSWORD_PLACEHOLDER);
        loginButton = createStyledButton("ENTRAR NA CONTA");

        formPanel.add(emailField);
        formPanel.add(passwordField);
        formPanel.add(new JLabel());
        formPanel.add(loginButton);

        // 5. Adicionar Componentes ao Painel Principal
        mainPanel.add(titleLabel);
        mainPanel.add(formPanel);

        add(mainPanel, BorderLayout.CENTER);

        loginButton.addActionListener(new LoginActionListener());

        setLocationRelativeTo(null);
    }

    // Métodos Auxiliares de Estilo
    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        field.setText(placeholder);
        field.setForeground(Color.GRAY);

        // Lógica de Placeholder
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
        return field;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField();
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        // A segurança do JPasswordField impede que o texto do placeholder seja fácil de implementar
        // Vamos usar um placeholder básico na implementação acima.
        return field;
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


    // 2. Lógica de Ação com Redirecionamento por Tipo de Usuário
    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            // Ignorar texto de placeholder para a validação
            if (email.equals(EMAIL_PLACEHOLDER) || password.equals(PASSWORD_PLACEHOLDER) || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginFrame.this,
                        "Por favor, preencha o Email/Nº de Conta e a Senha.",
                        "Erro",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Execução da lógica de autenticação (idealmente num SwingWorker para não bloquear)
            try {
                User user = authService.login(email, password);

                // --- VERIFICAÇÃO DO TIPO DE USUÁRIO E REDIRECIONAMENTO ---
                String userType = user.getUserType(); // Requer que o método getUserType() exista na classe User

                dispose(); // Fechar a janela de login

                if ("CUSTOMER".equalsIgnoreCase(userType)) {
                    // 1. Abrir o Dashboard do Cliente
                    new ClientDashboardFrame(user).setVisible(true);

                } else if ("EMPLOYEE".equalsIgnoreCase(userType)) {
                    // 2. Abrir o Painel de Gestão de Funcionários
                    new EmployeeManagementFrame(user).setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this,
                            "Tipo de usuário não reconhecido.",
                            "Erro de Acesso",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (InvalidCredentialsException ex) {
                JOptionPane.showMessageDialog(LoginFrame.this,
                        "Credenciais inválidas: Email ou senha incorreta.",
                        "Falha de Login",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(LoginFrame.this,
                        "Erro interno. Tente novamente.",
                        "Erro Crítico",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}