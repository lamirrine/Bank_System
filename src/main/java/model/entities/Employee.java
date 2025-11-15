// Employee.java
package model.entities;

import model.enums.AccessLevel;
import model.enums.UserType;

public class Employee extends User {
    private AccessLevel accessLevel;
    private boolean isSupervisor;

    public Employee() {
        super();
        this.setUserType(UserType.EMPLOYEE);
        this.accessLevel = AccessLevel.STAFF;
        this.isSupervisor = false;
    }

    public Employee(String firstName, String lastName, String email, String phone,
                    String passHash, String address, AccessLevel accessLevel, boolean isSupervisor) {
        super(firstName, lastName, email, phone, passHash, address, UserType.EMPLOYEE);
        this.accessLevel = accessLevel;
        this.isSupervisor = isSupervisor;
    }

    public Employee(int userId, String firstName, String lastName, String email,
                    String phone, String passHash, String address, AccessLevel accessLevel, boolean isSupervisor) {
        super(userId, firstName, lastName, email, phone, passHash, address, UserType.EMPLOYEE);
        this.accessLevel = accessLevel;
        this.isSupervisor = isSupervisor;
    }

    // Getters e Setters
    public AccessLevel getAccessLevel() { return accessLevel; }
    public void setAccessLevel(AccessLevel accessLevel) { this.accessLevel = accessLevel; }

    public boolean isSupervisor() { return isSupervisor; }
    public void setSupervisor(boolean supervisor) { isSupervisor = supervisor; }

    @Override
    public String toString() {
        return getFullName() + " - " + accessLevel + (isSupervisor ? " (Supervisor)" : "");
    }
}