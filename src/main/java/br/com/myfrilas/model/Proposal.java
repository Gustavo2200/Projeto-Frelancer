package br.com.myfrilas.model;

import java.math.BigDecimal;

public class Proposal {
    
    private Long idProject;
    private Long idFreelancer;
    private String comment;
    private BigDecimal value;

    public Proposal() {}

    public Proposal(Long idProject, Long idFreelancer, String comment, BigDecimal value) {
        this.idProject = idProject;
        this.idFreelancer = idFreelancer;
        this.comment = comment;
        this.value = value;
    }

    public Long getIdProject() {
        return idProject;
    }

    public void setIdProject(Long idProject) {
        this.idProject = idProject;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Long getIdFreelancer() {
        return idFreelancer;
    }

    public void setIdFreelancer(Long idFreelancer) {
        this.idFreelancer = idFreelancer;
    }

    

    

}
