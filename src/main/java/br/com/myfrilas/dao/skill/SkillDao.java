package br.com.myfrilas.dao.skill;

import java.util.List;

import br.com.myfrilas.dto.skill.SkillDto;
import br.com.myfrilas.model.Skill;

public interface SkillDao {

    void saveSkill(Skill skill);
    List<SkillDto> getSkills();
    void deleteSkill(Long idSkill);
    void updateSkill(SkillDto skillDto);
    boolean checkSkillExists(String skill);
    SkillDto getSkillById(Long idSkill);
}
