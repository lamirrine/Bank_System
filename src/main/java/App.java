import controller.MainController;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // Configuração do look and feel
       try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Usando look and feel padrão");
        }

        // Inicia a aplicação
        SwingUtilities.invokeLater(() -> {
            try {
                new MainController().start();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Erro ao iniciar aplicação: " + e.getMessage(),
                        "Erro Fatal", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}