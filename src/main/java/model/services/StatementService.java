package model.services;

import model.dao.ITransactionDAO;
import model.dao.impl.TransactionDAO;
import model.entities.Transaction;
import model.enums.TransactionType;

import java.sql.SQLException;
import java.util.Date;
import java.util.*;
import java.util.stream.Collectors;

public class StatementService {

    private ITransactionDAO transactionDAO;

    public StatementService() {
        this.transactionDAO = new TransactionDAO();
    }

    public StatementService(ITransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    public List<Transaction> getRecentTransactionsByAccount(int accountId, int limit) {
        try {
            return transactionDAO.findByAccountId(accountId, limit);
        } catch (Exception e) {
            System.err.println("Erro ao buscar transações da conta: " + e.getMessage());
            return List.of();
        }
    }

    public List<Transaction> getRecentTransactionsByCustomer(int customerId, int limit) {
        try {
            return transactionDAO.findByCustomerId(customerId, limit);
        } catch (Exception e) {
            System.err.println("Erro ao buscar transações do cliente: " + e.getMessage());
            return List.of();
        }
    }

    // No StatementService.java, adicione estes métodos:

    /**
     * Obtém todas as transações do sistema (para administração)
     */
    public List<Transaction> getAllTransactions() {
        try {
            return transactionDAO.findAll();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todas as transações: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Retorna lista vazia em caso de erro
        }
    }

    /**
     * Busca transações com filtros (para administração)
     */
    public List<Transaction> findTransactionsByFilters(String type, String status, Date startDate, Date endDate) {
        try {
            return transactionDAO.findByFilters(type, status, startDate, endDate);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar transações com filtros: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Obtém estatísticas gerais das transações
     */
    public Map<String, Object> getTransactionStats(Date startDate, Date endDate) {
        Map<String, Object> stats = new HashMap<>();
        try {
            List<Transaction> transactions = transactionDAO.findByDateRange(startDate, endDate);

            long totalTransactions = transactions.size();
            double totalVolume = transactions.stream()
                    .mapToDouble(Transaction::getAmount)
                    .sum();
            double totalFees = transactions.stream()
                    .mapToDouble(Transaction::getFeeAmount)
                    .sum();

            long depositos = transactions.stream()
                    .filter(t -> t.getType() == TransactionType.DEPOSITO)
                    .count();
            long levantamentos = transactions.stream()
                    .filter(t -> t.getType() == TransactionType.LEVANTAMENTO)
                    .count();
            long transferencias = transactions.stream()
                    .filter(t -> t.getType() == TransactionType.TRANSFERENCIA)
                    .count();

            stats.put("totalTransactions", totalTransactions);
            stats.put("totalVolume", totalVolume);
            stats.put("totalFees", totalFees);
            stats.put("depositos", depositos);
            stats.put("levantamentos", levantamentos);
            stats.put("transferencias", transferencias);

        } catch (SQLException e) {
            System.err.println("Erro ao calcular estatísticas de transações: " + e.getMessage());
            e.printStackTrace();
        }
        return stats;
    }
    // NOVO: Buscar transações por conta e período
    public List<Transaction> getTransactionsByAccountAndPeriod(int accountId, Date startDate, Date endDate) {
        try {
            List<Transaction> allTransactions = transactionDAO.findByAccountId(accountId, 1000); // Buscar muitas transações
            return filterTransactionsByPeriod(allTransactions, startDate, endDate);
        } catch (Exception e) {
            System.err.println("Erro ao buscar transações por período: " + e.getMessage());
            return List.of();
        }
    }

    // NOVO: Buscar transações por cliente e período
    public List<Transaction> getTransactionsByCustomerAndPeriod(int customerId, Date startDate, Date endDate) {
        try {
            List<Transaction> allTransactions = transactionDAO.findByCustomerId(customerId, 1000); // Buscar muitas transações
            return filterTransactionsByPeriod(allTransactions, startDate, endDate);
        } catch (Exception e) {
            System.err.println("Erro ao buscar transações por período: " + e.getMessage());
            return List.of();
        }
    }

    // NOVO: Buscar todas as transações de um cliente (sem limite)
    public List<Transaction> getAllTransactionsByCustomer(int customerId) {
        try {
            // Usar um número grande para buscar todas as transações
            return transactionDAO.findByCustomerId(customerId, 10000);
        } catch (Exception e) {
            System.err.println("Erro ao buscar todas as transações do cliente: " + e.getMessage());
            return List.of();
        }
    }

    // NOVO: Buscar todas as transações de uma conta (sem limite)
    public List<Transaction> getAllTransactionsByAccount(int accountId) {
        try {
            // Usar um número grande para buscar todas as transações
            return transactionDAO.findByAccountId(accountId, 10000);
        } catch (Exception e) {
            System.err.println("Erro ao buscar todas as transações da conta: " + e.getMessage());
            return List.of();
        }
    }

    private List<Transaction> filterTransactionsByPeriod(List<Transaction> transactions, Date startDate, Date endDate) {
        // Ajustar as datas para incluir todo o dia
        Date adjustedStartDate = adjustToStartOfDay(startDate);
        Date adjustedEndDate = adjustToEndOfDay(endDate);

        return transactions.stream()
                .filter(t -> isDateInRange(t.getTimestamp(), adjustedStartDate, adjustedEndDate))
                .sorted((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp())) // Mais recentes primeiro
                .collect(Collectors.toList());
    }

    private boolean isDateInRange(Date dateToCheck, Date startDate, Date endDate) {
        return !dateToCheck.before(startDate) && !dateToCheck.after(endDate);
    }

    private Date adjustToStartOfDay(Date date) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.set(java.util.Calendar.MINUTE, 0);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Date adjustToEndOfDay(Date date) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
        cal.set(java.util.Calendar.MINUTE, 59);
        cal.set(java.util.Calendar.SECOND, 59);
        cal.set(java.util.Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    // NOVO: Calcular saldo total de todas as contas do cliente
    public double getTotalBalanceForCustomer(int customerId) {
        try {
            List<Transaction> recentTransactions = getRecentTransactionsByCustomer(customerId, 100);
            if (recentTransactions.isEmpty()) {
                return 0.0;
            }

            // Agrupar por conta e pegar o saldo mais recente de cada uma
            return recentTransactions.stream()
                    .collect(Collectors.groupingBy(Transaction::getSourceAccountId))
                    .values()
                    .stream()
                    .mapToDouble(transactions -> {
                        // Pegar a transação mais recente de cada conta
                        Transaction latest = transactions.stream()
                                .max((t1, t2) -> t1.getTimestamp().compareTo(t2.getTimestamp()))
                                .orElse(null);
                        return latest != null ? latest.getResultingBalance() : 0.0;
                    })
                    .sum();

        } catch (Exception e) {
            System.err.println("Erro ao calcular saldo total: " + e.getMessage());
            return 0.0;
        }
    }
}