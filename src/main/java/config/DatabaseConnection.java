package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitária para gerenciar a conexão JDBC com o banco de dados MySQL.
 */
public class DatabaseConnection {
    // Parâmetros de conexão - AJUSTE AQUI
    private static final String URL = "jdbc:mysql://localhost:3306/bancodigital_db";
    private static final String USER = "root"; // <-- Substitua pelo seu usuário MySQL
    private static final String PASSWORD = "Mirrine22!"; // <-- Substitua pela sua senha MySQL

    /**
     * Retorna uma nova conexão com o banco de dados.
     * @return Objeto Connection.
     * @throws SQLException Se a conexão falhar ou o driver não for encontrado.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Carrega o driver JDBC do MySQL (necessário para versões mais antigas do JDBC)
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            // Lança uma exceção se o JAR do conector MySQL estiver faltando.
            throw new SQLException("Driver JDBC não encontrado. Verifique se o mysql-connector-j.jar está no classpath.", e);
        }
    }

    /**
     * Fecha a conexão de forma segura.
     * @param conn A conexão a ser fechada.
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}