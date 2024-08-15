package br.com.myfreelas.dao.skill.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import br.com.myfreelas.contantutils.ErrorDatabaseMessageConstants;
import br.com.myfreelas.contantutils.FunctionsName;
import br.com.myfreelas.contantutils.SqlFunctionCall;
import br.com.myfreelas.dao.skill.SkillDao;
import br.com.myfreelas.dto.skill.SkillDto;
import br.com.myfreelas.err.exceptions.DataBaseException;
import br.com.myfreelas.model.Skill;

@Slf4j
@Repository
public class SkillDaoImpl implements SkillDao {

   private JdbcTemplate jdbcTemplate;

    public SkillDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveSkill(Skill skill) {
        
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.SAVE_SKILL.getFunctionName());
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("skill_name", skill.getSkill());
        
        try{
            jdbcCall.execute(sqlParameterSource);
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch(Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_SAVE_SKILL.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_SAVE_SKILL.getMessageUser());
        }
    }

    @Override
    public List<SkillDto> getSkills() {
        try{
            var result = jdbcTemplate.queryForList(SqlFunctionCall.GET_ALL_SKILLS);
            List<SkillDto> skills = new ArrayList<>();

            for(var r : result){
                SkillDto skillDto = new SkillDto();
                skillDto.setId(((Number) r.get("p_nr_id_skill")).longValue());
                skillDto.setSkill((String) r.get("p_nm_skill_name"));
                skills.add(skillDto);
            }
            return skills;

        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch(Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_FIND_SKILL.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_FIND_SKILL.getMessageUser());
        }
    }

    @Override
    public void deleteSkill(Long idSkill) {
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.DELETE_SKILL_BY_ID.getFunctionName());

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                    .addValue("p_id_skill", idSkill);
        try{
            jdbcCall.execute(sqlParameterSource);
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch(Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_DELETE_SKILL.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_DELETE_SKILL.getMessageUser());
        }
    }

    @Override
    public void updateSkill(SkillDto skilldto) {
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.UPDATE_SKILL.getFunctionName());
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("skill", skilldto.getSkill())
                .addValue("p_id_skill", skilldto.getId());
        try{
            jdbcCall.execute(namedParameters);
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch(Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_UPDATE_SKILL.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_UPDATE_SKILL.getMessageUser());
        }
    }

    @Override
    public boolean checkSkillExists(String skill) {
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.CHECK_SKILL_EXISTS.getFunctionName());
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                    .addValue("skill", skill);
        try{
            Integer count = jdbcCall.executeFunction(Integer.class, namedParameters);
            return count > 0;
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch(Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_VERIFY_SKILL_EXISTS.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_VERIFY_SKILL_EXISTS.getMessageUser());
        }
    }

    @Override
    public SkillDto getSkillById(Long idSkill) {
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.GET_SKILL_BY_ID.getFunctionName());
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                    .addValue("id_skill", idSkill);
        try{
            var result = jdbcCall.execute(sqlParameterSource);
            SkillDto skill = new SkillDto();

            skill.setId(((Number) result.get("p_nr_id_skill")).longValue());
            skill.setSkill((String) result.get("p_nm_skill_name"));
            return skill;
        }catch(DataIntegrityViolationException e){
            return null;
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch(Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_FIND_SKILL.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_FIND_SKILL.getMessageUser());
        }
    }

    private SimpleJdbcCall createJdbcCall(String functionName) {
        return new SimpleJdbcCall(jdbcTemplate).withFunctionName(functionName);
    } 
    
}
