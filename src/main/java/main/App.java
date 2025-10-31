package main;

import model.services.CustomerService;
import model.services.AuthenticationService;
import model.dao.IUserDAO;
import model.dao.impl.MySQLUserDAO; // ASSUMIMOS QUE ESTA É A CLASSE QUE IMPLEMENTA IUserDAO
import view.LoginFrame;

import javax.swing.SwingUtilities;

public class App {

    private final CustomerService customerService;
    private final AuthenticationService authenticationService;

    public App() {
        // 1. Criação do DAO (Instancia a classe CONCRETA)
        // Se a sua implementação tem um nome diferente, altere "MySQLUserDAO"
        IUserDAO userDAO = new MySQLUserDAO();

        // 2. Injeção de Dependência: Passa o DAO para o construtor do Service
        this.customerService = new CustomerService(userDAO);
        this.authenticationService = new AuthenticationService(userDAO);
    }

    // Método principal
    public static void main(String[] args) {
        App application = new App();

        SwingUtilities.invokeLater(() -> {
            // Injeta ambos os serviços no LoginFrame
            new LoginFrame(application.authenticationService, application.customerService).setVisible(true);
        });
    }

    // ...
}