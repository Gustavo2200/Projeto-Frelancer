package br.com.myfrilas.dto.project;

import java.util.List;

import br.com.myfrilas.enums.StatusProject;
import br.com.myfrilas.model.Skill;

public class ProjectDtoResponse {
    
    private Integer id;
    private String title;
    private String description;
    private String customerName;
    private StatusProject status;
    private List<Skill> skills;
    
    public ProjectDtoResponse() {}

    public ProjectDtoResponse(Integer id, String title, String description, String customerName, StatusProject status, List<Skill> skills) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.customerName = customerName;
        this.status = status;
        this.skills = skills;
    }
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public StatusProject getStatus() {
        return status;
    }
    public void setStatus(StatusProject status) {
        this.status = status;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }
    
}