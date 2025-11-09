package view;

import javax.swing.*;
import java.awt.*;

/**
 * Painel de Depósito com layout fiel ao PDF.
 * Expor addConfirmListener para o controller.
 */
public class DepositoPanel extends JPanel {
    private JTextField txtConta;
    private JTextField txtValor;
    private JComboBox<String> cbMetodo;
    private JButton btnConfirm;
    private JLabel lblStatus;

    public DepositoPanel() {
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
        c.anchor = GridBagConstraints.WEST;

        c.gridx=0; c.gridy=0; form.add(new JLabel("Conta destino:"), c);
        c.gridx=1; txtConta = new JTextField(18); form.add(txtConta, c);

        c.gridy=1; c.gridx=0; form.add(new JLabel("Valor (MZN):"), c);
        c.gridx=1; txtValor = new JTextField(12); form.add(txtValor, c);

        c.gridy=2; c.gridx=0; form.add(new JLabel("Método:"), c);
        c.gridx=1; cbMetodo = new JComboBox<>(new String[]{"Dinheiro","Transferência","Cheque"}); form.add(cbMetodo, c);

        add(form, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);
        btnConfirm = new JButton("Confirmar Depósito");
        lblStatus = new JLabel(" ");
        bottom.add(lblStatus);
        bottom.add(btnConfirm);
        add(bottom, BorderLayout.SOUTH);
    }

    public String getConta() { return txtConta.getText().trim(); }
    public String getValorText() { return txtValor.getText().trim(); }
    public String getMetodo() { return (String) cbMetodo.getSelectedItem(); }
    public void setStatus(String s) { lblStatus.setText(s); }
    public void addConfirmListener(java.awt.event.ActionListener l) { btnConfirm.addActionListener(l); }
}
