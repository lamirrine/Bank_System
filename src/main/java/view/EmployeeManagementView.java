// EmployeeManagementView.java
package view;

import model.entities.Employee;
import net.miginfocom.swing.MigLayout;
import view.componet.MyButton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class EmployeeManagementView extends JPanel {
    private JTable employeesTable;
    private DefaultTableModel tableModel;
    private MyButton addEmployeeBtn;
    private MyButton editEmployeeBtn;
    private MyButton changeAccessBtn;
    private MyButton deactivateBtn;
    private MyButton backBtn;
    private JLabel statsLabel;

    public EmployeeManagementView() {
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

        JLabel titleLabel = new JLabel("Gestão de Funcionários");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(15, 23, 42));

        addEmployeeBtn = new MyButton();
        addEmployeeBtn.setText("+ Novo Funcionário");
        addEmployeeBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addEmployeeBtn.setBackground(new Color(16, 185, 129));

        header.add(titleLabel, "grow");
        header.add(addEmployeeBtn);

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
        String[] columns = {"ID", "Nome", "Email", "Telefone", "Nível Acesso", "Supervisor", "Data Registo", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        employeesTable = new JTable(tableModel);
        employeesTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        employeesTable.setRowHeight(30);
        employeesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(employeesTable);
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

        editEmployeeBtn = new MyButton();
        editEmployeeBtn.setText("Editar");
        editEmployeeBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        editEmployeeBtn.setBackground(new Color(59, 130, 246));

        changeAccessBtn = new MyButton();
        changeAccessBtn.setText("Alterar Acesso");
        changeAccessBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        changeAccessBtn.setBackground(new Color(245, 158, 11));

        deactivateBtn = new MyButton();
        deactivateBtn.setText("Desativar");
        deactivateBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        deactivateBtn.setBackground(new Color(239, 68, 68));

        footer.add(backBtn);
        footer.add(editEmployeeBtn);
        footer.add(changeAccessBtn);
        footer.add(deactivateBtn);

        return footer;
    }

    public void setEmployees(List<Employee> employees) {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (Employee employee : employees) {
            String nivelAcesso = employee.getAccessLevel().toString();
            String supervisor = employee.isSupervisor() ? "Sim" : "Não";

            tableModel.addRow(new Object[]{
                    employee.getUserId(),
                    employee.getFullName(),
                    employee.getEmail(),
                    employee.getPhone(),
                    nivelAcesso,
                    supervisor,
                    sdf.format(employee.getRegistrationDate()),
                    "Ativo"
            });
        }
    }

    public void setStats(int totalEmployees, int admins, int managers, int staff) {
        statsLabel.setText(String.format("Total: %d funcionários | Admin: %d | Gestores: %d | Staff: %d",
                totalEmployees, admins, managers, staff));
    }

    public int getSelectedEmployeeId() {
        int selectedRow = employeesTable.getSelectedRow();
        if (selectedRow >= 0) {
            return (Integer) tableModel.getValueAt(selectedRow, 0);
        }
        return -1;
    }

    // Getters
    public MyButton getBackBtn() { return backBtn; }
    public MyButton getAddEmployeeBtn() { return addEmployeeBtn; }
    public MyButton getEditEmployeeBtn() { return editEmployeeBtn; }
    public MyButton getChangeAccessBtn() { return changeAccessBtn; }
    public MyButton getDeactivateBtn() { return deactivateBtn; }
}