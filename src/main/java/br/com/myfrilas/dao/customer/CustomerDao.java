package br.com.myfrilas.dao.customer;

import java.util.List;

import br.com.myfrilas.model.Proposal;

public interface CustomerDao {
    
    List<Proposal> viewProposalsByProjectId(Long idProject);

    void acceptProposal(Long idPropoal);

    void rejectProposal(Long idPropoal);

    boolean checkProposalExists(Long idProposal);

    Long idProjectByIdProposal(Long idProposal);
}
