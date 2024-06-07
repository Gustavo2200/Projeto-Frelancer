package br.com.myfrilas.model;

import java.math.BigDecimal;
import java.util.List;

import br.com.myfrilas.dto.user.UserDto;
import br.com.myfrilas.enums.StatusProject;

public class Project {
    
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private UserDto client; 
    private UserDto freelancer;
    private StatusProject status;
    private List<Skill> necessarySkills;

    public Project() {}
    
    public Project(Long id, String title, String description, BigDecimal price, UserDto client, UserDto freelancer,
            StatusProject status, List<Skill> necessarySkills ) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.client = client;
        this.freelancer = freelancer;
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
    public UserDto getClient() {
        return client;
    }
    public void setClient(UserDto client) {
        this.client = client;
    }
    public UserDto getFreelancer() {
        return freelancer;
    }
    public void setFreelancer(UserDto freelancer) {
        this.freelancer = freelancer;
    }
    public StatusProject getStatus() {
        return status;
    }
    public void setStatus(StatusProject status) {
        this.status = status;
    }

    public List<Skill> getSkills() {
        return necessarySkills;
    }

    public void setSkills(List<Skill> necessarySkills) {
        this.necessarySkills = necessarySkills;
    }
    
}
