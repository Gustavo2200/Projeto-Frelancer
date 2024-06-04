package br.com.myfrilas.enums;

public enum TypeUser {
    CUSTOMER(0, "Cliente"),
    FREELANCER(1, "Freelancer");

    private int type;
    private String description;

    TypeUser(int type, String description) {
        this.type = type;
        this.description = description;
    }

    public int getType() {
        return type;
    }   

    public String getDescription() {    
        return description;
    }
}
