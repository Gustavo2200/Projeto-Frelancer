package br.com.myfrilas.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.myfrilas.dao.freelancer.FreelancerDao;
import br.com.myfrilas.err.exceptions.FreelasException;

@Service
public class FreelancerService {
    
    private FreelancerDao freelancerDao;

    public FreelancerService(FreelancerDao freelancerDao) { this.freelancerDao = freelancerDao;}

    public void saveSkill(Long idFreelancer, String skillName) {
        Long idSkill = checkBeforeSaving(skillName);
        if(this.freelancerDao.checkSkillIsAlreadySaved(idSkill, idFreelancer)) {
            throw new FreelasException("Skill ja foi adicionada", HttpStatus.BAD_REQUEST.value());
        }
        this.freelancerDao.saveSkill(idSkill, idFreelancer);
    }

    private Long checkBeforeSaving(String skillName) {

        Long idSkill = this.freelancerDao.checkSkillExists(skillName);

        if(idSkill == null) {
            throw new FreelasException("Skill inexistente", HttpStatus.NOT_FOUND.value());
        }
        return idSkill;
    }
}