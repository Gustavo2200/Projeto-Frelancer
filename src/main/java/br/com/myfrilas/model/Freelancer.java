package br.com.myfrilas.model;

import java.util.List;

public class Freelancer extends User{
    
    private List<String> skills;
   
    public Freelancer(String name, String cpf, String email, String phone, String password, byte ranking, List<Project> projects,
    List<Assessment> assessments, List<String> skills) {
        super(name, cpf, email, phone, password, ranking, projects, assessments);
        this.skills = skills;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
    
}
