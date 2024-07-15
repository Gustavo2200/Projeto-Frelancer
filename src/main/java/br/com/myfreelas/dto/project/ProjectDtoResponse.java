package br.com.myfreelas.dto.project;

import java.util.List;

import br.com.myfreelas.enums.StatusProject;

public class ProjectDtoResponse {
    
    private Long id;
    private String title;
    private String description;
    private Long customerId;
    private String customerName;
    private StatusProject status;
    private List<String> skills;
    
    public ProjectDtoResponse() {}

    public ProjectDtoResponse(Long id, String title, String description, Long customerId, String customerName, 
                StatusProject status, List<String> skills) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.customerId = customerId;
        this.customerName = customerName;
        this.status = status;
        this.skills = skills;
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
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

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    
}
