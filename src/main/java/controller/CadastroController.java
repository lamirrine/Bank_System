package controller;

import view.CreateAccountsPanel;

import javax.swing.*;
import java.sql.*;

public class CadastroController {
    private final CreateAccountsPanel panel;

    public CadastroController(CreateAccountsPanel panel) {
        this.panel = panel;
        panel.addCadastrarListener(e -> doRegisterAsync());
    }

    private void doRegisterAsync() {
        SwingWorker<Void, Void> w = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                panel.setStatus("Registrando...");
                String nome = panel.getNome();
                String email = panel.getEmail();
                String conta = panel.getConta();
                if (nome.isEmpty() || email.isEmpty() || conta.isEmpty()) { panel.setStatus("Preencha todos os campos."); return null; }

                try (Connection conn = config.DatabaseConnection.getConnection()) {
                    try (PreparedStatement ps = conn.prepareStatement(
                            "INSERT INTO customer(name, email) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS)) {
                        ps.setString(1, nome);
                        ps.setString(2, email);
                        ps.executeUpdate();
                        int customerId = -1;
                        try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) customerId = rs.getInt(1); }

                        // cria conta vinculada
                        try (PreparedStatement ps2 = conn.prepareStatement(
                                "INSERT INTO account(customer_id, account_number, balance) VALUES(?, ?, ? )")) {
                            ps2.setInt(1, customerId);
                            ps2.setString(2, conta);
                            ps2.setBigDecimal(3, java.math.BigDecimal.ZERO);
                            ps2.executeUpdate();
                        }
                    }
                    panel.setStatus("Cadastro efetuado.");
                } catch (Exception ex) {
                    panel.setStatus("Erro: " + ex.getMessage());
                }
                return null;
            }
        };
        w.execute();
    }
}
