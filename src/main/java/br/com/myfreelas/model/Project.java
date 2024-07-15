package br.com.myfreelas.model;

import java.math.BigDecimal;
import java.util.List;

import br.com.myfreelas.enums.StatusProject;

public class Project {
    
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private Long clientId; 
    private Long freelancerId;
    private StatusProject status;
    private List<String> necessarySkills;

    public Project() {}
    
    public Project(Long id, String title, String description, BigDecimal price, Long clientId, Long freelancerId,
            StatusProject status, List<String> necessarySkills ) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.clientId = clientId;
        this.freelancerId = freelancerId;
        this.status = status;
        this.necessarySkills = necessarySkills;
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
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public Long getClientId() {
        return clientId;
    }
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
    public Long getFreelancerId() {
        return freelancerId;
    }
    public void setFreelancerId(Long freelancerId) {
        this.freelancerId = freelancerId;
    }
    public StatusProject getStatus() {
        return status;
    }
    public void setStatus(StatusProject status) {
        this.status = status;
    }

    public List<String> getSkills() {
        return necessarySkills;
    }

    public void setSkills(List<String> necessarySkills) {
        this.necessarySkills = necessarySkills;
    }
    
}
