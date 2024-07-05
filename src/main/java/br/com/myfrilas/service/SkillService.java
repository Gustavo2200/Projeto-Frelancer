package br.com.myfrilas.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.myfrilas.dao.skill.SkillDao;
import br.com.myfrilas.dto.skill.SkillDto;
import br.com.myfrilas.err.exceptions.FreelasException;
import br.com.myfrilas.model.Skill;

@Service
public class SkillService {
    
    private SkillDao skillDao;

    public SkillService(SkillDao skillDao) {
        this.skillDao = skillDao;
    }

    public void saveSkill(Skill skill) {
        if(skillDao.checkSkillExists(skill.getSkill())){
            throw new FreelasException("Skill ja existe", HttpStatus.CONFLICT.value());
        }
        skillDao.saveSkill(skill);
    }

    public List<SkillDto> getSkills() {
        return skillDao.getSkills();
    }

    public void deleteSkill(Long idSkill) {
        try{
            SkillDto skill = skillDao.getSkillById(idSkill);
            if(skill == null){
                throw new FreelasException("Skill nao existe", HttpStatus.NOT_FOUND.value());
            }
            skillDao.deleteSkill(idSkill);
        }catch(Exception e){
            throw new FreelasException("Erro interno ao deletar a skill", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public void updateSkill(SkillDto skillDto) {

        try{
            SkillDto skill = skillDao.getSkillById(skillDto.getId());
            if(skill == null){
                throw new FreelasException("Skill nao existe", HttpStatus.NOT_FOUND.value());
            }
            skillDao.updateSkill(skillDto);
        }catch(Exception e){
            throw new FreelasException("Erro interno ao atualizar a skill", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        
    }
}
