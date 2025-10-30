package model.entities;

public class Account {
    private int id;
    private String accountNumber;
    private String type;
    private double balance;
    private int ownerId;

    public Account() {}

    public Account(int id, String accountNumber, String type, double balance, int ownerId) {
        this.id = id; this.accountNumber = accountNumber; this.type = type; this.balance = balance; this.ownerId = ownerId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public int getOwnerId() { return ownerId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }
}
