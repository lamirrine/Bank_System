package model.entities;

public class Employee {
    private int id;
    private String name;
    private String role;
    private int agencyId;

    public Employee() {}

    public Employee(int id, String name, String role, int agencyId) {
        this.id = id; this.name = name; this.role = role; this.agencyId = agencyId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public int getAgencyId() { return agencyId; }
    public void setAgencyId(int agencyId) { this.agencyId = agencyId; }
}
