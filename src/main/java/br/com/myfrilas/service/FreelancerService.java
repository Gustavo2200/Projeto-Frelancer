package br.com.myfrilas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.myfrilas.dao.freelancer.FreelancerDao;
import br.com.myfrilas.dto.freelancer.FreelancerDtoResponse;
import br.com.myfrilas.dto.proposal.ProposalDtoRequest;
import br.com.myfrilas.err.exceptions.FreelasException;
import br.com.myfrilas.model.Skill;

@Service
public class FreelancerService {
    
    private FreelancerDao freelancerDao;

    public FreelancerService(FreelancerDao freelancerDao) { this.freelancerDao = freelancerDao;}

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

    public FreelancerDtoResponse freelancerById(Long idFreelancer) {
        FreelancerDtoResponse freelancer = freelancerDao.freelancerById(idFreelancer);
        if(freelancer == null) {
            throw new FreelasException("Freelancer não encontrado", HttpStatus.NOT_FOUND.value());
        }

        return freelancer;
    }

    public void sendProposal(ProposalDtoRequest proposal, Long idFreelancer) {
        if(!this.freelancerDao.checkBeforeSendProposal(proposal.getIdProject())) {
            throw new FreelasException("Projeto não encontrado", HttpStatus.NOT_FOUND.value());
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
}