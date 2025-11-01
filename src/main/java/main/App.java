package main;

import model.services.CustomerService;
import model.services.AuthenticationService;
import model.dao.IUserDAO;
import model.dao.impl.UserDAO;
import view.LoginFrame;

import javax.swing.SwingUtilities;

public class App {

    private final CustomerService customerService;
    private final AuthenticationService authenticationService;

    public App() {
        IUserDAO userDAO = new UserDAO();
        this.customerService = new CustomerService(userDAO);
        this.authenticationService = new AuthenticationService(userDAO);
    }

    public static void main(String[] args) {
        App application = new App();

        SwingUtilities.invokeLater(() -> {
            new LoginFrame(application.authenticationService, application.customerService).setVisible(true);
        });
    }
}