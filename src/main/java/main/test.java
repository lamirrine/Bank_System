package main;

import controller.DashboardController;
import model.entities.Customer;
import model.services.AccountService;
import model.services.CustomerService;
import model.services.StatementService;
import view.*;
import view.login.componet.Loginview;

import javax.swing.*;

public class test {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                Customer c = new Customer();
                AccountService ac = new AccountService();
                StatementService ss = new StatementService();
                CustomerService cs = new CustomerService();
                //Loginview s = new Loginview();
                DashboardView s = new DashboardView();
                DashboardController d = new DashboardController(s,ac,ss,cs,c);
                s.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Erro ao iniciar aplicação: " + e.getMessage(),
                        "Erro Fatal", JOptionPane.ERROR_MESSAGE);
            }
        });

    }
}
