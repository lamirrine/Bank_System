package model.entities;

import java.util.Date;

public class Transaction {
    private int id;
    private int accountId;
    private String type;
    private double amount;
    private String description;
    private Date createdAt;
    private double balanceAfter;

    public Transaction() {}

    public Transaction(int id, int accountId, String type, double amount, String description, Date createdAt, double balanceAfter) {
        this.id = id; this.accountId = accountId; this.type = type; this.amount = amount; this.description = description; this.createdAt = createdAt; this.balanceAfter = balanceAfter;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public double getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(double balanceAfter) { this.balanceAfter = balanceAfter; }
}
