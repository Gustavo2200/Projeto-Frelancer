package br.com.myfrilas.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.myfrilas.dao.customer.CustomerDao;
import br.com.myfrilas.dao.project.ProjectDao;
import br.com.myfrilas.err.exceptions.FreelasException;
import br.com.myfrilas.model.Proposal;

@Service
public class CustomerService {
    
    private CustomerDao customerDao;
    private ProjectDao projectDao;

    public CustomerService(CustomerDao customerDao, ProjectDao projectDao) {
        this.customerDao = customerDao;
        this.projectDao = projectDao;
    }

    public List<Proposal> viewProposalsByProjectId(Long idProject, Long idCustomer) {
        validadeCustomer(idCustomer, idProject);
        List<Proposal> proposals = customerDao.viewProposalsByProjectId(idProject);
        if(proposals.size() == 0) {
            throw new FreelasException("Nenhum proposta encontrada", HttpStatus.NOT_FOUND.value());
        }
        return proposals;
    }

    public void rejectProposal(Long idProposal, Long idCustomer) {
        Long idProject = customerDao.idProjectByIdProposal(idProposal);

        validadeCustomer(idCustomer, idProject);
        if(!customerDao.checkProposalExists(idProposal)) {
            throw new FreelasException("Proposta nao encontrada", HttpStatus.NOT_FOUND.value());
        }
        customerDao.rejectProposal(idProposal);
    }

    public void acceptProposal(Long idProposal, Long idCustomer) {
        Long idProject = customerDao.idProjectByIdProposal(idProposal);
        validadeCustomer(idCustomer, idProject);
        if(!customerDao.checkProposalExists(idProposal)) {
            throw new FreelasException("Proposta nao encontrada", HttpStatus.NOT_FOUND.value());
        }
        customerDao.acceptProposal(idProposal);
    }

    private void validadeCustomer(Long idCustomer, Long idProject) {
        if(idCustomer != projectDao.customerIdByProjectId(idProject)){
            throw new FreelasException("Voce so pode ver propostas do seu projeto", HttpStatus.FORBIDDEN.value());
        }
    }
}
