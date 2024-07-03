package br.com.myfrilas.dao.freelancer.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import br.com.myfrilas.dao.freelancer.FreelancerDao;
import br.com.myfrilas.dto.proposal.ProposalDtoRequest;
import br.com.myfrilas.err.exceptions.FreelasException;

@Repository
public class FreelancerDaoImpl implements FreelancerDao{

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public FreelancerDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void saveSkill(List<Long> idSkills, Long idFreelancer) {
        String query = "INSERT INTO TB_FREELANCER_SKILLS (FK_ID_FREELANCER, FK_ID_SKILL) VALUES (:idFreelancer, :idSkill)";
        
        for(Long idSkill : idSkills) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("idFreelancer", idFreelancer)
                .addValue("idSkill", idSkill);
                
        namedParameterJdbcTemplate.update(query, namedParameters);
        }
    }

    @Override
    public void deleteSkill(List<Long> idSkills, Long idFreelancer) {
        String query = "DELETE FROM TB_FREELANCER_SKILLS WHERE FK_ID_FREELANCER = :idFreelancer AND FK_ID_SKILL = :idSkill";
        
        for(Long idSkill : idSkills) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("idFreelancer", idFreelancer)
                .addValue("idSkill", idSkill);

        namedParameterJdbcTemplate.update(query, namedParameters);
        }
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
        String query = "SELECT COUNT(1) FROM TB_FREELANCER_SKILLS WHERE FK_ID_FREELANCER = :idFreelancer AND FK_ID_SKILL = :idSkill";
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

    @Override
    public void sendProposal(ProposalDtoRequest proposal, Long idFreelancer) {
        String query = "INSERT INTO TB_PROPOSTA (FK_ID_FREELANCER, FK_ID_PROJETO, NM_COMENTARIO, VL_VALOR) " +
                "VALUES (:idFreelancer, :idProject, :comment, :value)";

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("idFreelancer", idFreelancer)
                .addValue("idProject", proposal.getIdProject())
                .addValue("comment", proposal.getComment())
                .addValue("value", proposal.getValue());

        namedParameterJdbcTemplate.update(query, namedParameters);
    }
    
    @Override
    public List<String> getSkillsByFreelancerId(Long idFreelancer){
        String query = "SELECT nm_skill_name FROM TB_SKILL s " +
                       "JOIN TB_FREELANCER_SKILLS psk ON s.nr_id_skill = psk.fk_id_skill " +
                       "WHERE psk.fk_id_freelancer = :idFreelancer";

        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("idFreelancer", idFreelancer);

        var result = namedParameterJdbcTemplate.queryForList(query, namedParameters);

        List<String> skills = new ArrayList<>();
        for(var r : result){
            skills.add(r.get("nm_skill_name").toString());
        }
        return skills;
     }

}
