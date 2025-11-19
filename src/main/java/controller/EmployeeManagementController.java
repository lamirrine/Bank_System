package controller;

import model.entities.Employee;
import model.enums.AccessLevel;
import model.services.EmployeeService;
import view.admin.EmployeeManagementView;

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

        System.out.println("=== CONTROLLER CRIADO para: " + (currentEmployee != null ? currentEmployee.getFullName() : "null") + " ===");

        setupListeners();
        loadEmployees();
    }

    // ✅ CONSTRUTOR ALTERNATivo (para compatibilidade)
    public EmployeeManagementController(EmployeeManagementView view, EmployeeService employeeService) {
        this(view, employeeService, null);
    }

    private void setupListeners() {
        System.out.println("=== CONFIGURANDO LISTENERS ===");

        view.addAddEmployeeListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("=== LISTENER EXTERNO ADD CHAMADO ===");
                handleAddEmployee();
            }
        });

        view.addEditEmployeeListener(e -> {
            System.out.println("=== LISTENER EXTERNO EDIT CHAMADO ===");
            handleEditEmployee();
        });

        view.addChangeAccessListener(e -> {
            System.out.println("=== LISTENER EXTERNO ACCESS CHAMADO ===");
            handleChangeAccess();
        });

        view.addDeactivateListener(e -> {
            System.out.println("=== LISTENER EXTERNO DEACTIVATE CHAMADO ===");
            handleDeactivateEmployee();
        });

        view.addBackListener(e -> {
            System.out.println("=== LISTENER EXTERNO BACK CHAMADO ===");
            handleBack();
        });

        System.out.println("=== TODOS OS LISTENERS EXTERNOS CONFIGURADOS ===");
    }

    private void loadEmployees() {
        try {
            System.out.println("Carregando funcionários...");
            List<Employee> employees = employeeService.getAllEmployees();
            view.setEmployees(employees);
            updateStatistics(employees);
            System.out.println("Funcionários carregados: " + employees.size());
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
        System.out.println("Fechando janela de gestão...");
        Window window = SwingUtilities.getWindowAncestor(view);
        if (window != null) {
            window.dispose();
        }
    }

    private void handleAddEmployee() {
        System.out.println("Abrindo diálogo de adicionar funcionário...");

        // Diálogo SIMPLES para teste
        String nome = JOptionPane.showInputDialog(view, "Digite o nome do novo funcionário:");
        if (nome != null && !nome.isEmpty()) {
            try {
                // Criar funcionário de teste
                Employee novo = employeeService.registerEmployee(
                        nome, "Sobrenome",
                        nome.toLowerCase() + "@empresa.com",
                        "123456789", "senha123",
                        "Endereço", AccessLevel.STAFF, false
                );

                JOptionPane.showMessageDialog(view,
                        "Funcionário criado!\nID: " + novo.getUserId() + "\nNome: " + novo.getFullName(),
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                loadEmployees(); // Recarregar lista

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view,
                        "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void handleEditEmployee() {
        int id = view.getSelectedEmployeeId();
        if (id == -1) {
            JOptionPane.showMessageDialog(view, "Selecione um funcionário!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(view, "Editando funcionário ID: " + id, "Editar", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleChangeAccess() {
        int id = view.getSelectedEmployeeId();
        if (id == -1) {
            JOptionPane.showMessageDialog(view, "Selecione um funcionário!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(view, "Alterando acesso do funcionário ID: " + id, "Acesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleDeactivateEmployee() {
        int id = view.getSelectedEmployeeId();
        if (id == -1) {
            JOptionPane.showMessageDialog(view, "Selecione um funcionário!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(view, "Desativando funcionário ID: " + id, "Desativar", JOptionPane.INFORMATION_MESSAGE);
    }
}