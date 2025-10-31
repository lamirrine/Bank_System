package view;

import model.entities.User;
import javax.swing.*;
import java.awt.*;

// Ecrã que o Cliente vê após o login (Dashboard)
public class ClientDashboardFrame extends JFrame {

    public ClientDashboardFrame(User user) {
        setTitle("Dashboard do Cliente: " + user.getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Bem-vindo ao seu Banco Digital, " + user.getFirstName() + "! (Dashboard em construção)", SwingConstants.CENTER);
        add(welcomeLabel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }
}