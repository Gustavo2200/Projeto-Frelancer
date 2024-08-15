package br.com.myfreelas.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.myfreelas.contantutils.ErrorMessageContants;
import br.com.myfreelas.dao.customer.CustomerDao;
import br.com.myfreelas.dao.project.ProjectDao;
import br.com.myfreelas.enums.StatusProject;
import br.com.myfreelas.err.exceptions.FreelasException;
import br.com.myfreelas.model.Proposal;

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
        return customerDao.viewProposalsByProjectId(idProject);
    }

    public void rejectProposal(Long idProposal, Long idCustomer) {
        if(!customerDao.checkProposalExists(idProposal)) {
            throw new FreelasException(ErrorMessageContants.PROPOSAL_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND.value());
        }
        Long idProject = customerDao.idProjectByIdProposal(idProposal);

        validadeCustomer(idCustomer, idProject);
        customerDao.rejectProposal(idProposal);
    }

    public void acceptProposal(Long idProposal, Long idCustomer) {
        if(!customerDao.checkProposalExists(idProposal)) {
            throw new FreelasException(ErrorMessageContants.PROPOSAL_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND.value());
        }
        Long idProject = customerDao.idProjectByIdProposal(idProposal);
        validadeCustomer(idCustomer, idProject);
        customerDao.acceptProposal(idProposal);
    }

    public void completeProject(Long idProject, Long idCustomer) {

        validadeCustomer(idCustomer, idProject);

        if(projectDao.checkStatusProject(idProject).getType() != StatusProject.IN_PROGRESS.getType()) {
            throw new FreelasException(ErrorMessageContants.PROJECT_NOT_OPEN.getMessage(), HttpStatus.FORBIDDEN.value());
        }

        BigDecimal valueProject = projectDao.priceByProjectId(idProject);

        if(!customerDao.checkBalanceCustomer(idCustomer, valueProject)){
            throw new FreelasException(ErrorMessageContants.INSUFFICIENT_BALANCE.getMessage(), HttpStatus.BAD_REQUEST.value());
        }
        customerDao.completeProject(idProject);
    }

    public void depositBalance(Long idCustomer, BigDecimal value) {
        customerDao.depositBalance(idCustomer, value);
    }

    private void validadeCustomer(Long idCustomer, Long idProject) {

        if(!projectDao.checkProjectExists(idProject)) {
            throw new FreelasException(ErrorMessageContants.PROJECT_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND.value());
        }

        else if(idCustomer != projectDao.customerIdByProjectId(idProject)){
            throw new FreelasException(ErrorMessageContants.CUSTOMER_NOT_OWNER.getMessage(), HttpStatus.BAD_REQUEST.value());
        }
    }
}
