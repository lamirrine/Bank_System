package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer; // <-- ESSENCIAL para resolver o erro
import java.awt.*;
import java.awt.Component; // ESSENCIAL para o método do renderer

public class StatementPanel extends JPanel {

    private static final Color PRIMARY_COLOR = new Color(50, 150, 250);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);

    public StatementPanel() {
        setLayout(new BorderLayout(20, 20)); // Adiciona espaçamento entre as áreas
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. Painel de Filtros (Norte)
        JPanel filterPanel = createFilterPanel();
        add(filterPanel, BorderLayout.NORTH);

        // 2. Painel de Conteúdo (Centro)
        JPanel contentPanel = createContentPanel();
        add(contentPanel, BorderLayout.CENTER);
    }

    // =========================================================================
    // --- 1. PAINEL DE FILTROS (TOP) ---
    // =========================================================================
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Conta
        panel.add(new JLabel("Conta:"));
        JComboBox<String> accountSelector = new JComboBox<>(new String[]{"12345-X (Corrente)", "67890-Y (Poupança)"});
        panel.add(accountSelector);

        // Período
        panel.add(new JLabel("Período:"));
        JComboBox<String> periodSelector = new JComboBox<>(new String[]{"Últimos 7 dias", "Últimos 30 dias", "Últimos 90 dias", "Personalizado"});
        panel.add(periodSelector);

        // Botão Filtrar
        JButton filterButton = new JButton("Filtrar");
        filterButton.setBackground(new Color(255, 165, 0)); // Laranja
        filterButton.setForeground(Color.WHITE);
        filterButton.setFocusPainted(false);
        panel.add(filterButton);

        // Botões de Ação (Exportar/Imprimir)
        JButton exportButton = new JButton("Exportar");
        JButton printButton = new JButton("Imprimir");

        // Adiciona um espaço para separar
        panel.add(Box.createRigidArea(new Dimension(50, 0)));
        panel.add(exportButton);
        panel.add(printButton);

        return panel;
    }

    // =========================================================================
    // --- 2. PAINEL DE CONTEÚDO (CENTER) ---
    // =========================================================================
    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.WHITE);

        // Tabela de Transações
        JScrollPane tableScrollPane = createTransactionTable();
        tabbedPane.addTab("Transações Detalhadas", tableScrollPane);

        tabbedPane.addTab("Resumo e Gráficos", new JLabel("Gráfico de despesas e receitas em construção...", SwingConstants.CENTER));

        panel.add(tabbedPane, BorderLayout.CENTER);

        // Adiciona um rodapé simples
        JLabel footer = new JLabel("Extrato referente ao período selecionado. Saldo Final: MZN 12.345,67", SwingConstants.CENTER);
        footer.setBorder(new EmptyBorder(10, 0, 10, 0));
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    // =========================================================================
    // --- 3. TABELA DE TRANSAÇÕES ---
    // =========================================================================
    private JScrollPane createTransactionTable() {
        String[] columnNames = {"Data", "Descrição", "Categoria", "Valor", "Saldo"};

        // Dados de Exemplo (Baseados no PDF)
        Object[][] data = {
                {"15/06/2023", "Transferência recebida (Maria Santos)", "Transferência", "+MZN 1.500,00", "MZN 12.345,67"},
                {"14/06/2023", "Pagamento de serviços (Electricidade)", "Utilidades", "-MZN 850,50", "MZN 10.845,67"},
                {"12/06/2023", "Depósito em caixa (Agência Central)", "Depósito", "+MZN 2.000,00", "MZN 11.696,17"},
                {"10/06/2023", "Compra com cartão (Supermercado)", "Alimentação", "-MZN 1.250,35", "MZN 9.696,17"},
                {"08/06/2023", "Transferência enviada (Carlos Lima)", "Transferência", "-MZN 500,00", "MZN 10.946,52"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.setDefaultRenderer(Object.class, new TransactionRenderer());

        return new JScrollPane(table);
    }

    // Renderer Customizado para colorir o Valor (Positivo/Negativo)
    private static class TransactionRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (column == 3) { // Coluna 'Valor'
                String text = (String) value;
                if (text.startsWith("+")) {
                    label.setForeground(new Color(0, 150, 0)); // Verde para crédito
                } else if (text.startsWith("-")) {
                    label.setForeground(Color.RED); // Vermelho para débito
                } else {
                    label.setForeground(Color.BLACK);
                }
            } else {
                label.setForeground(Color.BLACK);
            }

            label.setHorizontalAlignment(column == 3 || column == 4 ? SwingConstants.RIGHT : SwingConstants.LEFT);

            return label;
        }
    }
}