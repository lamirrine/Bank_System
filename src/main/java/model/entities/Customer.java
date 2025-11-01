package model.entities;

import model.enums.UserType;

// A classe Customer estende User, adicionando campos específicos de cliente.
public class Customer extends User {

    // Campos que SÃO ESPECÍFICOS DO CLIENTE (não estão em User)
    private String biNumber;
    private String nuit;
    private String passportNumber;

    // Construtor Padrão
    public Customer() {
        super();
        this.setUserType(UserType.valueOf("CUSTOMER"));
    }

    // Construtor Completo (para Registo)
    public Customer(String firstName, String lastName, String email, String phone, String passHash, String address, String biNumber, String nuit, String passportNumber) {
        // CORREÇÃO: Passa 'address' e 'userType' para o construtor da superclasse
        super(firstName, lastName, email, phone, passHash, address, UserType.valueOf("CUSTOMER"));

        this.biNumber = biNumber;
        this.nuit = nuit;
        this.passportNumber = passportNumber;
    }

    // Construtor Usado para Carregamento (inclui userId)
    public Customer(int userId, String firstName, String lastName, String email, String phone, String passHash, String address, String biNumber, String nuit, String passportNumber) {
        // CORREÇÃO: Passa 'address' e 'userType' para o construtor da superclasse
        super(userId, firstName, lastName, email, phone, passHash, address, UserType.valueOf("CUSTOMER"));

        this.biNumber = biNumber;
        this.nuit = nuit;
        this.passportNumber = passportNumber;
    }

    // Getters e Setters... (apenas para campos de cliente)
    public String getBiNumber() { return biNumber; }
    public void setBiNumber(String biNumber) { this.biNumber = biNumber; }

    public String getNuit() { return nuit; }
    public void setNuit(String nuit) { this.nuit = nuit; }

    public String getPassportNumber() { return passportNumber; }
    public void setPassportNumber(String passportNumber) { this.passportNumber = passportNumber; }
}