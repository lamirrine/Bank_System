package model.entities;

import java.time.LocalTime;

public class Agency {

    private int agencyId;
    private String bankCode;
    private String name;
    private String address;
    private String phone;
    private int managerEmployeeId; // ID do funcionário gerente desta agência
    private LocalTime openTime;
    private LocalTime closeTime;

    // Construtor Completo
    public Agency(int agencyId, String bankCode, String name, String address, String phone, int managerEmployeeId, LocalTime openTime, LocalTime closeTime) {
        this.agencyId = agencyId;
        this.bankCode = bankCode;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.managerEmployeeId = managerEmployeeId;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    // Construtor Padrão
    public Agency() {}

    // --- Getters e Setters ---
    public int getAgencyId() { return agencyId; }
    public void setAgencyId(int agencyId) { this.agencyId = agencyId; }
    public String getBankCode() { return bankCode; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public int getManagerEmployeeId() { return managerEmployeeId; }
    public void setManagerEmployeeId(int managerEmployeeId) { this.managerEmployeeId = managerEmployeeId; }
    public LocalTime getOpenTime() { return openTime; }
    public void setOpenTime(LocalTime openTime) { this.openTime = openTime; }
    public LocalTime getCloseTime() { return closeTime; }
    public void setCloseTime(LocalTime closeTime) { this.closeTime = closeTime; }
}