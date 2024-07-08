package br.com.myfrilas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.myfrilas.dao.customer.CustomerDao;
import br.com.myfrilas.dao.freelancer.FreelancerDao;
import br.com.myfrilas.dao.project.ProjectDao;
import br.com.myfrilas.dto.proposal.ProposalDtoRequest;
import br.com.myfrilas.enums.StatusProject;
import br.com.myfrilas.err.exceptions.FreelasException;
import br.com.myfrilas.model.Proposal;
import br.com.myfrilas.model.Skill;

@Service
public class FreelancerService {
    
    private FreelancerDao freelancerDao;
    private ProjectDao projectDao;
    private CustomerDao customerDao;

    public FreelancerService(FreelancerDao freelancerDao, ProjectDao projectDao, CustomerDao customerDao) {
        
        this.freelancerDao = freelancerDao;
        this.projectDao = projectDao;
        this.customerDao = customerDao;
    }

    public void saveSkill(Long idFreelancer, List<Skill> skills) {
        List<Long> idSkills = new ArrayList<>();

        for(Skill skill : skills) {
            Long idSkill = checkBeforeSaving(skill.getSkill());
            if(this.freelancerDao.checkSkillIsAlreadySaved(idSkill, idFreelancer)) {
                throw new FreelasException("Skill ja foi adicionada", HttpStatus.BAD_REQUEST.value());
            }
            idSkills.add(idSkill);
        }
        
        this.freelancerDao.saveSkill(idSkills, idFreelancer);

    }

    public void deleteSkill(Long idFreelancer, List<Skill> skills) {
        
        List<Long> idSkills = new ArrayList<>();
        List<String> skillsFreelancer = this.freelancerDao.getSkillsByFreelancerId(idFreelancer);
        boolean check = false;

        for(int i = 0; i < skillsFreelancer.size(); i++) {
            for(int j = 0; j < skills.size(); j++) {
                if(skillsFreelancer.get(i).equals(skills.get(j).getSkill())) {
                    check = true;
                    break;
                }
            }
        }

        if(!check) {
            throw new FreelasException("Skill não encontrada para esse freelancer", HttpStatus.NOT_FOUND.value());
        }

        for(Skill skill : skills) {
            Long idSkill = checkBeforeSaving(skill.getSkill());
            
            
            if(!this.freelancerDao.checkSkillIsAlreadySaved(idSkill, idFreelancer)) {
                continue;   
           
            }else{
                idSkills.add(idSkill);
            }
            
        }
        freelancerDao.deleteSkill(idSkills, idFreelancer);
    }

    public void sendProposal(ProposalDtoRequest proposal, Long idFreelancer) {
        if(!projectDao.checkProjectExists(proposal.getIdProject())){
            throw new FreelasException("Projeto não encontrado", HttpStatus.NOT_FOUND.value());
        }   
        else if(projectDao.checkStatusProject(proposal.getIdProject()).getType() != StatusProject.OPEN.getType()) {
            throw new FreelasException("Apenas projetos abertos podem receber propostas", HttpStatus.BAD_REQUEST.value());
        }

        if(checkBeforeSend(proposal, idFreelancer)) {
            throw new FreelasException("Proposta ja enviada para esse projeto", HttpStatus.CONFLICT.value());
        }
        freelancerDao.sendProposal(proposal, idFreelancer);
    }

    private Long checkBeforeSaving(String skillName) {

        Long idSkill = this.freelancerDao.idBySkillName(skillName);

        if(idSkill == null) {
            throw new FreelasException("Skill inexistente", HttpStatus.NOT_FOUND.value());
        }
        return idSkill;
    }

    private boolean checkBeforeSend(ProposalDtoRequest proposal, Long idFreelancer) {
        try{
           List<Proposal> proposals = customerDao.viewProposalsByProjectId(proposal.getIdProject());

           for(int i = 0; i < proposals.size(); i++) {
               if((proposals.get(i)).getIdFreelancer() == idFreelancer) {
                return true;
               }
           }

           return false;
        }catch(Exception e) {
            return true;
        }
    }
}