package model.entities;

import java.util.Date;

// Cliente herda de User e adiciona documentos para KYC
public class Customer extends User {

    private String biNumber;
    private String nuit;
    private String passportNumber; // Opcional para clientes estrangeiros

    // Construtor Completo
    public Customer(int userId, String firstName, String lastName, String email, String phone, String address, String passHash, Date registrationDate, String biNumber, String nuit, String passportNumber) {
        super(userId, firstName, lastName, email, phone, address, passHash, registrationDate);
        this.biNumber = biNumber;
        this.nuit = nuit;
        this.passportNumber = passportNumber;
    }

    // Construtor Padrão
    public Customer() {
        super();
    }

    // --- Getters e Setters ---
    public String getBiNumber() { return biNumber; }
    public void setBiNumber(String biNumber) { this.biNumber = biNumber; }
    public String getNuit() { return nuit; }
    public void setNuit(String nuit) { this.nuit = nuit; }
    public String getPassportNumber() { return passportNumber; }
    public void setPassportNumber(String passportNumber) { this.passportNumber = passportNumber; }
}