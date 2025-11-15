// TransactionView.java
package view;

import model.entities.Transaction;
import net.miginfocom.swing.MigLayout;
import view.componet.MyButton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class TransactionView extends JPanel {
    private JTable transactionsTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> typeFilter;
    private JComboBox<String> statusFilter;
    private JTextField dateFromField;
    private JTextField dateToField;
    private MyButton searchBtn;
    private MyButton exportBtn;
    private MyButton backBtn;
    private JLabel statsLabel;

    public TransactionView() {
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

        JLabel titleLabel = new JLabel("Visualização de Transações");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(15, 23, 42));

        // Filters panel
        JPanel filterPanel = new JPanel(new MigLayout("insets 0", "[][][][][][][]", "[]"));
        filterPanel.setBackground(Color.WHITE);

        JLabel typeLabel = new JLabel("Tipo:");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        typeFilter = new JComboBox<>(new String[]{"Todos", "Depósito", "Levantamento", "Transferência"});
        typeFilter.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        statusFilter = new JComboBox<>(new String[]{"Todos", "Concluída", "Pendente", "Rejeitada"});
        statusFilter.setFont(new Font("Segoe UI", Font.PLAIN, 12));

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

        searchBtn = new MyButton();
        searchBtn.setText("Filtrar");
        searchBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchBtn.setBackground(new Color(59, 130, 246));

        filterPanel.add(typeLabel);
        filterPanel.add(typeFilter);
        filterPanel.add(statusLabel);
        filterPanel.add(statusFilter);
        filterPanel.add(dateFromLabel);
        filterPanel.add(dateFromField);
        filterPanel.add(dateToLabel);
        filterPanel.add(dateToField);
        filterPanel.add(searchBtn);

        header.add(titleLabel, "grow");
        header.add(filterPanel);

        return header;
    }

    private JPanel createMainContent() {
        JPanel content = new JPanel(new MigLayout("fill, insets 0", "[grow]", "[][grow]"));
        content.setBackground(new Color(229, 231, 235));

        // Stats panel
        statsLabel = new JLabel("Carregando estatísticas...");
        statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statsLabel.setForeground(new Color(107, 114, 128));
        content.add(statsLabel, "growx, wrap");

        // Table panel
        JPanel tablePanel = new JPanel(new MigLayout("fill", "[grow]", "[grow]"));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create table
        String[] columns = {"ID", "Data/Hora", "Tipo", "Valor", "Taxa", "Conta Origem", "Conta Destino", "Descrição", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        transactionsTable = new JTable(tableModel);
        transactionsTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        transactionsTable.setRowHeight(25);
        transactionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        transactionsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));

        JScrollPane scrollPane = new JScrollPane(transactionsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        tablePanel.add(scrollPane, "grow");

        content.add(tablePanel, "grow");

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
        exportBtn.setText("Exportar Relatório");
        exportBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        exportBtn.setBackground(new Color(16, 185, 129));

        footer.add(backBtn);
        footer.add(exportBtn);

        return footer;
    }

    public void setTransactions(List<Transaction> transactions) {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (Transaction transaction : transactions) {
            String tipo = transaction.getType().toString();
            String status = transaction.getStatus().toString();
            String valor = String.format("MZN %,.2f", transaction.getAmount());
            String taxa = String.format("MZN %,.2f", transaction.getFeeAmount());

            tableModel.addRow(new Object[]{
                    transaction.getTransactionId(),
                    sdf.format(transaction.getTimestamp()),
                    tipo,
                    valor,
                    taxa,
                    "Conta #" + transaction.getSourceAccountId(),
                    transaction.getDestinationAccountId() > 0 ? "Conta #" + transaction.getDestinationAccountId() : "N/A",
                    transaction.getDescription(),
                    status
            });
        }
    }

    public void setStats(int totalTransactions, double totalVolume) {
        statsLabel.setText(String.format("Total: %d transações | Volume: MZN %,.2f",
                totalTransactions, totalVolume));
    }

    // Getters
    public MyButton getBackBtn() { return backBtn; }
    public MyButton getSearchBtn() { return searchBtn; }
    public MyButton getExportBtn() { return exportBtn; }
    public String getTypeFilter() { return (String) typeFilter.getSelectedItem(); }
    public String getStatusFilter() { return (String) statusFilter.getSelectedItem(); }
    public String getDateFrom() { return dateFromField.getText(); }
    public String getDateTo() { return dateToField.getText(); }
}