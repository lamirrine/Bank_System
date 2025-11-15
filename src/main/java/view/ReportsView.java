// ReportsView.java
package view;

import net.miginfocom.swing.MigLayout;
import view.componet.MyButton;

import javax.swing.*;
import java.awt.*;

public class ReportsView extends JPanel {
    private JComboBox<String> reportType;
    private JComboBox<String> periodFilter;
    private JTextField dateFromField;
    private JTextField dateToField;
    private MyButton generateBtn;
    private MyButton exportBtn;
    private MyButton backBtn;
    private JTextArea reportArea;

    public ReportsView() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new MigLayout("fill, insets 20", "[grow]", "[][grow][]"));
        setBackground(new Color(229, 231, 235));

        // Header
        add(createHeader(), "growx, wrap");

        // Main content
        add(createMainContent(), "grow, wrap");

        // Footer
        add(createFooter(), "growx");
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new MigLayout("fill, insets 0 0 15 0", "[grow][]", "[]"));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));

        JLabel titleLabel = new JLabel("Relatórios e Estatísticas");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(15, 23, 42));

        // Filters panel
        JPanel filterPanel = new JPanel(new MigLayout("insets 0", "[][][][][][][]", "[]"));
        filterPanel.setBackground(Color.WHITE);

        JLabel reportLabel = new JLabel("Relatório:");
        reportLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        reportType = new JComboBox<>(new String[]{
                "Transações Diárias", "Transações Mensais", "Clientes Ativos",
                "Contas por Tipo", "Movimento Financeiro", "Performance"
        });
        reportType.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel periodLabel = new JLabel("Período:");
        periodLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        periodFilter = new JComboBox<>(new String[]{"Personalizado", "Hoje", "Esta Semana", "Este Mês", "Este Ano"});
        periodFilter.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel dateFromLabel = new JLabel("De:");
        dateFromLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        dateFromField = new JTextField(8);
        dateFromField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateFromField.setText("01/01/2024");

        JLabel dateToLabel = new JLabel("Até:");
        dateToLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        dateToField = new JTextField(8);
        dateToField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateToField.setText("31/12/2024");

        generateBtn = new MyButton();
        generateBtn.setText("Gerar");
        generateBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        generateBtn.setBackground(new Color(59, 130, 246));

        filterPanel.add(reportLabel);
        filterPanel.add(reportType);
        filterPanel.add(periodLabel);
        filterPanel.add(periodFilter);
        filterPanel.add(dateFromLabel);
        filterPanel.add(dateFromField);
        filterPanel.add(dateToLabel);
        filterPanel.add(dateToField);
        filterPanel.add(generateBtn);

        header.add(titleLabel, "grow");
        header.add(filterPanel);

        return header;
    }

    private JPanel createMainContent() {
        JPanel content = new JPanel(new MigLayout("fill, insets 0", "[grow]", "[grow]"));
        content.setBackground(new Color(229, 231, 235));

        // Report panel
        JPanel reportPanel = new JPanel(new MigLayout("fill", "[grow]", "[grow]"));
        reportPanel.setBackground(Color.WHITE);
        reportPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        reportArea = new JTextArea();
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        reportArea.setEditable(false);
        reportArea.setText("Selecione um tipo de relatório e clique em 'Gerar' para visualizar os dados...");

        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));

        reportPanel.add(scrollPane, "grow");

        content.add(reportPanel, "grow");

        return content;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new MigLayout("insets 20 0 0 0", "[grow][]", "[]"));
        footer.setBackground(new Color(229, 231, 235));

        backBtn = new MyButton();
        backBtn.setText("Voltar");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setBackground(new Color(107, 114, 128));

        exportBtn = new MyButton();
        exportBtn.setText("Exportar PDF");
        exportBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        exportBtn.setBackground(new Color(239, 68, 68));

        footer.add(backBtn);
        footer.add(exportBtn);

        return footer;
    }

    public void setReportContent(String content) {
        reportArea.setText(content);
    }

    // Getters
    public MyButton getBackBtn() { return backBtn; }
    public MyButton getGenerateBtn() { return generateBtn; }
    public MyButton getExportBtn() { return exportBtn; }
    public String getReportType() { return (String) reportType.getSelectedItem(); }
    public String getPeriodFilter() { return (String) periodFilter.getSelectedItem(); }
    public String getDateFrom() { return dateFromField.getText(); }
    public String getDateTo() { return dateToField.getText(); }
}