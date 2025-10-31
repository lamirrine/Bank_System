package main;

import config.DatabaseConnection;
// Importações de interfaces DAO
import model.dao.IAccountDAO;
import model.dao.ICustomerDAO;
import model.dao.IUserDAO;
// Importações das implementações DAO
import model.dao.impl.MySQLAccountDAO;
import model.dao.impl.MySQLCustomerDAO;
import model.dao.impl.MySQLUserDAO;

import model.entities.Customer;
import model.entities.User;
import model.exceptions.InvalidCredentialsException;
import model.services.AuthenticationService;
import model.services.CustomerService;
import utils.security.PasswordUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public class App {

    public static void main(String[] args) {
        System.out.println("--- Sistema Bancário Integrado - Inicialização ---");

        // 1. TESTE DE CONEXÃO COM O BANCO DE DADOS
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Status: Conexão com o banco de dados OK.");
            }
        } catch (SQLException e) {
            System.err.println("ERRO CRÍTICO: Falha na conexão com o banco de dados.");
            System.err.println("Mensagem: " + e.getMessage());
            return;
        }

        System.out.println("\n--- Simulação de Uso ---");

        // --- DADOS DE TESTE NOVOS E LIMPOS ---
        String newEmail = "cliente.teste.novo@banco.mz";
        String newPassword = "testepass123";


        // =========================================================================
        // --- CORREÇÃO DE COMPILAÇÃO: INSTANCIAÇÃO DE DEPENDÊNCIAS ---
        // Instanciar os DAOs para poder passá-los para os Construtores dos Serviços.
        // O erro 'Expected 2 arguments but found 0' foi resolvido aqui.
        IUserDAO userDAO = new MySQLUserDAO();
        ICustomerDAO customerDAO = new MySQLCustomerDAO();
        IAccountDAO accountDAO = new MySQLAccountDAO(); // Assumindo esta é a dependência

        // Instanciar os Serviços (passando as dependências)
        // ATENÇÃO: Verifique os argumentos exatos que seus construtores exigem.
        CustomerService customerService = new CustomerService(userDAO, customerDAO);
        AuthenticationService authenticationService = new AuthenticationService(userDAO, accountDAO);
        // =========================================================================


        try {
            // 1. CRIAÇÃO DO OBJETO CLIENTE
            Customer newCustomer = new Customer();
            newCustomer.setFirstName("Cliente");
            newCustomer.setLastName("Teste");
            newCustomer.setEmail(newEmail);
            newCustomer.setPhone("849876543");
            newCustomer.setAddress("Avenida da Liberdade, 10");
            newCustomer.setBiNumber("999999999");
            newCustomer.setNuit("000000000");
            newCustomer.setPassportNumber("P12345678");
            newCustomer.setRegistrationDate(new Date());

            // --- CORREÇÃO DE LÓGICA: HASHING ANTES DO REGISTO ---
            // Salva o hash da senha em texto simples no objeto antes de enviá-lo para o DB.
            newCustomer.setPassHash(PasswordUtil.hashPassword(newPassword));
            // ----------------------------------------------------

            // 2. REGISTRO (Persistência)
            System.out.println("\n[Teste de Registo] Tentando registrar Cliente Teste...");
            Customer registeredCustomer = customerService.registerCustomer(newCustomer);
            System.out.println("Status: Registo bem-sucedido! ID Gerado: " + registeredCustomer.getUserId());

            // 3. LOGIN IMEDIATO (Autenticação)
            System.out.println("\n[Teste de Login] Tentando Login com a senha em texto simples...");

            // O login usa a senha em TEXTO SIMPLES. O AuthenticationService fará o re-hash e a comparação.
            User loggedInUser = authenticationService.login(newEmail, newPassword);

            System.out.println("Resultado: Login bem-sucedido! Bem-vindo(a), " + loggedInUser.getFullName());
            System.out.println("ID do Usuário logado: " + loggedInUser.getUserId());


        } catch (InvalidCredentialsException e) {
            System.err.println("\nErro na simulação: Falha na autenticação. " + e.getMessage());
        } catch (Exception e) {
            System.err.println("\nErro na simulação: Falha crítica. A transação pode não ter sido concluída.");
            e.printStackTrace();
        }

        System.out.println("\n--- Simulação Concluída ---");
    }
}