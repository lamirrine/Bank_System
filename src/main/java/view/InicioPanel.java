package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Vector;

/**
 * InicioPanel - painel principal do "Início" com layout fiel ao PDF.
 *
 * Usa a controller InicioController para popular dados (ele chama loadDataAsync()).
 * Exposes listeners públicos para os botões rápidos.
 */
public class InicioPanel extends JPanel {
    // Cartões / cabeçalhos
    private JLabel lblTotalBalance;
    private JPanel accountsCardsContainer;

    // Ações rápidas
    private JButton btnDepositar;
    private JButton btnLevantar;
    private JButton btnTransferir;
    private JButton btnExtrato;

    // Limites e contactos
    private JLabel lblLimiteLevantamento;
    private JLabel lblLimiteTransferencia;
    private JLabel lblContactSupport;

    // Tabela de transações
    private JTable tblRecent;
    private DefaultTableModel tblModel;

    // Status
    private JLabel lblStatus;

    private final NumberFormat moneyFmt = NumberFormat.getCurrencyInstance(new Locale("pt", "MZ"));

    public InicioPanel() {
        setLayout(new BorderLayout(12, 12));
        setBackground(new Color(0xF5F7FA));
        setBorder(new EmptyBorder(14, 14, 14, 14));
        initComponents();
    }

