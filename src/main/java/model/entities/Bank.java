package model.entities;

public class Bank {

    private String bankCode;
    private String bankName;
    private double transferFeeRate; // Taxa percentual para transferências externas (Ex: 0.005 para 0.5%)
    private String supportPhone;
    private String emergencyPhone;

    // Construtor Completo
    public Bank(String bankCode, String bankName, double transferFeeRate, String supportPhone, String emergencyPhone) {
        this.bankCode = bankCode;
        this.bankName = bankName;
        this.transferFeeRate = transferFeeRate;
        this.supportPhone = supportPhone;
        this.emergencyPhone = emergencyPhone;
    }

    // Construtor Padrão
    public Bank() {}

    // --- Getters e Setters ---
    public String getBankCode() { return bankCode; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public double getTransferFeeRate() { return transferFeeRate; }
    public void setTransferFeeRate(double transferFeeRate) { this.transferFeeRate = transferFeeRate; }
    public String getSupportPhone() { return supportPhone; }
    public void setSupportPhone(String supportPhone) { this.supportPhone = supportPhone; }
    public String getEmergencyPhone() { return emergencyPhone; }
    public void setEmergencyPhone(String emergencyPhone) { this.emergencyPhone = emergencyPhone; }
}