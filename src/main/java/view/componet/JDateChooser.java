package view.componet;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JDateChooser extends JPanel {
    private JTextField dateField;
    private JButton calendarButton;
    private Date selectedDate;

    public JDateChooser() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(120, 30));

        dateField = new JTextField();
        dateField.setEditable(false);
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        calendarButton = new JButton("📅");
        calendarButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        calendarButton.setPreferredSize(new Dimension(30, 30));
        calendarButton.setFocusPainted(false);
        calendarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        calendarButton.addActionListener(e -> showDatePickerDialog());

        add(dateField, BorderLayout.CENTER);
        add(calendarButton, BorderLayout.EAST);
    }

    private void showDatePickerDialog() {
        // Diálogo simples para seleção de data
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setValue(selectedDate != null ? selectedDate : new Date());

        int result = JOptionPane.showConfirmDialog(this, dateSpinner,
                "Selecionar Data", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            setDate((Date) dateSpinner.getValue());
        }
    }

    public void setDate(Date date) {
        this.selectedDate = date;
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            dateField.setText(sdf.format(date));
        } else {
            dateField.setText("");
        }
    }

    public Date getDate() {
        return selectedDate;
    }

    public void setDateFormatString(String format) {
        // Para compatibilidade com a interface esperada
    }
}