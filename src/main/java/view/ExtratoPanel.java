package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ExtratoPanel extends JPanel {
    private JTextField txtFrom, txtTo;
    private JButton btnFilter, btnRefresh;
    private JTable table;
    private JLabel lblStatus;

    public ExtratoPanel() {
        setLayout(new BorderLayout(12,12));
        setBackground(new Color(0xF5F7FA));
        setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        init();
    }

    private void init() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setOpaque(false);
        top.add(new JLabel("De (yyyy-MM-dd):"));
        txtFrom = new JTextField(10);
        top.add(txtFrom);
        top.add(new JLabel("Até (yyyy-MM-dd):"));
        txtTo = new JTextField(10);
        top.add(txtTo);
        btnFilter = new JButton("Filtrar");
        btnRefresh = new JButton("Atualizar");
        top.add(btnFilter);
        top.add(btnRefresh);
        add(top, BorderLayout.NORTH);

        table = new JTable(new DefaultTableModel(new Object[]{"Data","Descrição","Valor","Saldo"},0));
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);
        lblStatus = new JLabel(" ");
        bottom.add(lblStatus);
        add(bottom, BorderLayout.SOUTH);
    }

    public String getFrom() { return txtFrom.getText().trim(); }
    public String getTo() { return txtTo.getText().trim(); }
    public void setTableModel(javax.swing.table.TableModel m) { table.setModel(m); }
    public void setStatus(String s) { lblStatus.setText(s); }
    public void addFilterListener(java.awt.event.ActionListener l) { btnFilter.addActionListener(l); }
    public void addRefreshListener(java.awt.event.ActionListener l) { btnRefresh.addActionListener(l); }
    public void loadRecent() { /* controller liga */ }
}
