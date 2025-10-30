package main;

import config.DatabaseConnection;
import model.dao.impl.*;
import model.services.*;
import model.entities.*;
import utils.security.PasswordUtil;
import java.util.Date;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Classe principal para inicialização e teste da Injeção de Dependência (Wiring).
 * Simula a Camada de Apresentação/Controller.
 */
public class App {

    public static void main(String[] args) {
        System.out.println("--- Sistema Bancário Integrado - Inicialização ---");

        // 1. Testar a Conexão com o Banco de Dados
        testDatabaseConnection();

        // 2. Configurar a Injeção de Dependência (Wiring)
        // Criar as implementações DAO (As dependências de baixo nível)
        MySQLUserDAO userDAO = new MySQLUserDAO();
        MySQLAccountDAO accountDAO = new MySQLAccountDAO();
        MySQLTransactionDAO transactionDAO = new MySQLTransactionDAO();
        MySQLBankDAO bankDAO = new MySQLBankDAO();
        MySQLCustomerDAO customerDAO = new MySQLCustomerDAO();

        // Criar os Serviços, injetando as dependências DAO
        BankService bankService = new BankService(bankDAO);
        AuthenticationService authService = new AuthenticationService(userDAO, accountDAO);
        CustomerService customerService = new CustomerService(userDAO, customerDAO);

        // O AccountService é complexo, precisa de três outros serviços/DAOs
        AccountService accountService = new AccountService(
                accountDAO,
                transactionDAO,
                authService,
                bankService // Note que o BankService é injetado aqui
        );

        // 3. Simular e Testar Funcionalidades
        System.out.println("\n--- Simulação de Uso ---");

        // Criar dados de teste (usando o utilitário SHA-256)
        String hashPassword = PasswordUtil.hashPassword("senha123");

        // SIMULAÇÃO: Criar um novo cliente (requer que o DB tenha sido populado com dados iniciais)
        try {
            Customer newCustomer = new Customer();
            newCustomer.setFirstName("Novo");
            newCustomer.setLastName("Teste");
            newCustomer.setEmail("novo.teste@banco.com");
            newCustomer.setPassHash(hashPassword);
            newCustomer.setRegistrationDate(new Date());
            newCustomer.setBiNumber("123456789X"); // Documento KYC

            // NOTE: Este teste falhará se o método save do MySQLUserDAO não estiver 100% implementado
            // customerService.registerCustomer(newCustomer);

            // SIMULAÇÃO: Login (assumindo que já existe um usuário no DB)
            System.out.println("\n[Teste de Login]");
            // Assumimos que o email 'joao.silva@banco.com' existe no DB com senha 'senha123'
            User loggedInUser = authService.login("joao.silva@banco.com", "senha123");
            System.out.println("Login bem-sucedido! Bem-vindo(a), " + loggedInUser.getFullName());

        } catch (Exception e) {
            System.err.println("Erro na simulação: " + e.getMessage());
        }
    }

    /**
     * Tenta obter uma conexão com o banco de dados.
     */
    private static void testDatabaseConnection() {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            if (conn != null) {
                System.out.println("Status: Conexão com o banco de dados OK.");
            }
        } catch (SQLException e) {
            System.err.println("ERRO CRÍTICO: Falha na conexão com o banco de dados.");
            System.err.println("Mensagem: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }
}