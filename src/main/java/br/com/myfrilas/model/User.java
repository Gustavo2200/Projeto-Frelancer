package br.com.myfrilas.model;

import br.com.myfrilas.enums.TypeUser;

public class User {
    private String name;
    private String cpf;
    private String email;
    private String phone;
    private String password;
    private byte ranking;
    private TypeUser typeUser;

    public User() {}
    
    public User(String name, String cpf, String email, String phone, String password, byte ranking, TypeUser typeUser) {
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.ranking = ranking;
        this.typeUser = typeUser;
    }
    public byte getRanking() {
        return ranking;
    }

    public void setRanking(byte ranking) {
        this.ranking = ranking;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public TypeUser getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(TypeUser typeUser) {
        this.typeUser = typeUser;
    }
}