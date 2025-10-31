package model.entities;

import model.enums.AccessLevel; // IMPORTAR O ENUM AQUI!
import java.util.Date;

// A classe Employee estende User
public class Employee extends User {

    private String employeeId;
    private AccessLevel accessLevel; // <-- CORREÇÃO: Tipo mudou para Enum
    private boolean isSupervisor;

    // Construtor Padrão
    public Employee() {
        super();
        this.setUserType("EMPLOYEE");
    }

    // Construtor para Registo
    public Employee(String firstName, String lastName, String email, String phone, String passHash, String address,
                    String employeeId, AccessLevel accessLevel, boolean isSupervisor) { // <-- AccessLevel no argumento

        super(firstName, lastName, email, phone, passHash, address, "EMPLOYEE");

        this.employeeId = employeeId;
        this.accessLevel = accessLevel;
        this.isSupervisor = isSupervisor;
    }

    // Construtor para Carregamento (inclui userId)
    public Employee(int userId, String firstName, String lastName, String email, String phone, String passHash, String address,
                    String employeeId, AccessLevel accessLevel, boolean isSupervisor) { // <-- AccessLevel no argumento

        super(userId, firstName, lastName, email, phone, passHash, address, "EMPLOYEE");

        this.employeeId = employeeId;
        this.accessLevel = accessLevel;
        this.isSupervisor = isSupervisor;
    }

    // --- Getters e Setters ---
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    // --- CORREÇÃO: Getter e Setter para AccessLevel (agora é do tipo Enum) ---
    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) { // O setter recebe o Enum
        this.accessLevel = accessLevel;
    }
    // -------------------------------------------------------------------------

    public boolean isSupervisor() {
        return isSupervisor;
    }

    public void setIsSupervisor(boolean isSupervisor) {
        this.isSupervisor = isSupervisor;
    }
}