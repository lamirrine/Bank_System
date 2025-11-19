// AccountManagementView.java
package view.admin;

import model.entities.Account;
import net.miginfocom.swing.MigLayout;
import view.componet.MyButton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class AccountManagementView extends JPanel {
    private JTable accountsTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> typeFilter;
    private JComboBox<String> statusFilter;
    private MyButton searchBtn;
    private MyButton viewDetailsBtn;
    private MyButton openAccountBtn;
    private MyButton closeAccountBtn;
    private MyButton adjustLimitsBtn;
    private MyButton backBtn;
    private JLabel statsLabel;

    public AccountManagementView() {
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

        JLabel titleLabel = new JLabel("Gestão de Contas");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(15, 23, 42));

        // Filters panel
        JPanel filterPanel = new JPanel(new MigLayout("insets 0", "[][][][]", "[]"));
        filterPanel.setBackground(Color.WHITE);

        JLabel typeLabel = new JLabel("Tipo:");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        typeFilter = new JComboBox<>(new String[]{"Todos", "Corrente", "Poupança"});
        typeFilter.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        statusFilter = new JComboBox<>(new String[]{"Todos", "Ativa", "Suspensa", "Fechada"});
        statusFilter.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        searchBtn = new MyButton();
        searchBtn.setText("Filtrar");
        searchBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchBtn.setBackground(new Color(59, 130, 246));

        filterPanel.add(typeLabel);
        filterPanel.add(typeFilter);
        filterPanel.add(statusLabel);
        filterPanel.add(statusFilter);
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
        String[] columns = {"Número", "Tipo", "Cliente", "Saldo", "Data Abertura", "Limite Lev.", "Limite Transf.", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        accountsTable = new JTable(tableModel);
        accountsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        accountsTable.setRowHeight(30);
        accountsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        accountsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(accountsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        tablePanel.add(scrollPane, "grow");

        content.add(tablePanel, "grow");

        return content;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new MigLayout("insets 20 0 0 0", "[grow][][][][]", "[]"));
        footer.setBackground(new Color(229, 231, 235));

        backBtn = new MyButton();
        backBtn.setText("Voltar");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setBackground(new Color(107, 114, 128));

        openAccountBtn = new MyButton();
        openAccountBtn.setText("Nova Conta");
        openAccountBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        openAccountBtn.setBackground(new Color(16, 185, 129));

        viewDetailsBtn = new MyButton();
        viewDetailsBtn.setText("Detalhes");
        viewDetailsBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        viewDetailsBtn.setBackground(new Color(59, 130, 246));

        adjustLimitsBtn = new MyButton();
        adjustLimitsBtn.setText("Ajustar Limites");
        adjustLimitsBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        adjustLimitsBtn.setBackground(new Color(245, 158, 11));

        closeAccountBtn = new MyButton();
        closeAccountBtn.setText("Encerrar");
        closeAccountBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeAccountBtn.setBackground(new Color(239, 68, 68));

        footer.add(backBtn);
        footer.add(openAccountBtn);
        footer.add(viewDetailsBtn);
        footer.add(adjustLimitsBtn);
        footer.add(closeAccountBtn);

        return footer;
    }

    public void setAccounts(List<Account> accounts) {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (Account account : accounts) {
            String tipo = account.getAccountType().toString().equals("CORRENTE") ? "Corrente" : "Poupança";
            String status = account.getStatus().toString().equals("ATIVA") ? "Ativa" :
                    account.getStatus().toString().equals("SUSPENSA") ? "Suspensa" : "Fechada";

            tableModel.addRow(new Object[]{
                    account.getAccountNumber(),
                    tipo,
                    "Cliente #" + account.getOwnerCustomerId(),
                    String.format("MZN %,.2f", account.getBalance()),
                    sdf.format(account.getOpenDate()),
                    String.format("MZN %,.2f", account.getDailyWithdrawLimit()),
                    String.format("MZN %,.2f", account.getDailyTransferLimit()),
                    status
            });
        }
    }

    public void setStats(int totalAccounts, int activeAccounts, double totalBalance) {
        statsLabel.setText(String.format("Total: %d contas | Ativas: %d | Saldo Total: MZN %,.2f",
                totalAccounts, activeAccounts, totalBalance));
    }

    public String getSelectedAccountNumber() {
        int selectedRow = accountsTable.getSelectedRow();
        if (selectedRow >= 0) {
            return (String) tableModel.getValueAt(selectedRow, 0);
        }
        return null;
    }

    public String getTypeFilter() {
        return (String) typeFilter.getSelectedItem();
    }

    public String getStatusFilter() {
        return (String) statusFilter.getSelectedItem();
    }

    // Getters
    public MyButton getBackBtn() { return backBtn; }
    public MyButton getSearchBtn() { return searchBtn; }
    public MyButton getOpenAccountBtn() { return openAccountBtn; }
    public MyButton getViewDetailsBtn() { return viewDetailsBtn; }
    public MyButton getAdjustLimitsBtn() { return adjustLimitsBtn; }
    public MyButton getCloseAccountBtn() { return closeAccountBtn; }
}