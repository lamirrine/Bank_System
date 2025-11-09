package controller;

import view.LevantamentoPanel;
import javax.swing.*;
import java.math.BigDecimal;
import java.sql.*;

public class LevantamentoController {
    private final LevantamentoPanel panel;

    public LevantamentoController(LevantamentoPanel panel) {
        this.panel = panel;
        panel.addConfirmListener(e -> doWithdrawAsync());
    }

    private void doWithdrawAsync() {
        SwingWorker<Void, Void> w = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                panel.setStatus("Processando...");
                String amt = panel.getValorText();
                String pin = panel.getPin();
                if (amt.isEmpty() || pin.isEmpty()) { panel.setStatus("Preencha todos os campos."); return null; }
                BigDecimal value;
                try { value = new BigDecimal(amt); if (value.compareTo(BigDecimal.ZERO)<=0) throw new NumberFormatException(); }
                catch (Exception ex) { panel.setStatus("Valor inválido."); return null; }

                try (Connection conn = config.DatabaseConnection.getConnection()) {
                    conn.setAutoCommit(false);
                    // aqui assumimos que existe uma conta logada; para demo procuramos a conta 12345-X
                    Integer accId = null;
                    BigDecimal balance = BigDecimal.ZERO;
                    try (PreparedStatement ps = conn.prepareStatement("SELECT id, balance, pin FROM accounts WHERE account_number = ?")) {
                        ps.setString(1, "12345-X"); // ajustar conforme sessão/conta logada
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                accId = rs.getInt("id");
                                balance = rs.getBigDecimal("balance");
                                String storedPin = rs.getString("pin");
                                if (storedPin == null || !storedPin.equals(pin)) {
                                    panel.setStatus("PIN inválido.");
                                    return null;
                                }
                            }
                        }
                    }
                    if (accId == null) { panel.setStatus("Conta não encontrada."); return null; }
                    if (balance.compareTo(value) < 0) { panel.setStatus("Saldo insuficiente."); return null; }

                    try (PreparedStatement ins = conn.prepareStatement(
                            "INSERT INTO transactions(account_id, timestamp, description, amount, balance_after) VALUES(?, NOW(), ?, ?, ?)")) {
                        BigDecimal newBal = balance.subtract(value);
                        ins.setInt(1, accId);
                        ins.setString(2, "Levantamento");
                        ins.setBigDecimal(3, value.negate()); // negativo
                        ins.setBigDecimal(4, newBal);
                        ins.executeUpdate();
                    }
                    try (PreparedStatement upd = conn.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE id = ?")) {
                        upd.setBigDecimal(1, value);
                        upd.setInt(2, accId);
                        upd.executeUpdate();
                    }
                    conn.commit();
                    panel.setStatus("Levantamento efetuado.");
                } catch (Exception ex) {
                    panel.setStatus("Erro: " + ex.getMessage());
                }
                return null;
            }
        };
        w.execute();
    }
}
