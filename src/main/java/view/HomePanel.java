package view;

//import util.DB;

import config.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Vector;

/**
 * Painel "Início" — mostra cartões de saldo e últimas transações.
 * Se preferires, podes injetar um AccountService em vez de usar o DB direto.
 */
public class HomePanel extends JPanel {
    private JLabel lblAvailable;
    private JTable recentTable;
    DatabaseConnection DB = new DatabaseConnection();

    public HomePanel() {
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
        initUI();
    }

    private void initUI() {
        // top cards
        JPanel cards = new JPanel(new GridLayout(1, 3, 12, 12));
        cards.add(makeCard("Saldo Disponível", "MZN 0.00", "💳"));
        cards.add(makeCard("Limite de Levantamento", "MZN 3.250,00", "⚖️"));
        cards.add(makeCard("Limite Transferência", "MZN 10.000,00", "🔒"));
        add(cards, BorderLayout.NORTH);

        // recent transactions table
        recentTable = new JTable();
        JScrollPane sp = new JScrollPane(recentTable);
        sp.setBorder(BorderFactory.createTitledBorder("Últimas transações"));
        add(sp, BorderLayout.CENTER);

        // bottom controls
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refresh = new JButton("Atualizar");
        refresh.addActionListener(e -> loadRecentTransactionsAsync());
        bottom.add(refresh);
        add(bottom, BorderLayout.SOUTH);
    }

    private JPanel makeCard(String title, String value, String icon) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220)),
                BorderFactory.createEmptyBorder(12,12,12,12)
        ));
        JLabel t = new JLabel("<html><b>"+title+"</b></html>");
        JLabel v = new JLabel("<html><h2>"+value+"</h2></html>");
        JLabel i = new JLabel(icon);
        p.add(t, BorderLayout.NORTH);
        p.add(v, BorderLayout.CENTER);
        p.add(i, BorderLayout.EAST);
        return p;
    }

    // Public API: carregar dados assíncrono
    public void loadSampleDataAsync() {
        SwingUtilities.invokeLater(() -> {
            lblAvailable = new JLabel();
            // tenta carregar saldo e transações da BD
            loadBalanceAsync();
            loadRecentTransactionsAsync();
        });
    }

    private void loadBalanceAsync() {
        SwingWorker<BigDecimal, Void> w = new SwingWorker<>() {
            @Override
            protected BigDecimal doInBackground() throws Exception {
                try (Connection conn = DB.getConnection();
                     PreparedStatement ps = conn.prepareStatement("SELECT SUM(balance) as total FROM accounts")) {
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            return rs.getBigDecimal("total");
                        }
                    }
                } catch (SQLException ex) {
                    // se tabela diferente, retornamos 0
                }
                return BigDecimal.ZERO;
            }
            @Override
            protected void done() {
                try {
                    BigDecimal val = get();
                    // atualiza primeiro cartão
                    Component c = ((JPanel)getComponent(0)).getComponent(0);
                    // para simplicidade apenas atualizamos um diálogo se houver
                    // (melhora: atualizar label dentro do card)
                } catch (Exception ignored) {}
            }
        };
        w.execute();
    }

    private void loadRecentTransactionsAsync() {
        SwingWorker<Vector<Vector<Object>>, Void> w = new SwingWorker<>() {
            @Override
            protected Vector<Vector<Object>> doInBackground() {
                Vector<Vector<Object>> rows = new Vector<>();
                try (Connection conn = DB.getConnection();
                     PreparedStatement ps = conn.prepareStatement(
                             "SELECT timestamp, description, amount, balance_after FROM transactions ORDER BY timestamp DESC LIMIT 8")) {
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            Vector<Object> r = new Vector<>();
                            r.add(rs.getTimestamp("timestamp"));
                            r.add(rs.getString("description"));
                            r.add(rs.getBigDecimal("amount"));
                            r.add(rs.getBigDecimal("balance_after"));
                            rows.add(r);
                        }
                    }
                } catch (SQLException ex) {
                    // fallback: mostrar mensagem
                    JOptionPane.showMessageDialog(HomePanel.this,
                            "Não foi possível carregar transações: " + ex.getMessage(),
                            "Erro", JOptionPane.WARNING_MESSAGE);
                }
                return rows;
            }

            @Override
            protected void done() {
                try {
                    Vector<Vector<Object>> data = get();
                    Vector<String> cols = new Vector<>();
                    cols.add("Data");
                    cols.add("Descrição");
                    cols.add("Valor");
                    cols.add("Saldo");
                    recentTable.setModel(new javax.swing.table.DefaultTableModel(data, cols));
                } catch (Exception ex) { /* ignore */ }
            }
        };
        w.execute();
    }
}
