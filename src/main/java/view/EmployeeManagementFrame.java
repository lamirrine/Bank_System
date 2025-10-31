package view;

import model.entities.User;

import javax.swing.*;
import java.awt.*;

public class EmployeeManagementFrame extends JFrame {

    public EmployeeManagementFrame(User user) {
        setTitle("Banco Digital - Gestão de Funcionários | Bem-vindo(a), " + user.getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        JLabel welcomeLabel = new JLabel("PAINEL DE GESTÃO DE FUNCIONÁRIOS (EM CONSTRUÇÃO)", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));

        add(welcomeLabel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }
}