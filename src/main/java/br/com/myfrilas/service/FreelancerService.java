package br.com.myfrilas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.myfrilas.dao.freelancer.FreelancerDao;
import br.com.myfrilas.dao.project.ProjectDao;
import br.com.myfrilas.dto.proposal.ProposalDtoRequest;
import br.com.myfrilas.enums.StatusProject;
import br.com.myfrilas.err.exceptions.FreelasException;
import br.com.myfrilas.model.Skill;

@Service
public class FreelancerService {
    
    private FreelancerDao freelancerDao;
    private ProjectDao projectDao;

    public FreelancerService(FreelancerDao freelancerDao, ProjectDao projectDao) {
        
        this.freelancerDao = freelancerDao;
        this.projectDao = projectDao;
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