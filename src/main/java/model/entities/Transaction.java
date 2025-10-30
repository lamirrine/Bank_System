package model.entities;

import model.enums.TransactionStatus;
import model.enums.TransactionType;
import java.util.Date;

public class Transaction {

    private int transactionId;
    private TransactionType type;
    private double amount;
    private Date timestamp;
    private TransactionStatus status;
    private String description;
    private int sourceAccountId;
    private int destinationAccountId; // 0 ou NULL se não for transferência
    private double resultingBalance; // Saldo da conta de origem APÓS a transação
    private double feeAmount;        // Taxas ou comissões cobradas

    // Construtor Completo
    public Transaction(int transactionId, TransactionType type, double amount, Date timestamp, TransactionStatus status, String description, int sourceAccountId, int destinationAccountId, double resultingBalance, double feeAmount) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
        this.status = status;
        this.description = description;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.resultingBalance = resultingBalance;
        this.feeAmount = feeAmount;
    }

    // Construtor Padrão
    public Transaction() {}

    // --- Getters e Setters ---
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }
    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getSourceAccountId() { return sourceAccountId; }
    public void setSourceAccountId(int sourceAccountId) { this.sourceAccountId = sourceAccountId; }
    public int getDestinationAccountId() { return destinationAccountId; }
    public void setDestinationAccountId(int destinationAccountId) { this.destinationAccountId = destinationAccountId; }
    public double getResultingBalance() { return resultingBalance; }
    public void setResultingBalance(double resultingBalance) { this.resultingBalance = resultingBalance; }
    public double getFeeAmount() { return feeAmount; }
    public void setFeeAmount(double feeAmount) { this.feeAmount = feeAmount; }
}