package br.com.myfrilas.model;

import java.math.BigDecimal;

public class Proposal {
    
    private Long idProposal;
    private Long idFreelancer;
    private String comment;
    private BigDecimal value;

    public Proposal() {}

    public Proposal(Long idProposal, Long idFreelancer, String comment, BigDecimal value) {
        this.idProposal = idProposal;
        this.idFreelancer = idFreelancer;
        this.comment = comment;
        this.value = value;
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

    public Long getIdProposal() {
        return idProposal;
    }

    public void setIdProposal(Long idProposal) {
        this.idProposal = idProposal;
    }

    

    

}
