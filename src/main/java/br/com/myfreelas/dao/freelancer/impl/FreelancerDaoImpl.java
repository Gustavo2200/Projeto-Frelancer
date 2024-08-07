package br.com.myfreelas.dao.freelancer.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;

import br.com.myfreelas.dao.freelancer.FreelancerDao;
import br.com.myfreelas.dto.proposal.ProposalDtoRequest;
import br.com.myfreelas.err.exceptions.FreelasException;

@Slf4j
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

        try{
            namedParameterJdbcTemplate.update(query, namedParameters);
        }catch(Exception e){
            log.error("Erro ao salvar skill", e.getMessage());
            throw new FreelasException("Erro interno ao salvar skill", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        }
    }

    @Override
    public void deleteSkill(List<Long> idSkills, Long idFreelancer) {
        String query = "DELETE FROM TB_FREELANCER_SKILLS WHERE FK_ID_FREELANCER = :idFreelancer AND FK_ID_SKILL = :idSkill";
        
        for(Long idSkill : idSkills) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("idFreelancer", idFreelancer)
                .addValue("idSkill", idSkill);

        try{
            namedParameterJdbcTemplate.update(query, namedParameters);
        }catch(Exception e){
            log.error("Erro ao deletar skill", e.getMessage());
            throw new FreelasException("Erro interno ao deletar skill", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        }
    }

    @Override
    public Long idBySkillName(String skill) {
        try{
            String querry = "SELECT NR_ID_SKILL FROM TB_SKILL WHERE NM_SKILL_NAME = :skill";
            SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("skill", skill);
        
            return namedParameterJdbcTemplate.queryForObject(querry, namedParameters, Long.class);
        }catch(Exception e){
            log.error("Erro ao buscar skill", e.getMessage());
            throw new FreelasException("Skill '" + skill + "' não existe", HttpStatus.NOT_FOUND.value());
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
        String query = "SELECT send_proposal(:idFreelancer, :idProject, :comment, :value)";

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("idFreelancer", idFreelancer)
                .addValue("idProject", proposal.getIdProject())
                .addValue("comment", proposal.getComment())
                .addValue("value", proposal.getValue());
        try {
            namedParameterJdbcTemplate.execute(query, namedParameters, (PreparedStatementCallback<Object>) ps ->{
            ps.execute();
            return null;
           });
        } catch (Exception e) {
            log.error("Erro ao enviar proposta", e.getMessage());
            throw new FreelasException("Erro interno ao enviar proposta", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    
    @Override
    public List<String> getSkillsByFreelancerId(Long idFreelancer) {
        String query = "SELECT * FROM get_skills_by_freelancer_id(:idFreelancer)";

        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("idFreelancer", idFreelancer);
        try {
            var result = namedParameterJdbcTemplate.queryForList(query, namedParameters);

            List<String> skills = new ArrayList<>();
            for (var r : result) {
                skills.add(r.get("nm_skill_name").toString());
            }
            return skills;
        } catch (Exception e) {
            log.error("Erro ao buscar skills", e.getMessage());
            throw new FreelasException("Erro interno ao buscar skills", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
