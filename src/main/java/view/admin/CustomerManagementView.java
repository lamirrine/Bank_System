package view.admin;

import model.entities.Customer;
import net.miginfocom.swing.MigLayout;
import view.componet.MyButton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class CustomerManagementView extends JPanel {
    private JTable customersTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private MyButton searchBtn;
    private MyButton viewDetailsBtn;
    private MyButton editCustomerBtn;
    private MyButton deactivateBtn;
    private MyButton backBtn;
    private JLabel statsLabel;

    public CustomerManagementView() {
        initializeUI();
        setupTableSelectionListener();
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

    private void setupTableSelectionListener() {
        customersTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelection = customersTable.getSelectedRow() >= 0;
                updateButtonsState(hasSelection);
            }
        });
    }

    private void updateButtonsState(boolean enabled) {
        viewDetailsBtn.setEnabled(enabled);
        editCustomerBtn.setEnabled(enabled);
        deactivateBtn.setEnabled(enabled);

        // Mudar cor para indicar estado
        Color enabledColor = new Color(59, 130, 246);
        Color enabledColorEdit = new Color(16, 185, 129);
        Color enabledColorDeactivate = new Color(239, 68, 68);
        Color disabledColor = Color.GRAY;

        viewDetailsBtn.setBackground(enabled ? enabledColor : disabledColor);
        editCustomerBtn.setBackground(enabled ? enabledColorEdit : disabledColor);
        deactivateBtn.setBackground(enabled ? enabledColorDeactivate : disabledColor);
    }

    private void showSelectionError() {
        JOptionPane.showMessageDialog(this,
                "❌ NENHUM CLIENTE SELECIONADO\n\n" +
                        "Por favor, selecione um cliente na tabela\n" +
                        "Clique em uma linha para selecionar",
                "Seleção Necessária", JOptionPane.WARNING_MESSAGE);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new MigLayout("fill, insets 0 0 15 0", "[grow][]", "[]"));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));

        JLabel titleLabel = new JLabel("Gestão de Clientes");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(15, 23, 42));

        // Search panel
        JPanel searchPanel = new JPanel(new MigLayout("insets 0", "[][grow][]", "[]"));
        searchPanel.setBackground(Color.WHITE);

        JLabel searchLabel = new JLabel("Buscar:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        searchBtn = new MyButton();
        searchBtn.setText("🔍 Buscar");
        searchBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchBtn.setBackground(new Color(59, 130, 246));
        searchBtn.setFocusPainted(false);

        searchPanel.add(searchLabel);
        searchPanel.add(searchField, "w 200!");
        searchPanel.add(searchBtn);

        header.add(titleLabel, "grow");
        header.add(searchPanel);

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
        String[] columns = {"ID", "Nome", "Email", "Telefone", "Data Registo", "Documento", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        customersTable = new JTable(tableModel);
        customersTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        customersTable.setRowHeight(30);
        customersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customersTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(customersTable);
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
        backBtn.setFocusPainted(false);

        viewDetailsBtn = new MyButton();
        viewDetailsBtn.setText("Ver Detalhes");
        viewDetailsBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        viewDetailsBtn.setBackground(new Color(59, 130, 246));
        viewDetailsBtn.setFocusPainted(false);

        editCustomerBtn = new MyButton();
        editCustomerBtn.setText("Editar");
        editCustomerBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        editCustomerBtn.setBackground(new Color(16, 185, 129));
        editCustomerBtn.setFocusPainted(false);

        deactivateBtn = new MyButton();
        deactivateBtn.setText("Desativar");
        deactivateBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        deactivateBtn.setBackground(new Color(239, 68, 68));
        deactivateBtn.setFocusPainted(false);

        footer.add(backBtn);
        footer.add(viewDetailsBtn);
        footer.add(editCustomerBtn);
        footer.add(deactivateBtn);

        return footer;
    }

    public void setCustomers(List<Customer> customers) {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        if (customers != null) {
            for (Customer customer : customers) {
                String documento = customer.getBiNumber() != null ? customer.getBiNumber() :
                        customer.getPassportNumber() != null ? customer.getPassportNumber() : "N/A";

                tableModel.addRow(new Object[]{
                        customer.getUserId(),
                        customer.getFullName(),
                        customer.getEmail(),
                        customer.getPhone(),
                        sdf.format(customer.getRegistrationDate()),
                        documento,
                        "Ativo"
                });
            }
        }
    }

    public void setStats(int totalCustomers, int activeCustomers) {
        statsLabel.setText(String.format("📊 Total: %d clientes | Ativos: %d", totalCustomers, activeCustomers));
    }

    public int getSelectedCustomerId() {
        int selectedRow = customersTable.getSelectedRow();
        if (selectedRow >= 0) {
            return (Integer) tableModel.getValueAt(selectedRow, 0);
        }
        return -1;
    }

    public String getSelectedCustomerName() {
        int selectedRow = customersTable.getSelectedRow();
        if (selectedRow >= 0) {
            return (String) tableModel.getValueAt(selectedRow, 1);
        }
        return "N/A";
    }

    public String getSelectedCustomerEmail() {
        int selectedRow = customersTable.getSelectedRow();
        if (selectedRow >= 0) {
            return (String) tableModel.getValueAt(selectedRow, 2);
        }
        return "N/A";
    }

    public String getSelectedCustomerPhone() {
        int selectedRow = customersTable.getSelectedRow();
        return (selectedRow >= 0) ? (String) tableModel.getValueAt(selectedRow, 3) : "N/A";
    }

    public String getSelectedCustomerDocument() {
        int selectedRow = customersTable.getSelectedRow();
        return (selectedRow >= 0) ? (String) tableModel.getValueAt(selectedRow, 5) : "N/A";
    }

    public String getSelectedCustomerDate() {
        int selectedRow = customersTable.getSelectedRow();
        return (selectedRow >= 0) ? (String) tableModel.getValueAt(selectedRow, 4) : "N/A";
    }

    public String getSearchText() {
        return searchField.getText().trim();
    }

    // Getters
    public MyButton getBackBtn() { return backBtn; }
    public MyButton getSearchBtn() { return searchBtn; }
    public MyButton getViewDetailsBtn() { return viewDetailsBtn; }
    public MyButton getEditCustomerBtn() { return editCustomerBtn; }
    public MyButton getDeactivateBtn() { return deactivateBtn; }
    public JTable getCustomersTable() { return customersTable; }
}