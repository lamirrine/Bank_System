package view;

import javax.swing.*;
import java.awt.*;

/**
 * Formulário de cadastro simples (nome, email, conta). Controller faz insert.
 */
public class CreateAccountsPanel extends JPanel {
    private JTextField txtNome, txtEmail, txtConta;
    private JButton btnCadastrar;
    private JLabel lblStatus;

    public CreateAccountsPanel() {
        setLayout(new BorderLayout(12,12));
        setBackground(new Color(0xF5F7FA));
        setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        init();
    }

    private void init() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(0xF5F7FA));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10,10,10,10);

        c.gridx=0;c.gridy=0; form.add(new JLabel("Nome:"), c);
        c.gridx=1; txtNome = new JTextField(18); form.add(txtNome, c);

        c.gridy=1; c.gridx=0; form.add(new JLabel("Email:"), c);
        c.gridx=1; txtEmail = new JTextField(16); form.add(txtEmail, c);

        c.gridy=2; c.gridx=0; form.add(new JLabel("Número Conta:"), c);
        c.gridx=1; txtConta = new JTextField(12); form.add(txtConta, c);

        add(form, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);
        btnCadastrar = new JButton("Cadastrar");
        lblStatus = new JLabel(" ");
        bottom.add(lblStatus);
        bottom.add(btnCadastrar);
        add(bottom, BorderLayout.SOUTH);
    }

    public String getNome() { return txtNome.getText().trim(); }
    public String getEmail() { return txtEmail.getText().trim(); }
    public String getConta() { return txtConta.getText().trim(); }
    public void addCadastrarListener(java.awt.event.ActionListener l) { btnCadastrar.addActionListener(l); }
    public void setStatus(String s) { lblStatus.setText(s); }
}
