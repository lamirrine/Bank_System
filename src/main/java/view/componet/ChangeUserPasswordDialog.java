package view.componet;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ChangeUserPasswordDialog extends JDialog {
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private MyButton confirmButton;
    private MyButton cancelButton;

    public ChangeUserPasswordDialog(JFrame parent) {
        super(parent, "Alterar Senha do Usuário", true);
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new MigLayout("wrap, fill, insets 20", "[grow]", "[]10[]5[]10[]10[]10[]10[]"));
        setSize(500, 500);
        setResizable(false);

        // Título
        JLabel titleLabel = new JLabel("Alterar Senha do Usuário");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(30, 40, 112));
        add(titleLabel, "align center, wrap");

        // Instruções
        JLabel instructionLabel = new JLabel("Digite sua senha atual e a nova senha");
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        instructionLabel.setForeground(Color.GRAY);
        add(instructionLabel, "align center, wrap");

        // Campo Senha Atual
        JLabel currentPassLabel = new JLabel("Senha Atual:");
        currentPassLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        add(currentPassLabel, "growx");

        currentPasswordField = new JPasswordField();
        currentPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(currentPasswordField, "growx, h 40!, wrap");

        // Campo Nova Senha
        JLabel newPassLabel = new JLabel("Nova Senha:");
        newPassLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        add(newPassLabel, "growx");

        newPasswordField = new JPasswordField();
        newPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(newPasswordField, "growx, h 40!, wrap");

        // Campo Confirmar Nova Senha
        JLabel confirmPassLabel = new JLabel("Confirmar Nova Senha:");
        confirmPassLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        add(confirmPassLabel, "growx");

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(confirmPasswordField, "growx, h 40!, wrap");

        // Requisitos da senha
        JLabel requirementsLabel = new JLabel("A senha deve ter pelo menos 6 caracteres");
        requirementsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        requirementsLabel.setForeground(new Color(107, 114, 128));
        add(requirementsLabel, "align center, wrap");

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
        confirmButton.setText("Alterar Senha");
        confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confirmButton.setBackground(new Color(168, 85, 247));
        confirmButton.setFocusPainted(false);
        buttonPanel.add(confirmButton, "growx");

        add(buttonPanel, "growx, span");

        addButtonHoverEffects();

        // Focar no campo senha atual
        SwingUtilities.invokeLater(() -> currentPasswordField.requestFocus());
    }

    private void addButtonHoverEffects() {
        confirmButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                confirmButton.setBackground(new Color(147, 51, 234));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                confirmButton.setBackground(new Color(168, 85, 247));
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

    public String getCurrentPassword() {
        return new String(currentPasswordField.getPassword());
    }

    public String getNewPassword() {
        return new String(newPasswordField.getPassword());
    }

    public String getConfirmPassword() {
        return new String(confirmPasswordField.getPassword());
    }

    public void addConfirmListener(ActionListener listener) {
        confirmButton.addActionListener(listener);
    }

    public void addCancelListener(ActionListener listener) {
        cancelButton.addActionListener(listener);
    }

    public void clearFields() {
        currentPasswordField.setText("");
        newPasswordField.setText("");
        confirmPasswordField.setText("");
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
        currentPasswordField.requestFocus();
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
}
