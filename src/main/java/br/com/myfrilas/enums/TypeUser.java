package br.com.myfrilas.enums;

public enum TypeUser {
    CUSTOMER(0, "CUSTOMER"),
    FREELANCER(1, "FREELANCER"),
    ADMIN(2, "ADMIN");
    

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

    public static TypeUser fromDescription(String description) {
        for (TypeUser type : TypeUser.values()) {
            if (type.getDescription().equalsIgnoreCase(description)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant with description " + description);
    }
}
