package view;

import javax.swing.*;
import java.awt.*;

public class TransferenciaPanel extends JPanel {
    private JTextField txtOrigem, txtDestino, txtValor;
    private JPasswordField txtPin;
    private JButton btnTransfer;
    private JLabel lblStatus;

    public TransferenciaPanel() {
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

        c.gridx=0;c.gridy=0; form.add(new JLabel("Conta Origem:"), c);
        c.gridx=1; txtOrigem = new JTextField(12); form.add(txtOrigem, c);

        c.gridy=1; c.gridx=0; form.add(new JLabel("Conta Destino:"), c);
        c.gridx=1; txtDestino = new JTextField(12); form.add(txtDestino, c);

        c.gridy=2; c.gridx=0; form.add(new JLabel("Valor (MZN):"), c);
        c.gridx=1; txtValor = new JTextField(10); form.add(txtValor, c);

        c.gridy=3; c.gridx=0; form.add(new JLabel("PIN:"), c);
        c.gridx=1; txtPin = new JPasswordField(6); form.add(txtPin, c);

        add(form, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);
        btnTransfer = new JButton("Transferir");
        lblStatus = new JLabel(" ");
        bottom.add(lblStatus);
        bottom.add(btnTransfer);
        add(bottom, BorderLayout.SOUTH);
    }

    public String getOrigem() { return txtOrigem.getText().trim(); }
    public String getDestino() { return txtDestino.getText().trim(); }
    public String getValorText() { return txtValor.getText().trim(); }
    public String getPin() { return new String(txtPin.getPassword()).trim(); }
    public void setStatus(String s) { lblStatus.setText(s); }
    public void addTransferListener(java.awt.event.ActionListener l) { btnTransfer.addActionListener(l); }
}
