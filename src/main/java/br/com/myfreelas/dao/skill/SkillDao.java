package br.com.myfreelas.dao.skill;

import java.util.List;

import br.com.myfreelas.dto.skill.SkillDto;
import br.com.myfreelas.model.Skill;

public interface SkillDao {

    void saveSkill(Skill skill);
    List<SkillDto> getSkills();
    void deleteSkill(Long idSkill);
    void updateSkill(SkillDto skillDto);
    boolean checkSkillExists(String skill);
    SkillDto getSkillById(Long idSkill);
}
