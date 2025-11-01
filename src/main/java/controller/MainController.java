// controller/MainController.java (ATUALIZADO)
package controller;

import model.dao.impl.*;
import model.services.AuthenticationService;
import javax.swing.*;

public class MainController {
    private AuthenticationController authController;

    public MainController() {
        initializeServices();
    }

    private void initializeServices() {
        try {
            // Inicializar DAOs
            UserDAO userDAO = new UserDAO();

            // Inicializar serviços
            AuthenticationService authService = new AuthenticationService(userDAO);

            // Inicializar controller de autenticação
            authController = new AuthenticationController(authService);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao inicializar sistema: " + e.getMessage(),
                    "Erro de Inicialização", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public void start() {
        authController.start();
    }
}