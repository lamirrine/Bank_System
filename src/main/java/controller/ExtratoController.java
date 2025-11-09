package controller;

import view.ExtratoPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class ExtratoController {
    private final ExtratoPanel panel;

    public ExtratoController(ExtratoPanel panel) {
        this.panel = panel;
        panel.addRefreshListener(e -> loadRecent());
        panel.addFilterListener(e -> loadFiltered());
        loadRecent();
    }

    public void loadRecent() {
        SwingUtilities.invokeLater(() -> {
            DefaultTableModel m = new DefaultTableModel(new Object[]{"Data","Descrição","Valor","Saldo"}, 0);
            try (Connection conn = config.DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "SELECT timestamp, description, amount, balance_after FROM transactions ORDER BY timestamp DESC LIMIT 200")) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        m.addRow(new Object[]{rs.getTimestamp("timestamp"), rs.getString("description"),
                                rs.getBigDecimal("amount"), rs.getBigDecimal("balance_after")});
                    }
                }
                panel.setTableModel(m);
                panel.setStatus("Carregado.");
            } catch (Exception ex) {
                panel.setStatus("Erro: " + ex.getMessage());
            }
        });
    }

    public void loadFiltered() {
        SwingUtilities.invokeLater(() -> {
            String from = panel.getFrom();
            String to = panel.getTo();
            DefaultTableModel m = new DefaultTableModel(new Object[]{"Data","Descrição","Valor","Saldo"}, 0);
            try (Connection conn = config.DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "SELECT timestamp, description, amount, balance_after FROM transactions WHERE timestamp BETWEEN ? AND ? ORDER BY timestamp DESC")) {
                ps.setString(1, from + " 00:00:00");
                ps.setString(2, to + " 23:59:59");
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        m.addRow(new Object[]{rs.getTimestamp("timestamp"), rs.getString("description"),
                                rs.getBigDecimal("amount"), rs.getBigDecimal("balance_after")});
                    }
                }
                panel.setTableModel(m);
                panel.setStatus("Filtrado.");
            } catch (Exception ex) {
                panel.setStatus("Erro: " + ex.getMessage());
            }
        });
    }
}
