package main;

import view.*;

import javax.swing.*;

public class test {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                //Loginview s = new Loginview();
                DashboardView s = new DashboardView();
                s.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Erro ao iniciar aplicação: " + e.getMessage(),
                        "Erro Fatal", JOptionPane.ERROR_MESSAGE);
            }
        });

    }
}
