package br.com.myfrilas.model;

import java.util.List;

import br.com.myfrilas.enums.TypeUser;

public class User {
    private String name;
    private String cpf;
    private String email;
    private String phone;
    private String password;
    private byte ranking;
    private TypeUser typeUser;
    private List<Project> projects;
    private List<Assessment> assessments;

    public User() {}
    
    public User(String name, String cpf, String email, String phone, String password, byte ranking, TypeUser typeUser,
            List<Project> projects, List<Assessment> assessments) {
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.ranking = ranking;
        this.typeUser = typeUser;
        this.projects = projects;
        this.assessments = assessments;
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
    public List<Project> getProjects() {
        return projects;
    }
    
    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
    public List<Assessment> getAssessments() {
        return assessments;
    }
    public void setAssessments(List<Assessment> assessments) {
        this.assessments = assessments;
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