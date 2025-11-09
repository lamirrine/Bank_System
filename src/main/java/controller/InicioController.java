package controller;

import view.InicioPanel;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Vector;

/**
 * InicioController - carrega dados da BD e popula InicioPanel.
 *
 * Usa java.config.DatabaseConnection.getConnection()
 *
 * Ajusta os nomes das tabelas/colunas se o teu schema for diferente.
 */
public class InicioController {
    private final InicioPanel panel;

    public InicioController(InicioPanel panel) {
        this.panel = panel;
        // liga botão atualizar e ações rápidas (se quiseres trocar de tela, adiciona código aqui)
        panel.addDepositarListener(e -> panel.setStatus("Abrir Depósito..."));
        panel.addLevantarListener(e -> panel.setStatus("Abrir Levantamento..."));
        panel.addTransferirListener(e -> panel.setStatus("Abrir Transferência..."));
        panel.addExtratoListener(e -> panel.setStatus("Abrir Extrato..."));

        loadDataAsync();
    }

    /**
     * Carrega saldos, contas e últimas transações em background.
     */
    public void loadDataAsync() {
        SwingWorker<Void, Void> w = new SwingWorker<>() {
            BigDecimal totalBalance = BigDecimal.ZERO;
            Vector<Vector<Object>> accounts = new Vector<>();
            Vector<Vector<Object>> transactions = new Vector<>();

            @Override
            protected Void doInBackground() {
                panel.setStatus("Carregando dados...");
                try (Connection conn = config.DatabaseConnection.getConnection()) {
                    // 1) Ler contas e somar saldo
                    // Assumimos tabela 'accounts' com colunas: id, account_number, account_type, balance
                    try (PreparedStatement ps = conn.prepareStatement(
                            "SELECT account_number, account_type, balance FROM account")) {
                        try (ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) {
                                String accNum = rs.getString("account_number");
                                String accType = rs.getString("account_type");
                                BigDecimal bal = rs.getBigDecimal("balance");
                                Vector<Object> row = new Vector<>();
                                row.add(accNum);
                                row.add(accType != null ? accType : "Conta");
                                row.add(bal != null ? bal : BigDecimal.ZERO);
                                accounts.add(row);
                                if (bal != null) totalBalance = totalBalance.add(bal);
                            }
                        }
                    } catch (SQLException ex) {
                        // Se tabela/colunas diferentes, tenta uma versão simples (account_number,balance)
                        accounts.clear();
                        try (PreparedStatement ps2 = conn.prepareStatement("SELECT account_number, balance FROM account")) {
                            try (ResultSet rs2 = ps2.executeQuery()) {
                                while (rs2.next()) {
                                    String accNum = rs2.getString("account_number");
                                    BigDecimal bal = rs2.getBigDecimal("balance");
                                    Vector<Object> row = new Vector<>();
                                    row.add(accNum);
                                    row.add("Conta");
                                    row.add(bal != null ? bal : BigDecimal.ZERO);
                                    accounts.add(row);
                                    if (bal != null) totalBalance = totalBalance.add(bal);
                                }
                            }
                        } catch (SQLException ignored) {
                            // se falhar, segue com accounts vazio (UI mostra placeholders)
                        }
                    }

                    // 2) Ler últimas transações
                    // Assumimos tabela 'transactions' com timestamp, description, amount, balance_after
                    try (PreparedStatement ps3 = conn.prepareStatement(
                            "SELECT timestamp, description, amount, balance_after FROM transaction ORDER BY timestamp DESC LIMIT 8")) {
                        try (ResultSet rs = ps3.executeQuery()) {
                            while (rs.next()) {
                                Vector<Object> r = new Vector<>();
                                r.add(rs.getTimestamp("timestamp"));
                                r.add(rs.getString("description"));
                                r.add(rs.getBigDecimal("amount"));
                                r.add(rs.getBigDecimal("balance_after"));
                                transactions.add(r);
                            }
                        }
                    } catch (SQLException ex) {
                        // fallback: tentar colunas alternativas ou ignorar
                        transactions.clear();
                    }

                    // 3) (opcional) limites podem vir de tabela config ou hardcoded — mantemos hardcoded por enquanto
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(panel,
                            "Erro ao conectar à BD: " + ex.getMessage(),
                            "Erro BD", JOptionPane.ERROR_MESSAGE));
                }
                return null;
            }

            @Override
            protected void done() {
                panel.setTotalBalance(totalBalance);
                panel.setAccounts(accounts);
                panel.setRecentTransactions(transactions);
                panel.setLimits("MZN 3.250,00", "MZN 10.000,00");
                panel.setStatus("Dados atualizados.");
            }
        };
        w.execute();
    }
}
