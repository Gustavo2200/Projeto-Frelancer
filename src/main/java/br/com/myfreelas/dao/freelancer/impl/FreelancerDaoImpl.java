package br.com.myfreelas.dao.freelancer.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;
import br.com.myfreelas.contantutils.ErrorDatabaseMessageConstants;
import br.com.myfreelas.contantutils.FunctionsName;
import br.com.myfreelas.contantutils.SqlFunctionCall;
import br.com.myfreelas.dao.freelancer.FreelancerDao;
import br.com.myfreelas.dto.proposal.ProposalDtoRequest;
import br.com.myfreelas.err.exceptions.DataBaseException;
import br.com.myfreelas.err.exceptions.FreelasException;

@Slf4j
@Repository
public class FreelancerDaoImpl implements FreelancerDao{

    private final JdbcTemplate jdbcTemplate;

    public FreelancerDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveSkill(List<Long> idSkills, Long idFreelancer) {
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.ADD_SKILL_FREELANCER.getFunctionName());
        
        for(Long idSkill : idSkills) {
            SqlParameterSource namedParameters = new MapSqlParameterSource()
                    .addValue("p_id_freelancer", idFreelancer)
                    .addValue("p_id_skill", idSkill);

            try{
                jdbcCall.execute(namedParameters);
            
            }catch(DataAccessException e){
                log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
                throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
            }catch(Exception e){
                log.error("Erro ao salvar skill", e.getMessage());
                throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_SAVE_SKILL.getMessageUser());
            }
        }
    }

    @Override
    public void deleteSkill(List<Long> idSkills, Long idFreelancer) {
        
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.DELETE_SKILL_FREELANCER.getFunctionName());

        for(Long idSkill : idSkills) {
            SqlParameterSource namedParameters = new MapSqlParameterSource()
                    .addValue("p_id_freelancer", idFreelancer)
                    .addValue("p_id_skill", idSkill);

            try{
                jdbcCall.execute(namedParameters);
            
            }catch(DataAccessException e){
                log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
                throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
     
            }catch(Exception e){
                log.error(ErrorDatabaseMessageConstants.ERROR_DELETE_SKILL.getMessageDev(), e.getMessage());
                throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_DELETE_SKILL.getMessageUser());
            }
        }
    }

    @Override
    public Long idBySkillName(String skill) {
        
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.GET_ID_SKILL_BY_NAME.getFunctionName());
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                        .addValue("p_nm_skill_name", skill);
        
        try{
            Long id = jdbcCall.executeFunction(Long.class, namedParameters);
            return id;
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch(Exception e){
            e.printStackTrace();
            log.error(ErrorDatabaseMessageConstants.ERROR_FIND_SKILL.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_FIND_SKILL.getMessageUser());
        }        
    }

    @Override
    public boolean checkSkillIsAlreadySaved(Long idSkill, Long idFreelancer) {
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.CHECK_FREELANCER_SKILL.getFunctionName());
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("p_id_freelancer", idFreelancer)
                .addValue("p_id_skill", idSkill);

        try{
            Integer count = jdbcCall.executeFunction(Integer.class, namedParameters);
            return count != null && count > 0;
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch(Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_FIND_SKILL.getMessageDev(), namedParameters);
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_FIND_SKILL.getMessageUser());
        }
    }

    @Override
    public void sendProposal(ProposalDtoRequest proposal, Long idFreelancer) {
        
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.SEND_PROPOSAL.getFunctionName());
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id_freelancer", idFreelancer)
                .addValue("id_project", proposal.getIdProject())
                .addValue("comment", proposal.getComment())
                .addValue("value", proposal.getValue());
        try {
            jdbcCall.execute(namedParameters);
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        } catch (Exception e) {
            log.error(ErrorDatabaseMessageConstants.ERROR_SEND_PROPOSAL.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_SEND_PROPOSAL.getMessageUser());
        }
    }
    
    @Override
    public List<String> getSkillsByFreelancerId(Long idFreelancer) {
        try {
            var result = jdbcTemplate.queryForList(SqlFunctionCall.GET_ALL_SKILLS_FREELANCER, idFreelancer);

            List<String> skills = new ArrayList<>();
            for (var r : result) {
                skills.add(r.get("nm_skill_name").toString());
            }
            return skills;

        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        } catch (Exception e) {
            log.error(ErrorDatabaseMessageConstants.ERROR_FIND_SKILL.getMessageDev(), e.getMessage());
            throw new FreelasException(ErrorDatabaseMessageConstants.ERROR_FIND_SKILL.getMessageUser(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    private SimpleJdbcCall createJdbcCall(String functionName) {
        return new SimpleJdbcCall(jdbcTemplate).withFunctionName(functionName);
    } 
}
