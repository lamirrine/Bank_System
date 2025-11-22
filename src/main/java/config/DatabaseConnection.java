package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/bancodigital_db";
    private static final String USER = "root"; // <-- Substitua pelo seu usuário MySQL
    private static final String PASSWORD = "Mirrine22!"; // <-- Substitua pela sua senha MySQL

    public static Connection getConnection() throws SQLException {
        try {
            // Carrega o driver JDBC do MySQL (necessário para versões mais antigas do JDBC)
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {

            throw new SQLException("Driver JDBC não encontrado. Verifique se o mysql-connector-j.jar está no classpath.", e);
        }
    }

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