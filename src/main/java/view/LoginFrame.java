package view;

import model.entities.User;
import model.exceptions.InvalidCredentialsException;
import model.services.AuthenticationService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

    // NOTE: O authService será injetado pelo main.App.java
    private final AuthenticationService authService;

    // Componentes de Input
    private final JComboBox<String> roleSelector;
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JButton loginButton;

    // Constantes de Estilo
    private static final Color PRIMARY_COLOR = new Color(50, 150, 250); // Azul
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Fundo Claro

    public LoginFrame(AuthenticationService authService) {
        this.authService = authService;

        // 1. Configuração da Janela
        setTitle("Banco Digital - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 350);

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

        // 4. Painel de Formulário (GridLayout 4 linhas x 2 colunas para rótulo e campo)
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBackground(BACKGROUND_COLOR);

        // --- SELETOR DE PAPEL ---
        roleSelector = new JComboBox<>(new String[]{"CLIENTE", "FUNCIONÁRIO"});
        roleSelector.setBackground(Color.WHITE);
        formPanel.add(new JLabel("Acessar como:"));
        formPanel.add(roleSelector);

        // --- CAMPOS DE INPUT COM RÓTULOS ---
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = createStyledButton("ENTRAR NA CONTA");

        formPanel.add(new JLabel("Email/Conta:"));
        formPanel.add(emailField);

        formPanel.add(new JLabel("Senha:"));
        formPanel.add(passwordField);

        formPanel.add(new JLabel("")); // Espaçador
        formPanel.add(loginButton);


        // 5. Adicionar Componentes ao Painel Principal
        mainPanel.add(titleLabel);
        mainPanel.add(formPanel);

        // --- LINK DE REGISTO (Cadastre-se aqui) ---
        JLabel registerLink = new JLabel("<html>Não tem uma conta? <u>Cadastre-se aqui</u></html>");
        registerLink.setFont(new Font("Arial", Font.PLAIN, 12));
        registerLink.setForeground(PRIMARY_COLOR);
        registerLink.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        registerLink.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // ATENÇÃO: O CustomerService deve ser passado, mas é 'null' para fins de compilação da GUI
                new CustomerRegistrationFrame(null).setVisible(true);
            }
        });

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espaço
        mainPanel.add(registerLink);

        add(mainPanel, BorderLayout.CENTER);

        loginButton.addActionListener(new LoginActionListener());

        setLocationRelativeTo(null);
    }

    // Método auxiliar de estilo (mantido)
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setFocusPainted(false);
        return button;
    }


    // Lógica de Ação com Redirecionamento e Autorização
    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedRole = (String) roleSelector.getSelectedItem();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginFrame.this,
                        "Por favor, preencha o Email/Nº de Conta e a Senha.",
                        "Erro",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // 1. Tenta fazer o login usando o serviço (que busca o userType no DB)
                User user = authService.login(email, password); // Requer authService != null

                // 2. Obtém o tipo de usuário do DB
                String userType = user.getUserType();

                // 3. Verificação de Autorização: O papel do DB deve ser consistente com o papel escolhido na tela
                if (!selectedRole.equalsIgnoreCase(userType)) {
                    JOptionPane.showMessageDialog(LoginFrame.this,
                            "A autenticação foi bem-sucedida, mas o seu perfil de '" + userType + "' não corresponde ao acesso escolhido ('" + selectedRole + "').",
                            "Erro de Autorização",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                dispose(); // Fechar a janela de login

                // 4. Redirecionamento
                if ("CLIENTE".equalsIgnoreCase(selectedRole)) {
                    // Requer a classe ClientDashboardFrame
                    new ClientDashboardFrame(user).setVisible(true);

                } else if ("FUNCIONÁRIO".equalsIgnoreCase(selectedRole)) {
                    // Requer a classe EmployeeManagementFrame
                    new EmployeeManagementFrame(user).setVisible(true);

                }

            } catch (InvalidCredentialsException ex) {
                JOptionPane.showMessageDialog(LoginFrame.this,
                        "Credenciais inválidas: Email ou senha incorreta.",
                        "Falha de Login",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(LoginFrame.this,
                        "Erro interno. Tente novamente. Certifique-se de que o Database está a correr.",
                        "Erro Crítico",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}