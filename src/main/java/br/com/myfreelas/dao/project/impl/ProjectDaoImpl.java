package br.com.myfreelas.dao.project.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import br.com.myfreelas.contantutils.ErrorDatabaseMessageConstants;
import br.com.myfreelas.contantutils.FunctionsName;
import br.com.myfreelas.contantutils.SqlFunctionCall;
import br.com.myfreelas.dao.project.ProjectDao;
import br.com.myfreelas.dto.project.ProjectDtoRequest;
import br.com.myfreelas.dto.project.ProjectDtoResponse;
import br.com.myfreelas.dto.project.UpdateProjectDtoRequest;
import br.com.myfreelas.enums.StatusProject;
import br.com.myfreelas.err.exceptions.DataBaseException;
import br.com.myfreelas.model.Project;

@Slf4j
@Repository
public class ProjectDaoImpl implements ProjectDao {

    private final JdbcTemplate jdbcTemplate;

    public ProjectDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public void saveProject(Long clientId ,ProjectDtoRequest project, Long[] idSkills) {
        SimpleJdbcCall jdbcCall = createJdbcCall("save_project_with_skills");

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("p_title", project.getTitle())
                .addValue("p_description", project.getDescription())
                .addValue("p_client_id", clientId)
                .addValue("p_skill_ids", idSkills);

        try{
            jdbcCall.execute(sqlParameterSource);
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch (Exception e) {
            log.error(ErrorDatabaseMessageConstants.ERROR_SAVE_PROJECT.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_SAVE_PROJECT.getMessageUser());
        }
    }

    @Override
    public List<ProjectDtoResponse> listProjectsByStatus(String status) {
        try {
            var result = jdbcTemplate.queryForList(
                SqlFunctionCall.LIST_PROJECTS_BY_STATUS,
                status.toUpperCase());

            List<ProjectDtoResponse> projects = new ArrayList<>();
            for (var r : result) {
                ProjectDtoResponse project = new ProjectDtoResponse();
                project.setId(((Number) r.get("nr_id_projeto")).longValue());
                project.setTitle((String) r.get("nm_titulo"));
                project.setDescription((String) r.get("nm_descricao"));
                project.setStatus(StatusProject.fromDescription((String) r.get("tp_status")));
                project.setCustomerId(((Number) r.get("fk_nr_id_cliente")).longValue());
                project.setCustomerName((String) r.get("nm_nome"));
                project.setSkills(getSkillsByProjectId(((Number) r.get("nr_id_projeto")).longValue()));
                projects.add(project);
            }
            return projects;
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        } catch (Exception e) {
            log.error(ErrorDatabaseMessageConstants.ERROR_FIND_PROJECTS.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_FIND_PROJECTS.getMessageUser());
        }
    }
    
    @Override
    public void updateProject(UpdateProjectDtoRequest project) {
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.UPDATE_PROJECT.getFunctionName());
       try{

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("p_id_projeto", project.getId())
                .addValue("p_nm_titulo", project.getTitle())
                .addValue("p_nm_descricao", project.getDescription());

        jdbcCall.execute(sqlParameterSource);  
    
    }catch(DataAccessException e){
        log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
        throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());

    }catch(Exception e){
        log.error(ErrorDatabaseMessageConstants.ERROR_UPDATE_PROJECT.getMessageDev(), e.getMessage());
        throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_UPDATE_PROJECT.getMessageUser());
       }
    }

    @Override
        public void deleteProjectById(Long id) {
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.DELETE_PROJECT_AND_SKILLS.getFunctionName());

        try {
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                    .addValue("project_id", id);
            jdbcCall.execute(sqlParameterSource);
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        } catch (Exception e) {
            log.error(ErrorDatabaseMessageConstants.ERROR_DELETE_PROJECT.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_DELETE_PROJECT.getMessageUser());
        }
    }

    @Override
    public Project getProjectById(Long id) {
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.GET_PROJECT_BY_ID.getFunctionName());
    
        try {
            SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("p_id", id);
            var result = jdbcCall.execute(parameterSource);
    
            Project project = new Project();
            project.setId(((Number) result.get("p_nr_id_projeto")).longValue());
            project.setTitle(result.get("p_nm_titulo").toString());
            project.setDescription(result.get("p_nm_descricao").toString());
            project.setStatus(StatusProject.fromDescription(result.get("p_tp_status").toString()));
    
            BigDecimal price = (BigDecimal) result.get("p_vl_preco");
    
            if (price != null) {
                project.setPrice(price);
            } else {
                project.setPrice(null);
            }
    
            project.setClientId(((Number) result.get("p_fk_nr_id_cliente")).longValue());
            if(result.get("p_fk_nr_id_freelancer") != null){
                project.setFreelancerId(((Number) result.get("p_fk_nr_id_freelancer")).longValue());
            }
            else{
                project.setFreelancerId(null);
            }
            project.setSkills(getSkillsByProjectId(id));
    
            return project;
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        } catch (Exception e) {
            log.error(ErrorDatabaseMessageConstants.ERROR_FIND_PROJECT.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_FIND_PROJECT.getMessageUser());
        }
    }

    @Override
    public List<Project> listProjectsByUserId(Long idUser) {
        try{
            var result = jdbcTemplate.queryForList(SqlFunctionCall.LIST_PROJECTS_BY_USER, idUser);
            
            List<Project> projects = new ArrayList<>();
            for(var r: result){
                Project project = new Project();
                project.setId(((Number) r.get("p_NR_ID_PROJETO")).longValue());
                project.setTitle((String)r.get("p_NM_TITULO"));
                project.setDescription((String)r.get("p_NM_DESCRICAO"));
                project.setStatus(StatusProject.fromDescription((String)r.get("p_TP_STATUS")));

                BigDecimal price = (BigDecimal) r.get("p_vl_preco");
        
                if (price != null) {
                    project.setPrice(price);
                } else {
                    project.setPrice(null);
                }
                project.setClientId(((Number) r.get("p_FK_NR_ID_CLIENTE")).longValue());
                if(r.get("p_fk_nr_id_freelancer") != null){
                    project.setFreelancerId(((Number) r.get("p_fk_nr_id_freelancer")).longValue());
                }
                else{
                    project.setFreelancerId(null);
                }
                project.setSkills(getSkillsByProjectId(((Number) r.get("p_NR_ID_PROJETO")).longValue()));
                projects.add(project);
            }
            return projects;

        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch(Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_FIND_PROJECTS.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_FIND_PROJECTS.getMessageUser());
        }
    }

    @Override
    public List<Project> listProjectsByUserIdAndStatus(Long idUser, String status) {
        try{
            var result = jdbcTemplate.queryForList(SqlFunctionCall.LIST_PROJECTS_BY_USER_AND_STATUS, idUser, status.toUpperCase());
            List<Project> projects = new ArrayList<>();
            for(var r: result){
                Project project = new Project();
                project.setId(((Number) r.get("p_NR_ID_PROJETO")).longValue());
                project.setTitle((String)r.get("p_NM_TITULO"));
                project.setDescription((String)r.get("p_NM_DESCRICAO"));
                project.setStatus(StatusProject.fromDescription((String)r.get("p_TP_STATUS")));

                BigDecimal price = (BigDecimal) r.get("p_vl_preco");
        
                if (price != null) {
                    project.setPrice(price);
                } else {
                    project.setPrice(BigDecimal.ZERO);
                }

                project.setClientId(((Number) r.get("p_FK_NR_ID_CLIENTE")).longValue());
                
                if(r.get("p_FK_NR_ID_FREELANCER") != null){
                    project.setFreelancerId(((Number) r.get("p_FK_NR_ID_FREELANCER")).longValue());
                }
                else{
                    project.setFreelancerId(null);
                }
                project.setSkills(getSkillsByProjectId(((Number) r.get("p_NR_ID_PROJETO")).longValue()));
                projects.add(project);
            }
            return projects;

        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch(Exception e){
            e.printStackTrace();
            log.error(ErrorDatabaseMessageConstants.ERROR_FIND_PROJECTS.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_FIND_PROJECTS.getMessageUser());
        }
    }

    @Override
    public boolean checkProjectExists(Long id) {
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.COUNT_PROJECT_BY_ID.getFunctionName());
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("p_id", id);
        try{
            Integer count = jdbcCall.executeFunction(Integer.class, sqlParameterSource);
            return count != null && count > 0;
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch(Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_FIND_PROJECT.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_FIND_PROJECT.getMessageUser());
        }
    }
    @Override
    public Long customerIdByProjectId(Long id) {
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.CUSTOMER_ID_BY_USER_ID.getFunctionName());
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("p_id", id);
        try{
            Long idCustomer = jdbcCall.executeFunction(Long.class, sqlParameterSource);
            return idCustomer;
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch(Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_FIND_CUSTOMER.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_FIND_CUSTOMER.getMessageUser());
        }
    }
    @Override
    public void addSkillNecessary(Long[] skillIds, Long projectId) {
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.ADD_SKILL_NECESARY.getFunctionName());
        try{
            for (int i = 0; i < skillIds.length; i++) {
                SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                        .addValue("p_id_projeto", projectId)
                        .addValue("p_id_skill", skillIds[i]);

                jdbcCall.execute(sqlParameterSource);
            }

        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch(Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_ADD_SKILL_DEPENDENCY.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_ADD_SKILL_DEPENDENCY.getMessageUser());
        }
    }

    @Override
    public void removeSkillNecessary(Long[] skillIds, Long projectId) {
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.REMOVE_SKILL_NECESARY.getFunctionName());
        try{
            for (int i = 0; i < skillIds.length; i++) {
                SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                        .addValue("p_id_project", projectId)
                        .addValue("p_id_skill", skillIds[i]);
                        
                jdbcCall.execute(sqlParameterSource);
            }

        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch(Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_REMOVE_SKILL_DEPENDENCY.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_REMOVE_SKILL_DEPENDENCY.getMessageUser());
        }
    }
    
    @Override
    public List<String> getSkillsByProjectId(Long projectId) {
        try {
            var result = jdbcTemplate.queryForList(SqlFunctionCall.GET_SKILLS_BY_PROJECT_ID, projectId);
            List<String> skills = new ArrayList<>();
            
            for(var r: result){
                skills.add((String)r.get("NM_SKILL_NAME"));
            }
            return skills;

        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        } catch (Exception e) {
            log.error(ErrorDatabaseMessageConstants.ERROR_FIND_SKILL_PROJECT.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_FIND_SKILL_PROJECT.getMessageUser());
        }
    }


    @Override
    public StatusProject checkStatusProject(Long idProject) {
        
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.GET_STATUS_FROM_PROJECT.getFunctionName());
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("p_id_projeto", idProject);
        
        try{    
            String status = jdbcCall.executeFunction(String.class, sqlParameterSource);
            return StatusProject.fromDescription(status);
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch(Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_FIND_STATUS_PROJECT.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_FIND_STATUS_PROJECT.getMessageUser());
        }
    }

    @Override
    public BigDecimal priceByProjectId(Long id) {
        
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.GET_PRICE_FROM_PROJECT.getFunctionName());
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("p_id_projeto", id);
       
        try{
            BigDecimal price = jdbcCall.executeFunction(BigDecimal.class, sqlParameterSource);
            return price;
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch(Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_FIND_PRICE_PROJECT.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_FIND_PRICE_PROJECT.getMessageUser());
        }
    }

    private SimpleJdbcCall createJdbcCall(String functionName) {
        return new SimpleJdbcCall(jdbcTemplate).withFunctionName(functionName);
    }
}