package br.com.myfrilas.dao.skill.impl;

import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import br.com.myfrilas.dao.skill.SkillDao;
import br.com.myfrilas.dto.skill.SkillDto;
import br.com.myfrilas.model.Skill;

@Repository
public class SkillDaoImpl implements SkillDao {

   private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public SkillDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void saveSkill(Skill skill) {
        String querry = "INSERT INTO TB_SKILL(NM_SKILL_NAME) VALUES(:skill)";

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("skill", skill.getSkill());


        try{
            namedParameterJdbcTemplate.update(querry, namedParameters);
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("Erro interno ao salvar a skill");
        }
    }

    @Override
    public List<SkillDto> getSkills() {
        String querry = "SELECT * FROM TB_SKILL";
        try{
            List<SkillDto> skills = namedParameterJdbcTemplate.query(querry, (rs, rowNum) -> {//funcao lambda
                SkillDto skill = new SkillDto();                                              //Ela define como cada linha do resultado da consulta SQL será mapeada para um objeto SkillDto.
                skill.setId(rs.getLong("NR_ID_SKILL"));                           // rs recebe o resultado da consulta SQL, e rowNum o numero de linhas retornadas.
                skill.setSkill(rs.getString("NM_SKILL_NAME"));                    // para cada linha um novo objeto e criado e populado e ao final das linas o metodo query                   
                return skill;                                                                 //coleta todos como uma lista e atribui a skills obtendo assim a lista completa.
            });
        
            return skills;
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("Erro interno ao buscar as skills");
        }
    }

    @Override
    public void deleteSkill(Long idSkill) {
        String querry = "DELETE FROM TB_SKILL WHERE NR_ID_SKILL = :idSkill";

        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("idSkill", idSkill);
        try{
            namedParameterJdbcTemplate.update(querry, namedParameters);
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("Erro interno ao deletar a skill");
        }
    }

    @Override
    public void updateSkill(SkillDto skilldto) {
        String querry = "UPDATE TB_SKILL SET NM_SKILL_NAME = :skill WHERE NR_ID_SKILL = :idSkill";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("skill", skilldto.getSkill())
                .addValue("idSkill", skilldto.getId());
        try{
            namedParameterJdbcTemplate.update(querry, namedParameters);
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("Erro interno ao atualizar a skill");
        }
    }

    @Override
    public boolean checkSkillExists(String skill) {
        String querry = "SELECT COUNT(1) FROM TB_SKILL WHERE NM_SKILL_NAME = :skill";

        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("skill", skill);
        Integer count = namedParameterJdbcTemplate.queryForObject(querry, namedParameters, Integer.class);

        if (count == null) {
            return false;
        }
        return count > 0;
    }

    @Override
    public SkillDto getSkillById(Long idSkill) {
        String querry = "SELECT * FROM TB_SKILL WHERE NR_ID_SKILL = :idSkill";
        try{
            SkillDto skill = namedParameterJdbcTemplate.queryForObject(querry, new MapSqlParameterSource().addValue("idSkill", idSkill), (rs, rowNum) -> {
                SkillDto skillDto = new SkillDto();
                skillDto.setId(rs.getLong("NR_ID_SKILL"));
                skillDto.setSkill(rs.getString("NM_SKILL_NAME"));
                return skillDto;
            });
            return skill;
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("Erro interno ao buscar a skill");
        }
    }
    
}
