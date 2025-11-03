package model.entities;

import model.enums.UserType;

import java.util.Date;

public class User {

    // Campos principais (Comuns a TODOS os usuários: Cliente e Funcionário)
    protected int userId;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String phone;
    protected String passHash;
    protected String address;       // <-- CORREÇÃO: ENDEREÇO ESTÁ AQUI
    protected UserType userType;      // <-- userType ADICIONADO PARA O LOGIN
    protected Date registrationDate;

    // Construtor completo (para carregamento do DB)
    public User(int userId, String firstName, String lastName, String email, String phone, String passHash, String address, UserType userType) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.passHash = passHash;
        this.address = address;       // Inicializado
        this.userType = userType;     // Inicializado
        this.registrationDate = new Date();
    }

    // Construtor usado no Registo (ID é gerado no DB)
    public User(String firstName, String lastName, String email, String phone, String passHash, String address, UserType userType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.passHash = passHash;
        this.address = address;       // Inicializado
        this.userType = userType;     // Inicializado
        this.registrationDate = new Date();
    }

    //login


    public User(int userId, String email, String passHash) {
        this.userId = userId;
        this.email = email;
        this.passHash = passHash;
    }

    public User(String email, String passHash) {
        this.email = email;
        this.passHash = passHash;
    }

    public User() {
    }

    // Getters e Setters

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getFullName() { return firstName + " " + lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassHash() { return passHash; }
    public void setPassHash(String passHash) { this.passHash = passHash; }
    public Date getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(Date registrationDate) { this.registrationDate = registrationDate; }
}