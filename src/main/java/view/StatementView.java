package view;

import net.miginfocom.swing.MigLayout;
import view.componet.JDateChooser;
import view.componet.MyButton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StatementView extends JPanel {
    private JComboBox<String> accountComboBox;
    private JComboBox<String> periodComboBox;
    private JDateChooser startDateChooser;
    private JDateChooser endDateChooser;
    private MyButton searchButton;
    private MyButton generatePdfButton;
    private MyButton cancelButton;
    private JTable transactionsTable;
    private JLabel totalLabel;
    private JProgressBar progressBar;
    private JLabel statusLabel;

    public StatementView() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new MigLayout("fill, insets 20", "[grow]", "[][grow]"));
        setBackground(new Color(229, 231, 235));

        // Header
        add(createHeader(), "growx, wrap");

        // Main content with scroll
        JScrollPane scrollPane = createMainContent();
        add(scrollPane, "grow");
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new MigLayout("fill, insets 15 15 15 0", "[grow]", "[]"));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));

        JLabel titleLabel = new JLabel("Extrato Bancário");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(15, 23, 42));

        header.add(titleLabel, "grow");

        return header;
    }

    private JScrollPane createMainContent() {
        JPanel content = new JPanel(new MigLayout("wrap, fillx, insets 20", "[grow]", "[]20[]20[]"));
        content.setBackground(new Color(229, 231, 235));

        // Filters section
        content.add(createFiltersSection(), "growx");

        // Transactions table section
        content.add(createTableSection(), "grow, push");

        // Summary and actions section
        content.add(createSummarySection(), "growx");

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    private JPanel createFiltersSection() {
        JPanel panel = createCard();
        panel.setLayout(new MigLayout("wrap, fill, insets 20", "[grow][]", "[]15[]"));

        JLabel titleLabel = new JLabel("Filtros do Extrato");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(15, 23, 42));

        // Filters content
        JPanel filtersPanel = new JPanel(new MigLayout(" fill, insets 0", "[]10[]60[]10[]200[]10[grow]30[]10[grow]30[grow]", "[]"));
        filtersPanel.setOpaque(false);

        // Account selection
        JLabel accountLabel = new JLabel("Conta:");
        accountLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        accountComboBox = new JComboBox<>(new String[]{"Todas as contas"});
        accountComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Period selection
        JLabel periodLabel = new JLabel("Período:");
        periodLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        periodComboBox = new JComboBox<>(new String[]{"Últimos 7 dias", "Últimos 30 dias", "Este mês", "Mês anterior", "Personalizado"});
        periodComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Date pickers
        JLabel startDateLabel = new JLabel("De:");
        startDateLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        startDateChooser = createDateChooser();

        JLabel endDateLabel = new JLabel("Até:");
        endDateLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        endDateChooser = createDateChooser();

        // Search button
        searchButton = new MyButton();
        searchButton.setText("Buscar");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchButton.setBackground(new Color(59, 130, 246));
        searchButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        filtersPanel.add(accountLabel);
        filtersPanel.add(accountComboBox, "w 260!");
        filtersPanel.add(periodLabel);
        filtersPanel.add(periodComboBox, "w 200!");
        filtersPanel.add(startDateLabel);
        filtersPanel.add(startDateChooser, "growx");
        filtersPanel.add(endDateLabel);
        filtersPanel.add(endDateChooser, "growx");
        filtersPanel.add(searchButton, "span, top, growx");

        panel.add(titleLabel, "growx, wrap");
        panel.add(filtersPanel, "growx");

        return panel;
    }

    private JDateChooser createDateChooser() {
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateChooser.setPreferredSize(new Dimension(120, 30));
        return dateChooser;
    }

    private JPanel createTableSection() {
        JPanel panel = createCard();
        panel.setLayout(new BorderLayout(0, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Transações");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(15, 23, 42));

        // Create table with sample data
        String[] columns = {"Data", "Descrição", "Tipo", "Valor", "Saldo"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        transactionsTable = new JTable(model);
        transactionsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        transactionsTable.setRowHeight(30);
        transactionsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        transactionsTable.getTableHeader().setBackground(new Color(248, 250, 252));
        transactionsTable.setShowGrid(true);
        transactionsTable.setGridColor(new Color(226, 232, 240));
        transactionsTable.setSelectionBackground(new Color(59, 130, 246, 50));

        JScrollPane tableScrollPane = new JScrollPane(transactionsTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSummarySection() {
        JPanel panel = createCard();
        panel.setLayout(new MigLayout("fill, insets 20", "[grow][200!][grow]15", "[]"));

        // Total label
        JPanel totalPanel = new JPanel(new MigLayout("insets 0", "[grow]", "[]5[]"));
        totalPanel.setOpaque(false);

        JLabel totalTitleLabel = new JLabel("Saldo Total no Período");
        totalTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalTitleLabel.setForeground(new Color(107, 114, 128));

        totalLabel = new JLabel("MZN 0,00");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        totalLabel.setForeground(new Color(16, 185, 129));

        totalPanel.add(totalTitleLabel, "growx, wrap");
        totalPanel.add(totalLabel, "growx");

        // Progress bar for PDF generation
        JPanel progressPanel = new JPanel(new MigLayout("wrap, fill, insets 0", "[grow]", "[]5[]"));
        progressPanel.setOpaque(false);

        progressBar = new JProgressBar();
        progressBar.setVisible(false);
        progressBar.setStringPainted(true);

        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(107, 114, 128));

        progressPanel.add(progressBar, "growx, wrap");
        progressPanel.add(statusLabel, "growx");

        // Buttons
        JPanel buttonPanel = new JPanel(new MigLayout("insets 5", "[grow]10[grow]10", "[]"));
        buttonPanel.setOpaque(false);

        cancelButton = new MyButton();
        cancelButton.setText("  Voltar  ");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setBackground(new Color(107, 114, 128));

        generatePdfButton = new MyButton();
        generatePdfButton.setText("Gerar PDF");
        generatePdfButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        generatePdfButton.setBackground(new Color(16, 185, 129));

        // Adicionar efeitos hover aos botões
        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (cancelButton.isEnabled()) {
                    cancelButton.setBackground(new Color(75, 85, 99));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (cancelButton.isEnabled()) {
                    cancelButton.setBackground(new Color(107, 114, 128));
                }
            }
        });

        generatePdfButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (generatePdfButton.isEnabled()) {
                    generatePdfButton.setBackground(new Color(14, 159, 110));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (generatePdfButton.isEnabled()) {
                    generatePdfButton.setBackground(new Color(16, 185, 129));
                }
            }
        });

        buttonPanel.add(progressPanel, "growx");
        buttonPanel.add(cancelButton, "growx");
        buttonPanel.add(generatePdfButton, "growx");

        panel.add(totalPanel, "grow");
        panel.add(new JLabel(), "grow"); // spacer
        panel.add(buttonPanel, "growx, wmax 40%, right");

        return panel;
    }

    private JPanel createCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        card.setLayout(new MigLayout("fill, insets 0", "[grow]", "[grow]"));
        card.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        card.setOpaque(false);
        return card;
    }

    // Getters
    public JComboBox<String> getAccountComboBox() { return accountComboBox; }
    public JComboBox<String> getPeriodComboBox() { return periodComboBox; }
    public JDateChooser getStartDateChooser() { return startDateChooser; }
    public JDateChooser getEndDateChooser() { return endDateChooser; }
    public JButton getSearchButton() { return searchButton; }
    public JButton getGeneratePdfButton() { return generatePdfButton; }
    public JButton getCancelButton() { return cancelButton; }
    public JTable getTransactionsTable() { return transactionsTable; }
    public JLabel getTotalLabel() { return totalLabel; }
    public JProgressBar getProgressBar() { return progressBar; }
    public JLabel getStatusLabel() { return statusLabel; }

    // Métodos para atualizar a tabela
    public void setTableData(Object[][] data, String[] columns) {
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        transactionsTable.setModel(model);
    }

    public void setAccounts(String[] accounts) {
        accountComboBox.removeAllItems();
        accountComboBox.addItem("Todas as contas");
        for (String account : accounts) {
            accountComboBox.addItem(account);
        }
    }

    public void showProgress(boolean show) {
        progressBar.setVisible(show);
        if (show) {
            progressBar.setValue(0);
        }
    }

    public void updateProgress(int value, String status) {
        progressBar.setValue(value);
        statusLabel.setText(status);
    }
}