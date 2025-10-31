package view;

import model.entities.User;
import javax.swing.*;
import java.awt.*;

// Ecrã que o Funcionário vê após o login (Gestão)
public class EmployeeManagementFrame extends JFrame {

    public EmployeeManagementFrame(User user) {
        setTitle("Painel de Gestão: " + user.getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Painel de Gestão de Funcionários. (Em construção)", SwingConstants.CENTER);
        add(welcomeLabel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }
}