// CreateAccountPinView.java
package view.login.componet;

import net.miginfocom.swing.MigLayout;
import view.login.fiels.Button;
import view.login.fiels.MyPasswordField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CreateAccountPinView extends JDialog {
    private MyPasswordField pinField;
    private MyPasswordField confirmPinField;
    private Button confirmButton;
    private Button cancelButton;

    public CreateAccountPinView(JFrame parent) {
        super(parent, "Definir PIN da Conta", true);
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new MigLayout("wrap, fill", "[center]", "push[]20[]10[]10[]20[]push"));
        setSize(400, 300);
        setResizable(false);

        // Título
        JLabel titleLabel = new JLabel("Definir PIN da Conta");
        titleLabel.setFont(new Font("sansserif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(30, 40, 112));
        add(titleLabel);

        // Instruções
        JLabel instructionLabel = new JLabel("Digite um PIN de 4 dígitos para sua conta corrente");
        instructionLabel.setFont(new Font("sansserif", Font.PLAIN, 14));
        instructionLabel.setForeground(Color.GRAY);
        add(instructionLabel);

        // Campo PIN
        pinField = new MyPasswordField();
        pinField.setHint("PIN (4 dígitos)");
        add(pinField, "w 60%");

        // Campo Confirmar PIN
        confirmPinField = new MyPasswordField();
        confirmPinField.setHint("Confirmar PIN");
        add(confirmPinField, "w 60%");

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout());

        confirmButton = new Button();
        confirmButton.setBackground(new Color(30, 44, 121));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setText("Confirmar");
        buttonPanel.add(confirmButton);

        cancelButton = new Button();
        cancelButton.setBackground(Color.GRAY);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setText("Cancelar");
        buttonPanel.add(cancelButton);

        add(buttonPanel, "wrap");
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
    }
}