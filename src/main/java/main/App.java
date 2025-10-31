package main;

// ... (todas as suas importações DAO/Service/Config)

import model.dao.IAccountDAO;
import model.dao.IUserDAO;
import model.services.AuthenticationService;
import model.services.CustomerService; // Necessário para inicializar
import view.LoginFrame; // NOVO: Importar a GUI

import javax.swing.*;

// ... (todas as suas classes DAO/Service/Config)

public class App {

    public static void main(String[] args) {
        // ... (Teste de Conexão com DB)

        // Inicialização dos DAOs (assumindo que está correto)
        IUserDAO userDAO = new model.dao.impl.MySQLUserDAO();
        // ... outros DAOs
        IAccountDAO accountDAO = new model.dao.impl.MySQLAccountDAO();

        // Inicialização dos Serviços (assumindo que está correto)
        CustomerService customerService = new CustomerService(userDAO, new model.dao.impl.MySQLCustomerDAO());
        AuthenticationService authenticationService = new AuthenticationService(userDAO, accountDAO);

        // --- LANÇAR A GUI NA THREAD DE EVENTOS DE SWING (EDT) ---
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(authenticationService);
            loginFrame.setVisible(true);
        });

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Fallback para o Look and Feel padrão (Metal/Cross-platform)
        }

        // O código de simulação pode ser apagado/comentado
        // ...
    }
}