package br.com.myfrilas.dao.freelancer.impl;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import br.com.myfrilas.dao.freelancer.FreelancerDao;
import br.com.myfrilas.err.exceptions.FreelasException;

@Repository
public class FreelancerDaoImpl implements FreelancerDao{

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public FreelancerDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void saveSkill(Long idSkill, Long idFreelancer) {
        String query = "INSERT INTO TB_FREELANCER_SKILLS (FK_ID_FREELANCER, FK_ID_SKILL) VALUES (:idFreelancer, :idSkill)";
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("idFreelancer", idFreelancer)
                .addValue("idSkill", idSkill);
        namedParameterJdbcTemplate.update(query, namedParameters);
        
    }

    @Override
    public Long idBySkillName(String skill) {
        try{
            String querry = "SELECT NR_ID_SKILL FROM TB_SKILL WHERE NM_SKILL_NAME = :skill";
            SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("skill", skill);
        
            return namedParameterJdbcTemplate.queryForObject(querry, namedParameters, Long.class);
        }catch(Exception e){
            e.printStackTrace();
            throw new FreelasException("Skill inexistente", HttpStatus.NOT_FOUND.value());
        }        
    }

    @Override
    public boolean checkSkillIsAlreadySaved(Long idSkill, Long idFreelancer) {
        String query = "SELECT 1 FROM TB_FREELANCER_SKILLS WHERE FK_ID_FREELANCER = :idFreelancer AND FK_ID_SKILL = :idSkill";
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("idFreelancer", idFreelancer)
                .addValue("idSkill", idSkill);
        Integer count = namedParameterJdbcTemplate.queryForObject(query, namedParameters, Integer.class);

        return count != null && count > 0;
    }

    @Override
    public boolean checkSkillExists(String skill) {
        String querry = "SELECT COUNT(1) FROM TB_SKILL WHERE NM_SKILL_NAME = :skill";
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("skill", skill);
        Integer count = namedParameterJdbcTemplate.queryForObject(querry, namedParameters, Integer.class);
        return count != null && count > 0;
    }
    
}
