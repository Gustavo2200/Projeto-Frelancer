package br.com.myfrilas.dto.freelancer;

import java.util.List;

public class FreelancerDtoResponse {
    
    private Long id;
    private String name;
    private String cpf;
    private String email;
    private String phone;
    private List<String> skills;

    public FreelancerDtoResponse() {}

    public FreelancerDtoResponse(Long id, String name, String cpf, String email, String phone, List<String> skills) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.phone = phone;
        this.skills = skills;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
}