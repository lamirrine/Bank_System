package view;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;

public class DashboardView extends JFrame {
    private SidebarView sidebarView;
    private HomepageView homepageView;
    private DepositView depositView;
    private WithdrawView withdrawView;
    private TransferView transferView;
    private StatementView statementView;
    private ProfileView profileView;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    public DashboardView() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("HomePage");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 700));
        setLocationRelativeTo(null);

        setLayout(new MigLayout("fill, insets 0", "[280!][grow]", "[grow]"));

        sidebarView = new SidebarView();
        add(sidebarView, "w 280!, h 100%");

        // Main content area with CardLayout
        mainContentPanel = new JPanel();
        cardLayout = new CardLayout();
        mainContentPanel.setLayout(cardLayout);

        // Homepage
        homepageView = new HomepageView();
        mainContentPanel.add(homepageView, "HOME");

        // Deposit view
        depositView = new DepositView();
        mainContentPanel.add(depositView, "DEPOSIT");

        // Withdraw view
        withdrawView = new WithdrawView();
        mainContentPanel.add(withdrawView, "WITHDRAW");

        // Transfer view
        transferView = new TransferView();
        mainContentPanel.add(transferView, "TRANSFER");

        // Statement view
        statementView = new StatementView();
        mainContentPanel.add(statementView, "STATEMENT");
        add(mainContentPanel, "grow");

        // Profile view
        profileView = new ProfileView();
        mainContentPanel.add(profileView, "PROFILE");
        add(mainContentPanel, "grow");
    }

    // Métodos para trocar telas
    public void showHomepage() {
        cardLayout.show(mainContentPanel, "HOME");
    }

    public void showDepositView() {
        cardLayout.show(mainContentPanel, "DEPOSIT");
    }

    public void showWithdrawView() {
        cardLayout.show(mainContentPanel, "WITHDRAW");
    }

    public void showTransferView() {
        cardLayout.show(mainContentPanel, "TRANSFER");
    }

    public void showStatementView() {
        cardLayout.show(mainContentPanel, "STATEMENT");
    }

    public void showProfileView() {
        cardLayout.show(mainContentPanel, "PROFILE");
    }

    // Getters
    public SidebarView getSidebarView() { return sidebarView; }
    public HomepageView getHomepageView() { return homepageView; }
    public DepositView getDepositView() { return depositView; }
    public WithdrawView getWithdrawView() { return withdrawView; }
    public TransferView getTransferView() { return transferView; }
    public StatementView getStatementView() { return statementView; }
    public ProfileView getProfileView() { return profileView; }

    // Métodos para atualizar dados
    public void setBalance(double balance) {
        homepageView.setBalance(balance);
    }

    public void setUserInfo(String name, String accountNumber) {
        sidebarView.setUserInfo(name, accountNumber);
        if (name != null && !name.trim().isEmpty()) {
            String firstName = name.split(" ")[0];
            homepageView.updateUserInfo(firstName);
        }
    }
}