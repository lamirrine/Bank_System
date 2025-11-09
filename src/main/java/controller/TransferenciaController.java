package controller;

import view.TransferenciaPanel;
import javax.swing.*;
import java.math.BigDecimal;
import java.sql.*;

public class TransferenciaController {
    private final TransferenciaPanel panel;

    public TransferenciaController(TransferenciaPanel panel) {
        this.panel = panel;
        panel.addTransferListener(e -> doTransferAsync());
    }

    private void doTransferAsync() {
        SwingWorker<Void, Void> w = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                panel.setStatus("Processando...");
                String src = panel.getOrigem();
                String dst = panel.getDestino();
                String amt = panel.getValorText();
                String pin = panel.getPin();
                if (src.isEmpty()||dst.isEmpty()||amt.isEmpty()||pin.isEmpty()) { panel.setStatus("Preencha todos os campos."); return null; }
                BigDecimal value;
                try { value = new BigDecimal(amt); if (value.compareTo(BigDecimal.ZERO)<=0) throw new NumberFormatException(); }
                catch (Exception ex) { panel.setStatus("Valor inválido."); return null; }

                try (Connection conn = config.DatabaseConnection.getConnection()) {
                    conn.setAutoCommit(false);
                    Integer srcId=null, dstId=null;
                    BigDecimal srcBal=BigDecimal.ZERO, dstBal=BigDecimal.ZERO;
                    String srcPin=null;
                    try (PreparedStatement ps = conn.prepareStatement("SELECT id, balance, pin FROM account WHERE account_number = ?")) {
                        ps.setString(1, src);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) { srcId=rs.getInt("id"); srcBal=rs.getBigDecimal("balance"); srcPin = rs.getString("pin"); }
                        }
                    }
                    try (PreparedStatement ps = conn.prepareStatement("SELECT id, balance FROM account WHERE account_number = ?")) {
                        ps.setString(1, dst);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) { dstId=rs.getInt("id"); dstBal=rs.getBigDecimal("balance"); }
                        }
                    }
                    if (srcId==null || dstId==null) { panel.setStatus("Conta origem/destino não encontrada."); return null; }
                    if (srcPin==null || !srcPin.equals(pin)) { panel.setStatus("PIN inválido."); return null; }
                    if (srcBal.compareTo(value) < 0) { panel.setStatus("Saldo insuficiente."); return null; }

                    // inserir transações e atualizar saldos
                    try (PreparedStatement ins = conn.prepareStatement(
                            "INSERT INTO transaction(account_id, timestamp, description, amount, balance_after) VALUES(?, NOW(), ?, ?, ?)")) {
                        // débito na origem
                        ins.setInt(1, srcId);
                        ins.setString(2, "Transferência para " + dst);
                        ins.setBigDecimal(3, value.negate());
                        ins.setBigDecimal(4, srcBal.subtract(value));
                        ins.executeUpdate();

                        // crédito no destino
                        ins.setInt(1, dstId);
                        ins.setString(2, "Transferência de " + src);
                        ins.setBigDecimal(3, value);
                        ins.setBigDecimal(4, dstBal.add(value));
                        ins.executeUpdate();
                    }
                    try (PreparedStatement upd = conn.prepareStatement("UPDATE accounts SET balance = ? WHERE id = ?")) {
                        upd.setBigDecimal(1, srcBal.subtract(value)); upd.setInt(2, srcId); upd.executeUpdate();
                        upd.setBigDecimal(1, dstBal.add(value)); upd.setInt(2, dstId); upd.executeUpdate();
                    }
                    conn.commit();
                    panel.setStatus("Transferência realizada.");
                } catch (Exception ex) {
                    panel.setStatus("Erro: " + ex.getMessage());
                }
                return null;
            }
        };
        w.execute();
    }
}
