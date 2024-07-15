package br.com.myfreelas.dto.proposal;

import java.math.BigDecimal;

public class ProposalDtoRequest {
    
    private Long idProject;
    private String comment;
    private BigDecimal value;

    public ProposalDtoRequest() {}

    public ProposalDtoRequest(Long idProject, String comment, BigDecimal value) {
        this.idProject = idProject;
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

    

    
}
