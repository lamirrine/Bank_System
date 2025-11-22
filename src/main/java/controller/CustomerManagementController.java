package controller;

import model.entities.Customer;
import model.services.CustomerService;
import view.admin.CustomerManagementView;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class CustomerManagementController {
    private CustomerManagementView view;
    private CustomerService customerService;

    public CustomerManagementController(CustomerManagementView view, CustomerService customerService) {
        this.view = view;
        this.customerService = customerService;

        setupListeners();
        loadCustomers();
    }

    private void setupListeners() {
        // Botão Buscar
        view.getSearchBtn().addActionListener(e -> handleSearch());

        // Botão Ver Detalhes
        view.getViewDetailsBtn().addActionListener(e -> handleViewDetails());

        // Botão Editar
        view.getEditCustomerBtn().addActionListener(e -> handleEditCustomer());

        // Botão Desativar
        view.getDeactivateBtn().addActionListener(e -> handleDeactivateCustomer());

        // Botão Voltar
        view.getBackBtn().addActionListener(e -> handleBack());
    }

    private void loadCustomers() {
        try {
            List<Customer> customers = customerService.findAllCustomers();
            view.setCustomers(customers);
            updateStatistics(customers);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Erro ao carregar clientes: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStatistics(List<Customer> customers) {
        int total = customers.size();
        int active = customers.size(); // Por enquanto, todos estão ativos
        view.setStats(total, active);
    }

    private void handleBack() {
        Window window = SwingUtilities.getWindowAncestor(view);
        if (window != null) {
            window.dispose();
        }
    }

    private void handleSearch() {
        String searchText = view.getSearchText();
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Digite um nome, email ou documento para buscar", "Campo Vazio", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<Customer> customers = customerService.searchCustomers(searchText);
            view.setCustomers(customers);
            view.setStats(customers.size(), customers.size());

            if (customers.isEmpty()) {
                JOptionPane.showMessageDialog(view,
                        "Nenhum cliente encontrado para: '" + searchText + "'",
                        "Busca Sem Resultados",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(view,
                        "Encontrados " + customers.size() + " cliente(s) para: '" + searchText + "'",
                        "Busca Concluída",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Erro na busca: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void handleViewDetails() {
        int customerId = view.getSelectedCustomerId();
        if (customerId == -1) {
            JOptionPane.showMessageDialog(view,
                    "Por favor, selecione um cliente na tabela",
                    "Seleção Necessária",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Customer customer = customerService.findCustomerById(customerId);
            if (customer == null) {
                JOptionPane.showMessageDialog(view, "Cliente não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            showCustomerDetailsDialog(customer);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Erro ao carregar detalhes: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void showCustomerDetailsDialog(Customer customer) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Detalhes do Cliente: " + customer.getFullName());
        dialog.setModal(true);
        dialog.setLayout(new MigLayout("wrap 2", "[right][grow]", "[]10[]"));
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(view);

        // Informações básicas
        dialog.add(new JLabel("<html><b>Informações Pessoais:</b></html>"), "span 2, wrap");

        dialog.add(new JLabel("ID:"));
        dialog.add(new JLabel(String.valueOf(customer.getUserId())));

        dialog.add(new JLabel("Nome Completo:"));
        dialog.add(new JLabel(customer.getFullName()));

        dialog.add(new JLabel("Email:"));
        dialog.add(new JLabel(customer.getEmail()));

        dialog.add(new JLabel("Telefone:"));
        dialog.add(new JLabel(customer.getPhone() != null ? customer.getPhone() : "N/A"));

        dialog.add(new JLabel("Endereço:"));
        dialog.add(new JLabel(customer.getAddress() != null ? customer.getAddress() : "N/A"));

        // Documentos
        dialog.add(new JLabel("<html><b>Documentos:</b></html>"), "span 2, wrap, gaptop 10");

        dialog.add(new JLabel("BI:"));
        dialog.add(new JLabel(customer.getBiNumber() != null ? customer.getBiNumber() : "N/A"));

        dialog.add(new JLabel("Passaporte:"));
        dialog.add(new JLabel(customer.getPassportNumber() != null ? customer.getPassportNumber() : "N/A"));

        dialog.add(new JLabel("NUIT:"));
        dialog.add(new JLabel(customer.getNuit() != null ? customer.getNuit() : "N/A"));

        // Data de registo
        dialog.add(new JLabel("<html><b>Informações de Sistema:</b></html>"), "span 2, wrap, gaptop 10");

        dialog.add(new JLabel("Data de Registo:"));
        dialog.add(new JLabel(customer.getRegistrationDate().toString()));

        // Botão fechar
        JButton closeBtn = new JButton("Fechar");
        closeBtn.addActionListener(e -> dialog.dispose());

        dialog.add(closeBtn, "span 2, center, gaptop 15");

        dialog.setVisible(true);
    }

    private void handleEditCustomer() {
        int customerId = view.getSelectedCustomerId();
        if (customerId == -1) {
            JOptionPane.showMessageDialog(view,
                    "Por favor, selecione um cliente na tabela",
                    "Seleção Necessária",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Customer customer = customerService.findCustomerById(customerId);
            if (customer == null) {
                JOptionPane.showMessageDialog(view, "Cliente não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            showEditCustomerDialog(customer);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Erro ao carregar dados do cliente: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void showEditCustomerDialog(Customer customer) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Editar Cliente: " + customer.getFullName());
        dialog.setModal(true);
        dialog.setLayout(new MigLayout("wrap 2", "[right][grow]", "[]10[]"));
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(view);

        // Campos editáveis
        JTextField firstNameField = new JTextField(customer.getFirstName(), 20);
        JTextField lastNameField = new JTextField(customer.getLastName(), 20);
        JTextField emailField = new JTextField(customer.getEmail(), 20);
        JTextField phoneField = new JTextField(customer.getPhone() != null ? customer.getPhone() : "", 20);
        JTextArea addressArea = new JTextArea(customer.getAddress() != null ? customer.getAddress() : "", 3, 20);
        JScrollPane addressScroll = new JScrollPane(addressArea);

        // Documentos (somente leitura)
        JTextField biField = new JTextField(customer.getBiNumber() != null ? customer.getBiNumber() : "");
        biField.setEditable(false);
        JTextField passportField = new JTextField(customer.getPassportNumber() != null ? customer.getPassportNumber() : "");
        passportField.setEditable(false);
        JTextField nuitField = new JTextField(customer.getNuit() != null ? customer.getNuit() : "");
        nuitField.setEditable(false);

        // Adicionar componentes
        dialog.add(new JLabel("<html><b>Informações Pessoais:</b></html>"), "span 2, wrap");

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

        dialog.add(new JLabel("<html><b>Documentos (não editáveis):</b></html>"), "span 2, wrap, gaptop 10");

        dialog.add(new JLabel("BI:"));
        dialog.add(biField, "growx");

        dialog.add(new JLabel("Passaporte:"));
        dialog.add(passportField, "growx");

        dialog.add(new JLabel("NUIT:"));
        dialog.add(nuitField, "growx");

        dialog.add(new JLabel("* Campos obrigatórios"), "span 2");

        // Botões
        JButton saveBtn = new JButton("Salvar Alterações");
        JButton cancelBtn = new JButton("Cancelar");

        saveBtn.addActionListener(e -> {
            try {
                // Validar campos
                if (firstNameField.getText().trim().isEmpty() ||
                        lastNameField.getText().trim().isEmpty() ||
                        emailField.getText().trim().isEmpty()) {

                    JOptionPane.showMessageDialog(dialog, "Preencha todos os campos obrigatórios!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Atualizar dados do cliente
                customer.setFirstName(firstNameField.getText().trim());
                customer.setLastName(lastNameField.getText().trim());
                customer.setEmail(emailField.getText().trim());
                customer.setPhone(phoneField.getText().trim());
                customer.setAddress(addressArea.getText().trim());

                // Salvar no banco de dados
                boolean success = customerService.updateCustomer(customer);

                if (success) {
                    JOptionPane.showMessageDialog(dialog,
                            "Cliente atualizado com sucesso!",
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);

                    dialog.dispose();
                    loadCustomers(); // Recarregar lista
                } else {
                    JOptionPane.showMessageDialog(dialog,
                            "Erro ao atualizar cliente",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Erro ao salvar alterações: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        dialog.add(buttonPanel, "span 2, center, gaptop 15");

        dialog.setVisible(true);
    }

    private void handleDeactivateCustomer() {
        int customerId = view.getSelectedCustomerId();
        if (customerId == -1) {
            JOptionPane.showMessageDialog(view,
                    "Por favor, selecione um cliente na tabela",
                    "Seleção Necessária",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Customer customer = customerService.findCustomerById(customerId);
            if (customer == null) {
                JOptionPane.showMessageDialog(view, "Cliente não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obter informações sobre as contas do cliente
            Map<String, Object> accountsInfo = customerService.getCustomerAccountsInfo(customerId);
            int totalAccounts = (Integer) accountsInfo.getOrDefault("totalAccounts", 0);
            double totalBalance = (Double) accountsInfo.getOrDefault("totalBalance", 0.0);
            boolean hasAccounts = totalAccounts > 0;

            // Construir mensagem baseada nas contas do cliente
            String accountsMessage;
            if (hasAccounts) {
                accountsMessage = "<html><b style='color: red;'>⚠️ ATENÇÃO: Este cliente possui " + totalAccounts + " conta(s)</b></html>\n" +
                        "• Saldo total: " + String.format("%,.2f", totalBalance) + " MT\n" +
                        "• Todas as contas serão ELIMINADAS\n" +
                        "• Todas as transações serão REMOVIDAS\n" +
                        "• Esta ação não pode ser desfeita!\n\n";
            } else {
                accountsMessage = "• Cliente não possui contas\n\n";
            }

            // Diálogo de confirmação para ELIMINAÇÃO COMPLETA
            int confirm = JOptionPane.showConfirmDialog(view,
                    "<html><b style='color: red; font-size: 14px;'>🚨 ELIMINAÇÃO COMPLETA DO CLIENTE</b></html>\n\n" +
                            "<html><b>Cliente a ser ELIMINADO:</b></html>\n" +
                            "• Nome: " + customer.getFullName() + "\n" +
                            "• Email: " + customer.getEmail() + "\n" +
                            "• ID: " + customer.getUserId() + "\n\n" +
                            accountsMessage +
                            "<html><b style='color: red;'>🗑️ TODOS OS SEGUINTES DADOS SERÃO PERDIDOS:</b></html>\n" +
                            "• Dados pessoais do cliente\n" +
                            "• " + totalAccounts + " conta(s) bancária(s)\n" +
                            "• Histórico completo de transações\n" +
                            "• Informações de documentos (BI, Passaporte)\n\n" +
                            "<html><b style='color: red;'>❌ ESTA AÇÃO É IRREVERSÍVEL E PERMANENTE!</b></html>\n\n" +
                            "<html><b>Confirmar eliminação completa?</b></html>",
                    "Confirmar Eliminação Completa",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {

                boolean success = customerService.deleteCustomer(customerId);

                if (success) {
                    String successMessage;
                    if (hasAccounts) {
                        successMessage = "<html><b style='color: red;'>✅ Cliente e Contas Eliminados Permanentemente!</b></html>\n\n" +
                                "Cliente: " + customer.getFullName() + "\n" +
                                "Email: " + customer.getEmail() + "\n" +
                                "Contas eliminadas: " + totalAccounts + "\n" +
                                "Saldo perdido: " + String.format("%,.2f", totalBalance) + " MT\n\n" +
                                "<b>Todos os dados foram removidos permanentemente da base de dados.</b>";
                    } else {
                        successMessage = "<html><b style='color: red;'>✅ Cliente Eliminado Permanentemente!</b></html>\n\n" +
                                "Cliente: " + customer.getFullName() + "\n" +
                                "Email: " + customer.getEmail() + "\n\n" +
                                "<b>Todos os dados foram removidos permanentemente da base de dados.</b>";
                    }

                    JOptionPane.showMessageDialog(view,
                            successMessage,
                            "Eliminação Completa Concluída",
                            JOptionPane.INFORMATION_MESSAGE);

                    loadCustomers(); // Recarregar lista

                } else {
                    JOptionPane.showMessageDialog(view,
                            "<html><b>Erro ao eliminar cliente!</b></html>\n\n" +
                                    "Possíveis causas:\n" +
                                    "• O cliente não existe\n" +
                                    "• Erro de conexão com a base de dados\n" +
                                    "• Restrições de integridade referencial\n" +
                                    "• Problema nas transações da base de dados\n\n" +
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