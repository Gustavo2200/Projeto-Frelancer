package br.com.myfreelas.dao.customer;

import java.math.BigDecimal;
import java.util.List;

import br.com.myfreelas.model.Proposal;

public interface CustomerDao {
    
    List<Proposal> viewProposalsByProjectId(Long idProject);

    void acceptProposal(Long idPropoal);

    void rejectProposal(Long idPropoal);

    void completeProject(Long idProject);

    boolean checkProposalExists(Long idProposal);

    boolean checkBalanceCustomer(Long idCustomer, BigDecimal valueProject);

    Long idProjectByIdProposal(Long idProposal);

    void depositBalance(Long idCustomer, BigDecimal value);
}
