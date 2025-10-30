package model.entities;

import java.util.Date;

public abstract class User {

    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String passHash;
    private Date registrationDate;

    // Construtor Completo
    public User(int userId, String firstName, String lastName, String email, String phone, String address, String passHash, Date registrationDate) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.passHash = passHash;
        this.registrationDate = registrationDate;
    }

    public User() {}

    // Método Auxiliar: Nome Completo (útil para a View)
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // --- Getters e Setters ---

    // Essenciais para Login
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassHash() { return passHash; }
    public void setPassHash(String passHash) { this.passHash = passHash; }

    // Informações Pessoais
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Date getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(Date registrationDate) { this.registrationDate = registrationDate; }
}