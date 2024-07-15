package br.com.myfreelas.model;

import br.com.myfreelas.enums.TypeUser;

public class User {
    private String name;
    private String cpfCNPJ;
    private String email;
    private String phone;
    private String password;
    private TypeUser typeUser;

    public User() {}
    
    public User(String name, String cpfCNPJ, String email, String phone, String password, TypeUser typeUser) {
        this.name = name;
        this.cpfCNPJ = cpfCNPJ;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.typeUser = typeUser;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getCpfCNPJ() {
        return cpfCNPJ;
    }

    public void setCpfCNPJ(String cpfCNPJ) {
        this.cpfCNPJ = cpfCNPJ;
    }

    public TypeUser getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(TypeUser typeUser) {
        this.typeUser = typeUser;
    }
}