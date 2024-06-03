package br.com.myfrilas.model;

import java.util.List;

public class Client {

    private String name;
    private String email;
    private String phone;
    private String password;
    private byte ranking;
    private List<Project> projects;
    private List<Assessment> assessments;

    public Client() {}
    
    public Client(String name, String email, String phone, String password, byte ranking, List<Project> projects,
            List<Assessment> assessments) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.ranking = ranking;
        this.projects = projects;
        this.assessments = assessments;
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
}