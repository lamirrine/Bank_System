package view.login.componet;

import view.login.fiels.Button;
import view.login.fiels.MyPasswordField;
import view.login.fiels.MyTextField;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class EmployeeLoginView extends JDialog {
    private MyTextField emailField;
    private MyPasswordField passwordField;
    private Button loginButton;
    private Button backButton;
    private JFrame parent;

    public EmployeeLoginView(JFrame parent) {
        super(parent, "Login - Funcionário", true);
        this.parent = parent;
        initializeUI();
        setLocationRelativeTo(parent);
    }

    private void initializeUI() {
        setLayout(new MigLayout("wrap, fill, insets 30", "[grow]", "[]20[]10[]10[]20[]"));
        setSize(800, 900);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Título
        JLabel titleLabel = new JLabel("Login Funcionário");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 40, 112));
        add(titleLabel, "align center, wrap");

        // Subtítulo
        JLabel subtitleLabel = new JLabel("Acesso administrativo");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);
        add(subtitleLabel, "align center, wrap");

        // Campo Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        add(emailLabel, "growx");

        emailField = new MyTextField();
        emailField.setHint("email@banco.com");
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(emailField, "growx, h 40!, wrap");

        // Campo Senha
        JLabel passwordLabel = new JLabel("Senha:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        add(passwordLabel, "growx");

        passwordField = new MyPasswordField();
        passwordField.setHint("Digite sua senha");
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(passwordField, "growx, h 40!, wrap");

        // Painel de botões
        JPanel buttonPanel = new JPanel(new MigLayout("insets 0", "[grow][grow]", "[]"));
        buttonPanel.setOpaque(false);

        backButton = new Button();
        backButton.setText("Voltar");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setBackground(new Color(107, 114, 128));
        backButton.setFocusPainted(false);
        buttonPanel.add(backButton, "growx");

        loginButton = new Button();
        loginButton.setText("Entrar");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBackground(new Color(39, 174, 96));
        loginButton.setFocusPainted(false);
        buttonPanel.add(loginButton, "growx");

        add(buttonPanel, "growx, span");

        addButtonHoverEffects();

        // Focar no campo email
        SwingUtilities.invokeLater(() -> emailField.requestFocus());
    }

    private void addButtonHoverEffects() {
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(33, 150, 83));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(39, 174, 96));
            }
        });

        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(75, 85, 99));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(107, 114, 128));
            }
        });
    }

    public String getEmail() {
        return emailField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    public void clearFields() {
        emailField.setText("");
        passwordField.setText("");
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro de Login", JOptionPane.ERROR_MESSAGE);
        emailField.requestFocus();
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (parent != null) {
            parent.setVisible(true);
        }
    }
}