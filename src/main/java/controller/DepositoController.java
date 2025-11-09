package controller;

import config.DatabaseConnection;
import view.DepositoPanel;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.*;

public class DepositoController {
    private final DepositoPanel panel;
    DatabaseConnection db = new DatabaseConnection();

    public DepositoController(DepositoPanel panel) {
        this.panel = panel;
        panel.addConfirmListener(e -> doDepositAsync());
    }

    private void doDepositAsync() {
        SwingWorker<Void, Void> w = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                panel.setStatus("Processando...");
                String acc = panel.getConta();
                String amt = panel.getValorText();
                if (acc.isEmpty() || amt.isEmpty()) {
                    panel.setStatus("Preencha todos os campos.");
                    return null;
                }
                BigDecimal value;
                try { value = new BigDecimal(amt); if (value.compareTo(BigDecimal.ZERO)<=0) throw new NumberFormatException(); }
                catch (Exception ex) { panel.setStatus("Valor inválido."); return null; }

                try (Connection conn = db.getConnection()) {
                    conn.setAutoCommit(false);
                    // encontra conta
                    Integer accId = null;
                    BigDecimal currentBalance = BigDecimal.ZERO;
                    try (PreparedStatement ps = conn.prepareStatement("SELECT id, balance FROM accounts WHERE account_number = ?")) {
                        ps.setString(1, acc);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) { accId = rs.getInt("id"); currentBalance = rs.getBigDecimal("balance"); }
                        }
                    }
                    if (accId == null) {
                        panel.setStatus("Conta não encontrada.");
                        return null;
                    }
                    // inserir transacao e atualizar saldo
                    try (PreparedStatement ins = conn.prepareStatement(
                            "INSERT INTO transactions(account_id, timestamp, description, amount, balance_after) VALUES(?, NOW(), ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS)) {
                        BigDecimal newBal = currentBalance.add(value);
                        ins.setInt(1, accId);
                        ins.setString(2, "Depósito");
                        ins.setBigDecimal(3, value);
                        ins.setBigDecimal(4, newBal);
                        ins.executeUpdate();
                    }
                    try (PreparedStatement upd = conn.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE id = ?")) {
                        upd.setBigDecimal(1, value);
                        upd.setInt(2, accId);
                        upd.executeUpdate();
                    }
                    conn.commit();
                    panel.setStatus("Depósito efetuado com sucesso.");
                } catch (Exception ex) {
                    panel.setStatus("Erro: " + ex.getMessage());
                }
                return null;
            }
        };
        w.execute();
    }
}
