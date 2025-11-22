package controller;

import model.entities.Employee;
import model.enums.AccessLevel;
import model.services.EmployeeService;
import view.admin.EmployeeManagementView;
import net.miginfocom.swing.MigLayout;
import model.utils.PasswordUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EmployeeManagementController {
    private EmployeeManagementView view;
    private EmployeeService employeeService;
    private Employee currentEmployee;

    public EmployeeManagementController(EmployeeManagementView view, EmployeeService employeeService, Employee currentEmployee) {
        this.view = view;
        this.employeeService = employeeService;
        this.currentEmployee = currentEmployee;

        System.out.println("=== CONTROLLER CRIADO ===");
        setupListeners();
        loadEmployees();
    }

    private void setupListeners() {
        view.addAddEmployeeListener(e -> handleAddEmployee());
        view.addEditEmployeeListener(e -> handleEditEmployee());
        view.addChangeAccessListener(e -> handleChangeAccess());
        view.addDeactivateListener(e -> handleDeactivateEmployee());
        view.addBackListener(e -> handleBack());
    }

    private void loadEmployees() {
        try {
            List<Employee> employees = employeeService.getAllEmployees();
            view.setEmployees(employees);
            updateStatistics(employees);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Erro ao carregar funcionários: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStatistics(List<Employee> employees) {
        int total = employees.size();
        int admins = 0, managers = 0, staff = 0;

        for (Employee emp : employees) {
            switch (emp.getAccessLevel()) {
                case ADMIN: admins++; break;
                case MANAGER: managers++; break;
                case STAFF: staff++; break;
            }
        }

        view.setStats(total, admins, managers, staff);
    }

    private void handleBack() {
        Window window = SwingUtilities.getWindowAncestor(view);
        if (window != null) {
            window.dispose();
        }
    }

    private void handleAddEmployee() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Adicionar Novo Funcionário");
        dialog.setModal(true);
        dialog.setLayout(new MigLayout("wrap 2", "[right][grow]", "[]10[]"));
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(view);

        // Campos do formulário
        JTextField firstNameField = new JTextField(20);
        JTextField lastNameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JPasswordField confirmPasswordField = new JPasswordField(20);
        JTextArea addressArea = new JTextArea(3, 20);
        JScrollPane addressScroll = new JScrollPane(addressArea);

        JComboBox<AccessLevel> accessLevelCombo = new JComboBox<>(AccessLevel.values());
        JCheckBox supervisorCheckbox = new JCheckBox();

        // Adicionar componentes
        dialog.add(new JLabel("Primeiro Nome:*"));
        dialog.add(firstNameField, "growx");
        dialog.add(new JLabel("Último Nome:*"));
        dialog.add(lastNameField, "growx");
        dialog.add(new JLabel("Email:*"));
        dialog.add(emailField, "growx");
        dialog.add(new JLabel("Telefone:"));
        dialog.add(phoneField, "growx");
        dialog.add(new JLabel("Senha:*"));
        dialog.add(passwordField, "growx");
        dialog.add(new JLabel("Confirmar Senha:*"));
        dialog.add(confirmPasswordField, "growx");
        dialog.add(new JLabel("Endereço:"));
        dialog.add(addressScroll, "growx");
        dialog.add(new JLabel("Nível de Acesso:*"));
        dialog.add(accessLevelCombo, "growx");
        dialog.add(new JLabel("Supervisor:"));
        dialog.add(supervisorCheckbox);
        dialog.add(new JLabel("* Campos obrigatórios"), "span 2");

        // Botões
        JButton saveBtn = new JButton("Salvar");
        JButton cancelBtn = new JButton("Cancelar");

        saveBtn.addActionListener(saveEvent -> {
            try {
                // Validar campos
                if (firstNameField.getText().trim().isEmpty() ||
                        lastNameField.getText().trim().isEmpty() ||
                        emailField.getText().trim().isEmpty() ||
                        passwordField.getPassword().length == 0) {

                    JOptionPane.showMessageDialog(dialog, "Preencha todos os campos obrigatórios!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(dialog, "As senhas não coincidem!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (password.length() < 6) {
                    JOptionPane.showMessageDialog(dialog, "A senha deve ter pelo menos 6 caracteres!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Criar funcionário
                Employee newEmployee = employeeService.registerEmployee(
                        firstNameField.getText().trim(),
                        lastNameField.getText().trim(),
                        emailField.getText().trim(),
                        phoneField.getText().trim(),
                        password,
                        addressArea.getText().trim(),
                        (AccessLevel) accessLevelCombo.getSelectedItem(),
                        supervisorCheckbox.isSelected()
                );

                JOptionPane.showMessageDialog(dialog,
                        "Funcionário adicionado com sucesso!\n\n" +
                                "Nome: " + newEmployee.getFullName() + "\n" +
                                "Email: " + newEmployee.getEmail() + "\n" +
                                "Nível de Acesso: " + newEmployee.getAccessLevel() +
                                (newEmployee.isSupervisor() ? " (Supervisor)" : ""),
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);

                dialog.dispose();
                loadEmployees();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Erro ao adicionar funcionário: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        dialog.add(buttonPanel, "span 2, center");

        dialog.setVisible(true);
    }

    private void handleEditEmployee() {
        int selectedId = view.getSelectedEmployeeId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(view, "Selecione um funcionário na tabela para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Employee employee = employeeService.getEmployeeById(selectedId);
            if (employee == null) {
                JOptionPane.showMessageDialog(view, "Funcionário não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JDialog dialog = new JDialog();
            dialog.setTitle("Editar Funcionário: " + employee.getFullName());
            dialog.setModal(true);
            dialog.setLayout(new MigLayout("wrap 2", "[right][grow]", "[]10[]"));
            dialog.setSize(500, 450);
            dialog.setLocationRelativeTo(view);

            // Campos do formulário pré-preenchidos
            JTextField firstNameField = new JTextField(employee.getFirstName(), 20);
            JTextField lastNameField = new JTextField(employee.getLastName(), 20);
            JTextField emailField = new JTextField(employee.getEmail(), 20);
            JTextField phoneField = new JTextField(employee.getPhone(), 20);
            JTextArea addressArea = new JTextArea(employee.getAddress(), 3, 20);
            JScrollPane addressScroll = new JScrollPane(addressArea);

            JComboBox<AccessLevel> accessLevelCombo = new JComboBox<>(AccessLevel.values());
            accessLevelCombo.setSelectedItem(employee.getAccessLevel());

            JCheckBox supervisorCheckbox = new JCheckBox();
            supervisorCheckbox.setSelected(employee.isSupervisor());

            // Adicionar componentes
            dialog.add(new JLabel("Primeiro Nome:*"));
            dialog.add(firstNameField, "growx");
            dialog.add(new JLabel("Último Nome:*"));
            dialog.add(lastNameField, "growx");
            dialog.add(new JLabel("Email:*"));
            dialog.add(emailField, "growx");
            dialog.add(new JLabel("Telefone:"));
            dialog.add(phoneField, "growx");
            dialog.add(new JLabel("Endereço:"));
            dialog.add(addressScroll, "growx");
            dialog.add(new JLabel("Nível de Acesso:*"));
            dialog.add(accessLevelCombo, "growx");
            dialog.add(new JLabel("Supervisor:"));
            dialog.add(supervisorCheckbox);
            dialog.add(new JLabel("* Campos obrigatórios"), "span 2");

            // Botões
            JButton saveBtn = new JButton("Salvar Alterações");
            JButton cancelBtn = new JButton("Cancelar");

            saveBtn.addActionListener(saveEvent -> {
                try {
                    // Validar campos
                    if (firstNameField.getText().trim().isEmpty() ||
                            lastNameField.getText().trim().isEmpty() ||
                            emailField.getText().trim().isEmpty()) {

                        JOptionPane.showMessageDialog(dialog, "Preencha todos os campos obrigatórios!", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Atualizar dados do funcionário
                    employee.setFirstName(firstNameField.getText().trim());
                    employee.setLastName(lastNameField.getText().trim());
                    employee.setEmail(emailField.getText().trim());
                    employee.setPhone(phoneField.getText().trim());
                    employee.setAddress(addressArea.getText().trim());
                    employee.setAccessLevel((AccessLevel) accessLevelCombo.getSelectedItem());
                    employee.setSupervisor(supervisorCheckbox.isSelected());

                    // TODO: Implementar método update no EmployeeService
                    JOptionPane.showMessageDialog(dialog,
                            "Funcionalidade de edição completa em desenvolvimento.\n\n" +
                                    "Dados que seriam salvos:\n" +
                                    "Nome: " + employee.getFullName() + "\n" +
                                    "Email: " + employee.getEmail() + "\n" +
                                    "Nível: " + employee.getAccessLevel(),
                            "Edição - Em Desenvolvimento",
                            JOptionPane.INFORMATION_MESSAGE);

                    dialog.dispose();
                    loadEmployees();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Erro ao editar funcionário: " + ex.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });

            cancelBtn.addActionListener(e -> dialog.dispose());

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(saveBtn);
            buttonPanel.add(cancelBtn);
            dialog.add(buttonPanel, "span 2, center");

            dialog.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Erro ao carregar dados do funcionário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void handleChangeAccess() {
        int selectedId = view.getSelectedEmployeeId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(view, "Selecione um funcionário na tabela para alterar o acesso!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Employee employee = employeeService.getEmployeeById(selectedId);
            if (employee == null) {
                JOptionPane.showMessageDialog(view, "Funcionário não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Diálogo para selecionar novo nível de acesso
            String[] options = {"STAFF", "MANAGER", "ADMIN"};
            String selected = (String) JOptionPane.showInputDialog(view,
                    "Alterar nível de acesso para: " + employee.getFullName() + "\n\n" +
                            "Nível atual: " + employee.getAccessLevel() +
                            (employee.isSupervisor() ? " (Supervisor)" : ""),
                    "Alterar Nível de Acesso",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    employee.getAccessLevel().toString());

            if (selected != null) {
                AccessLevel newLevel = AccessLevel.valueOf(selected);

                // Confirmar a alteração
                int confirm = JOptionPane.showConfirmDialog(view,
                        "Confirmar alteração de acesso?\n\n" +
                                "Funcionário: " + employee.getFullName() + "\n" +
                                "De: " + employee.getAccessLevel() + "\n" +
                                "Para: " + newLevel,
                        "Confirmar Alteração",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = employeeService.updateEmployeeAccessLevel(selectedId, newLevel);

                    if (success) {
                        JOptionPane.showMessageDialog(view,
                                "Nível de acesso alterado com sucesso!\n\n" +
                                        "Funcionário: " + employee.getFullName() + "\n" +
                                        "Novo nível: " + newLevel,
                                "Acesso Alterado",
                                JOptionPane.INFORMATION_MESSAGE);

                        loadEmployees(); // Recarregar lista
                    } else {
                        JOptionPane.showMessageDialog(view,
                                "Erro ao alterar nível de acesso.",
                                "Erro",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Erro: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void handleDeactivateEmployee() {
        int selectedId = view.getSelectedEmployeeId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(view, "Selecione um funcionário na tabela para eliminar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Employee employee = employeeService.getEmployeeById(selectedId);
            if (employee == null) {
                JOptionPane.showMessageDialog(view, "Funcionário não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar se não está tentando eliminar a si mesmo
            if (currentEmployee != null && employee.getUserId() == currentEmployee.getUserId()) {
                JOptionPane.showMessageDialog(view,
                        "Você não pode eliminar a sua própria conta!",
                        "Ação Não Permitida",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Diálogo de confirmação mais forte para ELIMINAÇÃO
            int confirm = JOptionPane.showConfirmDialog(view,
                    "<html><b style='color: red;'>⚠️ CONFIRMAR ELIMINAÇÃO PERMANENTE</b></html>\n\n" +
                            "<html><b>Funcionário a ser ELIMINADO:</b></html>\n" +
                            "• Nome: " + employee.getFullName() + "\n" +
                            "• Email: " + employee.getEmail() + "\n" +
                            "• Nível: " + employee.getAccessLevel() +
                            (employee.isSupervisor() ? " (Supervisor)" : "") + "\n\n" +
                            "<html><b style='color: red;'>🚨 ESTA AÇÃO É IRREVERSÍVEL!</b></html>\n" +
                            "• Todos os dados serão PERDIDOS permanentemente\n" +
                            "• Não será possível recuperar a conta\n" +
                            "• O funcionário não poderá mais aceder ao sistema\n\n" +
                            "Tem certeza absoluta que deseja continuar?",
                    "Confirmar Eliminação Permanente",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                // Executar ELIMINAÇÃO REAL
                boolean success = employeeService.deleteEmployee(selectedId);

                if (success) {
                    JOptionPane.showMessageDialog(view,
                            "<html><b style='color: red;'>✅ Funcionário Eliminado Permanentemente!</b></html>\n\n" +
                                    "Nome: " + employee.getFullName() + "\n" +
                                    "Email: " + employee.getEmail() + "\n" +
                                    "Status: <b>ELIMINADO</b>\n\n" +
                                    "Todos os dados foram removidos da base de dados.",
                            "Eliminação Concluída",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Recarregar a lista para refletir a mudança
                    loadEmployees();

                } else {
                    JOptionPane.showMessageDialog(view,
                            "<html><b>Erro ao eliminar funcionário!</b></html>\n\n" +
                                    "Possíveis causas:\n" +
                                    "• O funcionário não existe\n" +
                                    "• Erro de conexão com a base de dados\n" +
                                    "• Restrições de integridade referencial\n\n" +
                                    "Por favor, tente novamente ou contate o administrador.",
                            "Erro na Eliminação",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Erro ao processar eliminação: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}