    private void initComponents() {
        // Top area: total balance + quick actions
        JPanel topArea = new JPanel(new BorderLayout(12, 12));
        topArea.setOpaque(false);

        // Left: Total balance & accounts cards
        JPanel leftTop = new JPanel(new BorderLayout(10, 10));
        leftTop.setOpaque(false);

        JPanel totalCard = createCardPanel();
        JLabel title = new JLabel("Saldo Disponível");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));
        lblTotalBalance = new JLabel(moneyFmt.format(0.0));
        lblTotalBalance.setFont(lblTotalBalance.getFont().deriveFont(Font.BOLD, 24f));
        totalCard.add(title, BorderLayout.NORTH);
        totalCard.add(lblTotalBalance, BorderLayout.CENTER);
        leftTop.add(totalCard, BorderLayout.NORTH);

        accountsCardsContainer = new JPanel(new GridLayout(1, 2, 10, 10));
        accountsCardsContainer.setOpaque(false);
        // cards fill dynamically (Account Card)
        leftTop.add(accountsCardsContainer, BorderLayout.CENTER);

        topArea.add(leftTop, BorderLayout.CENTER);

        // Right: Quick actions
        JPanel quickActions = new JPanel(new GridLayout(2, 2, 10, 10));
        quickActions.setOpaque(false);
        btnDepositar = makeBigActionButton("➕ Depósito");
        btnLevantar = makeBigActionButton("🏧 Levantamento");
        btnTransferir = makeBigActionButton("🔁 Transferência");
        btnExtrato = makeBigActionButton("📄 Extrato");
        quickActions.add(btnDepositar);
        quickActions.add(btnLevantar);
        quickActions.add(btnTransferir);
        quickActions.add(btnExtrato);

        JPanel rightWrap = new JPanel(new BorderLayout());
        rightWrap.setOpaque(false);
        rightWrap.add(quickActions, BorderLayout.NORTH);

        // Limits & Contacts card under quick actions
        JPanel infoCard = createCardPanel();
        infoCard.setLayout(new GridLayout(3, 1, 6, 6));
        lblLimiteLevantamento = new JLabel("Limite Levantamento: MZN 3.250,00");
        lblLimiteTransferencia = new JLabel("Limite Transferência: MZN 10.000,00");
        lblContactSupport = new JLabel("Apoio ao Cliente: 84 123 4567");
        infoCard.add(lblLimiteLevantamento);
        infoCard.add(lblLimiteTransferencia);
        infoCard.add(lblContactSupport);
        rightWrap.add(infoCard, BorderLayout.CENTER);

        topArea.add(rightWrap, BorderLayout.EAST);

        add(topArea, BorderLayout.NORTH);

        // Center: Recent transactions table
        tblModel = new DefaultTableModel(new Object[]{"Data", "Descrição", "Valor (MZN)", "Saldo (MZN)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblRecent = new JTable(tblModel);
        tblRecent.setFillsViewportHeight(true);
        tblRecent.setRowHeight(28);
        JScrollPane sp = new JScrollPane(tblRecent);
        sp.setBorder(BorderFactory.createTitledBorder("Últimas Transações"));
        add(sp, BorderLayout.CENTER);

        // Bottom: status and actions
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        lblStatus = new JLabel(" ");
        bottom.add(lblStatus, BorderLayout.WEST);

        // quick links at bottom right (Find Agencies / Map)
        JPanel bottomRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        bottomRight.setOpaque(false);
        JButton btnFindAgencies = new JButton("Encontrar Agências");
        JButton btnBlockCard = new JButton("Bloquear Cartão");
        bottomRight.add(btnFindAgencies);
        bottomRight.add(btnBlockCard);
        bottom.add(bottomRight, BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);
    }

    // Cria painel "card" branco com borda leve
    private JPanel createCardPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xE6E9EE)),
                new EmptyBorder(12, 12, 12, 12)
        ));
        return p;
    }

    private JButton makeBigActionButton(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setBackground(new Color(0x1976D2));
        b.setForeground(Color.WHITE);
        b.setPreferredSize(new Dimension(180, 60));
        b.setFont(b.getFont().deriveFont(Font.BOLD, 12f));
        return b;
    }

    // Public API used by controller -------------------------------------------------

    /**
     * Substitui os cards de conta com a lista (label + valor).
     * Recebe vetor de arrays: {accountNumber:String, accountType:String, balance:BigDecimal}
     */
    public void setAccounts(Vector<Vector<Object>> accounts) {
        accountsCardsContainer.removeAll();
        if (accounts == null || accounts.isEmpty()) {
            // placeholder cards
            accountsCardsContainer.add(makeAccountCard("Conta Corrente", "12345-X", BigDecimal.ZERO));
            accountsCardsContainer.add(makeAccountCard("Conta Poupança", "67890-Y", BigDecimal.ZERO));
        } else {
            for (Vector<Object> acc : accounts) {
                String type = (String) acc.get(1);
                String number = (String) acc.get(0);
                BigDecimal bal = (BigDecimal) acc.get(2);
                accountsCardsContainer.add(makeAccountCard(type + " - " + number, number, bal));
            }
        }
        revalidate();
        repaint();
    }

    private JPanel makeAccountCard(String titleText, String accountNumber, BigDecimal balance) {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(6, 6));
        JLabel t = new JLabel(titleText);
        t.setFont(t.getFont().deriveFont(Font.PLAIN, 12f));
        JLabel bal = new JLabel(formatMoney(balance));
        bal.setFont(bal.getFont().deriveFont(Font.BOLD, 16f));
        card.add(t, BorderLayout.NORTH);
        card.add(bal, BorderLayout.CENTER);
        return card;
    }

    public void setTotalBalance(BigDecimal total) {
        lblTotalBalance.setText(formatMoney(total));
    }

    public void setLimits(String limiteLevant, String limiteTransf) {
        lblLimiteLevantamento.setText("Limite Levantamento: " + limiteLevant);
        lblLimiteTransferencia.setText("Limite Transferência: " + limiteTransf);
    }

    /**
     * Substitui as linhas da tabela de transações.
     * rows: Vector<Vector<Object>> com colunas [Timestamp, description, amount, balance_after]
     */
    public void setRecentTransactions(Vector<Vector<Object>> rows) {
        tblModel.setRowCount(0);
        if (rows != null) {
            for (Vector<Object> r : rows) {
                Object data = r.get(0);
                Object desc = r.get(1);
                Object amt = r.get(2);
                Object bal = r.get(3);
                String amtStr = amt instanceof java.math.BigDecimal ? formatMoney((java.math.BigDecimal)amt) : String.valueOf(amt);
                String balStr = bal instanceof java.math.BigDecimal ? formatMoney((java.math.BigDecimal)bal) : String.valueOf(bal);
                tblModel.addRow(new Object[]{ data, desc, amtStr, balStr });
            }
        }
    }

    public void setStatus(String s) { lblStatus.setText(s); }

    // Listeners públicos para ligares às outras telas
    public void addDepositarListener(java.awt.event.ActionListener l) { btnDepositar.addActionListener(l); }
    public void addLevantarListener(java.awt.event.ActionListener l) { btnLevantar.addActionListener(l); }
    public void addTransferirListener(java.awt.event.ActionListener l) { btnTransferir.addActionListener(l); }
    public void addExtratoListener(java.awt.event.ActionListener l) { btnExtrato.addActionListener(l); }

    private String formatMoney(BigDecimal val) {
        if (val == null) return moneyFmt.format(0.0);
        return moneyFmt.format(val);
    }

    // Test main para ver o painel isolado
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Teste - Início");
            InicioPanel p = new InicioPanel();
            f.setContentPane(p);
            f.setSize(1000, 640);
            f.setLocationRelativeTo(null);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setVisible(true);
        });
    }
}
