package br.com.myfreelas.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.myfreelas.contantutils.ErrorMessageContants;
import br.com.myfreelas.dao.skill.SkillDao;
import br.com.myfreelas.dto.skill.SkillDto;
import br.com.myfreelas.err.exceptions.FreelasException;
import br.com.myfreelas.model.Skill;

@Service
public class SkillService {
    
    private SkillDao skillDao;

    public SkillService(SkillDao skillDao) {
        this.skillDao = skillDao;
    }

    public void saveSkill(Skill skill) {
        if(skillDao.checkSkillExists(skill.getSkill())){
            throw new FreelasException(ErrorMessageContants.SKILL_ALREADY_SAVED.getMessage(), HttpStatus.CONFLICT.value());
        }
        skillDao.saveSkill(skill);
    }

    public List<SkillDto> getSkills() {
        return skillDao.getSkills();
    }

    public void deleteSkill(Long idSkill) {
            if(skillDao.getSkillById(idSkill) == null){
                throw new FreelasException(ErrorMessageContants.SKILL_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND.value());
            }
            skillDao.deleteSkill(idSkill);
    }

    public void updateSkill(SkillDto skillDto) {
            if(skillDao.getSkillById(skillDto.getId()) == null){
                throw new FreelasException(ErrorMessageContants.SKILL_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND.value());
            }
            
            skillDao.updateSkill(skillDto);

        
    }
}
