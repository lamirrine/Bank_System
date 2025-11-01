// view/LoginView.java
package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton backButton;
    private JCheckBox rememberCheckbox;

    public LoginView(String userType) {
        initializeUI(userType);
    }

    private void initializeUI(String userType) {
        setTitle("Banco Digital - Login " + userType);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);

        // Layout principal
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        mainPanel.setBackground(Color.WHITE);

        // Header com botão voltar
        JPanel headerPanel = createHeaderPanel(userType);

        // Formulário de login
        JPanel formPanel = createFormPanel();

        // Painel de recursos
        JPanel featuresPanel = createFeaturesPanel();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(featuresPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel(String userType) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Botão voltar
        backButton = new JButton("← Voltar");
        backButton.setFont(new Font("Arial", Font.PLAIN, 12));
        backButton.setForeground(new Color(0, 102, 204));
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        backButton.setContentAreaFilled(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Título centralizado
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 5, 5));
        titlePanel.setBackground(Color.WHITE);

        JLabel mainTitle = new JLabel("Banco Digital", JLabel.CENTER);
        mainTitle.setFont(new Font("Arial", Font.BOLD, 24));
        mainTitle.setForeground(Color.BLACK);

        JLabel subTitle = new JLabel("Sistema Bancário Integrado", JLabel.CENTER);
        subTitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subTitle.setForeground(Color.GRAY);

        titlePanel.add(mainTitle);
        titlePanel.add(subTitle);

        panel.add(backButton, BorderLayout.WEST);
        panel.add(titlePanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        // Título "Bem-vindo de volta"
        JLabel welcomeLabel = new JLabel("Bem-vindo de volta");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.BLACK);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campo Email (AGORA COM EMAIL)
        JPanel emailPanel = createFieldPanel("Email", emailField = new JTextField());

        // Campo Senha
        JPanel passwordPanel = createFieldPanel("Senha", passwordField = new JPasswordField());
        passwordField.setEchoChar('•');

        // Checkbox "Lembrar minha conta"
        rememberCheckbox = new JCheckBox("Lembrar minha conta");
        rememberCheckbox.setFont(new Font("Arial", Font.PLAIN, 12));
        rememberCheckbox.setBackground(Color.WHITE);
        rememberCheckbox.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Botão "Entrar na Conta"
        loginButton = new JButton("Entrar na Conta");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Link "Não tem uma conta? Cadastre-se!"
        JPanel registerPanel = new JPanel();
        registerPanel.setBackground(Color.WHITE);
        JLabel registerLabel = new JLabel("Não tem uma conta? Cadastre-se!");
        registerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        registerLabel.setForeground(new Color(0, 102, 204));
        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerPanel.add(registerLabel);

        // Espaçamento
        panel.add(welcomeLabel);
        panel.add(Box.createVerticalStrut(30));
        panel.add(emailPanel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(passwordPanel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(rememberCheckbox);
        panel.add(Box.createVerticalStrut(30));
        panel.add(loginButton);
        panel.add(Box.createVerticalStrut(20));
        panel.add(registerPanel);

        return panel;
    }

    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(Color.BLACK);

        field.setPreferredSize(new Dimension(300, 40));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFeaturesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Adicionando os recursos
        panel.add(createFeatureItem("Segurança Máxima", "criptografia de ponta a ponta"));
        panel.add(Box.createVerticalStrut(8));
        panel.add(createFeatureItem("Suporte 24/7", "Atendimento sempre disponível"));
        panel.add(Box.createVerticalStrut(8));
        panel.add(createFeatureItem("Transações Rápidas", "Processamento instantâneo"));
        panel.add(Box.createVerticalStrut(8));
        panel.add(createFeatureItem("Trava Zero", "Alertas de operações em conta"));

        return panel;
    }

    private JPanel createFeatureItem(String title, String description) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 11));
        titleLabel.setForeground(Color.BLACK);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        descLabel.setForeground(Color.GRAY);

        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(descLabel, BorderLayout.CENTER);

        return panel;
    }

    // MÉTODOS PÚBLICOS PARA ACESSAR OS CAMPOS
    public String getEmail() {
        return emailField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public boolean isRememberAccount() {
        return rememberCheckbox.isSelected();
    }

    // LISTENERS
    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }
}