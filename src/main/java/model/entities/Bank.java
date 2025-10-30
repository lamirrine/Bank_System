package model.entities;

public class Bank {
    private int id;
    private String name;
    private String code;
    private String supportPhone;

    public Bank() {}

    public Bank(int id, String name, String code, String supportPhone) {
        this.id = id; this.name = name; this.code = code; this.supportPhone = supportPhone;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getSupportPhone() { return supportPhone; }
    public void setSupportPhone(String supportPhone) { this.supportPhone = supportPhone; }
}
