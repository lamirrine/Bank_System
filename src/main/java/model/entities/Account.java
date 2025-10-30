package model.entities;

import model.enums.AccountStatus;
import model.enums.AccountType;
import java.util.Date;

public class Account {

    private int accountId;
    private String accountNumber;
    private AccountType accountType;
    private double balance;
    private Date openDate;
    private Date closeDate; // Nullable: Data em que a conta foi encerrada
    private AccountStatus status;
    private int ownerCustomerId; // Chave estrangeira para o dono da conta
    private int agencyId;        // Chave estrangeira para a agência
    private double dailyWithdrawLimit;
    private double dailyTransferLimit;
    private String transactionPinHash; // Hash seguro do PIN de 4 dígitos para transações

    // Construtor Completo
    public Account(int accountId, String accountNumber, AccountType accountType, double balance, Date openDate, Date closeDate, AccountStatus status, int ownerCustomerId, int agencyId, double dailyWithdrawLimit, double dailyTransferLimit, String transactionPinHash) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.status = status;
        this.ownerCustomerId = ownerCustomerId;
        this.agencyId = agencyId;
        this.dailyWithdrawLimit = dailyWithdrawLimit;
        this.dailyTransferLimit = dailyTransferLimit;
        this.transactionPinHash = transactionPinHash;
    }

    public Account() {}

    // --- Getters e Setters ---
    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }
    public int getOwnerCustomerId() { return ownerCustomerId; }
    public void setOwnerCustomerId(int ownerCustomerId) { this.ownerCustomerId = ownerCustomerId; }
    public double getDailyWithdrawLimit() { return dailyWithdrawLimit; }
    public void setDailyWithdrawLimit(double dailyWithdrawLimit) { this.dailyWithdrawLimit = dailyWithdrawLimit; }
    public Date getOpenDate() {return openDate;}
    public void setOpenDate(Date openDate) {this.openDate = openDate;}
    public Date getCloseDate() {return closeDate;}
    public void setCloseDate(Date closeDate) {this.closeDate = closeDate;}
    public int getAgencyId() {return agencyId;}
    public void setAgencyId(int agencyId) {this.agencyId = agencyId;}
    public double getDailyTransferLimit() {return dailyTransferLimit;}
    public void setDailyTransferLimit(double dailyTransferLimit) {this.dailyTransferLimit = dailyTransferLimit;}
    public String getTransactionPinHash() { return transactionPinHash; }
    public void setTransactionPinHash(String transactionPinHash) { this.transactionPinHash = transactionPinHash; }
}