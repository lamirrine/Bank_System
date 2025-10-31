package model.services;

import model.dao.ICustomerDAO;
import model.dao.IUserDAO;
import model.entities.Customer;
import java.sql.SQLException;

/**
 * Serviço responsável pelo registo e gestão dos dados específicos do Cliente (KYC).
 * Orquestra o DAO de User e o DAO de Customer para garantir a integridade.
 */
public class CustomerService {

    // Dependências injetadas
    private IUserDAO userDAO;
    private ICustomerDAO customerDAO;


    // Construtor para Injeção de Dependência
    public CustomerService(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }
    public CustomerService(IUserDAO userDAO, ICustomerDAO customerDAO) {
        this.userDAO = userDAO;
        this.customerDAO = customerDAO;
    }

    /**
     * Regista um novo cliente no sistema (processo KYC completo).
     * Envolve a criação de um registro na tabela User e depois na tabela Customer.
     * @param customer O objeto Customer contendo dados pessoais e documentos.
     * @return O objeto Customer registado (com o ID gerado).
     * @throws Exception Se alguma validação falhar ou ocorrer erro de DB.
     */
    public Customer registerCustomer(Customer customer) throws Exception {

        // --- 1. Lógica de Validação (KYC) ---
        if (customer.getEmail() == null || customer.getEmail().isEmpty()) {
            throw new IllegalArgumentException("O email é obrigatório.");
        }

        // Regra de Negócio: Deve ter pelo menos um documento de identificação
        if (customer.getBiNumber() == null && customer.getPassportNumber() == null) {
            throw new IllegalArgumentException("É obrigatório fornecer BI ou Passaporte para registo.");
        }

        // Regra de Negócio: Verificar duplicidade
        // TODO: userDAO.findByEmail(customer.getEmail()) deve ser null
        // TODO: customerDAO.findByBiNumber(customer.getBiNumber()) deve ser null

        // --- 2. Preparação de Dados (Segurança) ---
        // TODO: Encriptar a senha inicial antes de salvar
        // customer.setPassHash(PasswordUtil.hash(customer.getPassHash()));

        // --- 3. Transação Atómica (Salvar User e Customer) ---
        try {
            // A. Salvar na tabela user (obtem o novo user_id através do DAO)
            userDAO.save(customer);

            // B. Usar o user_id retornado (agora em customer.getUserId()) para salvar na tabela customer
            customerDAO.save(customer);

            return customer;
        } catch (SQLException e) {
            // CRÍTICO: Em caso de falha no customerDAO.save(), o userDAO.save() PRECISA de ROLLBACK
            throw new Exception("Falha crítica no registo do cliente. A transação foi revertida.", e);
        }
    }

    /**
     * Busca os dados completos de um cliente (dados de User + dados de Customer).
     */
    public Customer findCustomerById(int customerId) throws SQLException {
        // O CustomerDAO é responsável por buscar os dados das duas tabelas (JOIN)
        return customerDAO.findById(customerId);
    }
}