package model.entities;

import model.enums.AccessLevel; // Importa o Enum
import java.util.Date;

public class Employee extends User {

    private AccessLevel accessLevel;
    private boolean isSupervisor;

    // Construtor Completo
    public Employee(int userId, String firstName, String lastName, String email, String phone, String address, String passHash, Date registrationDate, AccessLevel accessLevel, boolean isSupervisor) {
        super(userId, firstName, lastName, email, phone, address, passHash, registrationDate);
        this.accessLevel = accessLevel;
        this.isSupervisor = isSupervisor;
    }

    // Construtor Padrão
    public Employee() {
        super();
    }

    // --- Getters e Setters ---
    public AccessLevel getAccessLevel() { return accessLevel; }
    public void setAccessLevel(AccessLevel accessLevel) { this.accessLevel = accessLevel; }
    public boolean isSupervisor() { return isSupervisor; }
    public void setSupervisor(boolean supervisor) { isSupervisor = supervisor; }
}