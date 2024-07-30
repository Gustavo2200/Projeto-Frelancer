package br.com.myfreelas.dao.project.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import br.com.myfreelas.dao.project.ProjectDao;
import br.com.myfreelas.dto.project.ProjectDtoRequest;
import br.com.myfreelas.dto.project.ProjectDtoResponse;
import br.com.myfreelas.dto.project.UpdateProjectDtoRequest;
import br.com.myfreelas.enums.StatusProject;
import br.com.myfreelas.err.exceptions.FreelasException;
import br.com.myfreelas.model.Project;

@Repository
public class ProjectDaoImpl implements ProjectDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ProjectDaoImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
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
        }catch (Exception e) {
            e.printStackTrace();
            throw new FreelasException("Erro interno ao salvar o projeto", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public List<ProjectDtoResponse> listProjectsByStatus(String status) {
        String query = "SELECT * FROM list_projects_by_status(:status)";
    
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("status", status.toUpperCase());
        try {
            var result = namedParameterJdbcTemplate.queryForList(query, namedParameters);
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
        } catch (Exception e) {
            e.printStackTrace();
            throw new FreelasException("Erro interno ao buscar projetos", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    

    @Override
    public void updateProject(UpdateProjectDtoRequest project) {
       String query = "UPDATE TB_PROJETO SET NM_TITULO = ?, NM_DESCRICAO = ? WHERE NR_ID_PROJETO = ?";

       try{
        jdbcTemplate.update(query, project.getTitle(), project.getDescription(), project.getId());
       
    }catch(Exception e){
        e.printStackTrace();
        throw new FreelasException("Erro interno ao atualizar o projeto", HttpStatus.INTERNAL_SERVER_ERROR.value());
       }
    }

    @Override
        public void deleteProjectById(Long id) {
        String query = "SELECT delete_project_and_skills(:id)";

        try {
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("id", id);
            namedParameterJdbcTemplate.execute(query, sqlParameterSource, (PreparedStatementCallback<Object>) ps -> {
                ps.execute();
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new FreelasException("Erro interno ao deletar o projeto", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public Project getProjectById(Long id) {
        String query = "SELECT * FROM TB_PROJETO WHERE NR_ID_PROJETO = :id";
    
        try {
            SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("id", id);
            var result = namedParameterJdbcTemplate.queryForMap(query, parameterSource);
    
            Project project = new Project();
            project.setId(((Number) result.get("nr_id_projeto")).longValue());
            project.setTitle(result.get("nm_titulo").toString());
            project.setDescription(result.get("nm_descricao").toString());
            project.setStatus(StatusProject.fromDescription(result.get("tp_status").toString()));
    
            BigDecimal price = (BigDecimal) result.get("vl_preco");
    
            if (price != null) {
                project.setPrice(price);
            } else {
                project.setPrice(BigDecimal.ZERO);
            }
    
            project.setClientId(((Number) result.get("fk_nr_id_cliente")).longValue());
            if(result.get("fk_nr_id_freelancer") != null){
                project.setFreelancerId(((Number) result.get("fk_nr_id_freelancer")).longValue());
            }
            else{
                project.setFreelancerId(null);
            }
            project.setSkills(getSkillsByProjectId(id));
    
            return project;
    
        } catch (Exception e) {
            e.printStackTrace();
            throw new FreelasException("Erro interno ao buscar projeto", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public List<Project> listProjectsByUserId(Long idUser) {
        String query = "SELECT * FROM TB_PROJETO WHERE FK_NR_ID_CLIENTE = :id OR FK_NR_ID_FREELANCER = :id "+
                "ORDER BY NR_ID_PROJETO";
        
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", idUser);

        try{
            var result = namedParameterJdbcTemplate.queryForList(query, parameterSource);
            
            List<Project> projects = new ArrayList<>();
            for(var r: result){
                Project project = new Project();
                project.setId(((Number) r.get("NR_ID_PROJETO")).longValue());
                project.setTitle((String)r.get("NM_TITULO"));
                project.setDescription((String)r.get("NM_DESCRICAO"));
                project.setStatus(StatusProject.fromDescription((String)r.get("TP_STATUS")));

                BigDecimal price = (BigDecimal) r.get("vl_preco");
        
                if (price != null) {
                    project.setPrice(price);
                } else {
                    project.setPrice(BigDecimal.ZERO);
                }
                project.setClientId(((Number) r.get("FK_NR_ID_CLIENTE")).longValue());
                project.setFreelancerId(((Number) r.get("FK_NR_ID_FREELANCER")).longValue());
                project.setSkills(getSkillsByProjectId(((Number) r.get("NR_ID_PROJETO")).longValue()));
                projects.add(project);
            }
            return projects;
        }catch(Exception e){
            e.printStackTrace();
            throw new FreelasException("Erro interno ao buscar projetos", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public List<Project> listProjectsByUserIdAndStatus(Long idUser, String status) {

        String query = "SELECT * FROM TB_PROJETO WHERE (FK_NR_ID_CLIENTE = :id OR FK_NR_ID_FREELANCER = :id) "+
                "AND TP_STATUS = :status ORDER BY NR_ID_PROJETO"; 
        
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", idUser)
                .addValue("status", status.toUpperCase());
        try{
            var result = namedParameterJdbcTemplate.queryForList(query, parameterSource);
            List<Project> projects = new ArrayList<>();
            for(var r: result){
                Project project = new Project();
                project.setId(((Number) r.get("NR_ID_PROJETO")).longValue());
                project.setTitle((String)r.get("NM_TITULO"));
                project.setDescription((String)r.get("NM_DESCRICAO"));
                project.setStatus(StatusProject.fromDescription((String)r.get("TP_STATUS")));

                BigDecimal price = (BigDecimal) r.get("vl_preco");
        
                if (price != null) {
                    project.setPrice(price);
                } else {
                    project.setPrice(BigDecimal.ZERO);
                }

                project.setClientId(((Number) r.get("FK_NR_ID_CLIENTE")).longValue());
                
                if(r.get("FK_NR_ID_FREELANCER") != null){
                    project.setFreelancerId(((Number) r.get("FK_NR_ID_FREELANCER")).longValue());
                }
                else{
                    project.setFreelancerId(null);
                }
                project.setSkills(getSkillsByProjectId(((Number) r.get("NR_ID_PROJETO")).longValue()));
                projects.add(project);
            }
            return projects;
        }catch(Exception e){
            e.printStackTrace();
            throw new FreelasException("Erro interno ao buscar projetos", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public boolean checkProjectExists(Long id) {
        String query = "SELECT COUNT(1) FROM TB_PROJETO WHERE NR_ID_PROJETO = :id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("id", id);
        try{
            Integer count = namedParameterJdbcTemplate.queryForObject(query, sqlParameterSource, Integer.class);
            return count != null && count > 0;
        }catch(Exception e){
            e.printStackTrace();
            throw new FreelasException("Erro interno ao buscar projeto", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    @Override
    public Long customerIdByProjectId(Long id) {
        String query = "SELECT FK_NR_ID_CLIENTE FROM TB_PROJETO WHERE NR_ID_PROJETO = :id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("id", id);
        try{
            Long idCustomer = namedParameterJdbcTemplate.queryForObject(query, sqlParameterSource, Long.class);
            return idCustomer;
        }catch(Exception e){
            e.printStackTrace();
            throw new FreelasException("Erro interno ao buscar  cliente", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    @Override
    public void addSkillNecessary(Long[] skillIds, Long projectId) {
        String query = "INSERT INTO TB_SKILLS_NECESSARIAS_PROJETO (FK_ID_PROJETO, FK_ID_SKILL) " +
                 "VALUES (:idProject, :idSkill)";

        try{
            for (int i = 0; i < skillIds.length; i++) {
                SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                        .addValue("idProject", projectId)
                        .addValue("idSkill", skillIds[i]);

                namedParameterJdbcTemplate.update(query, sqlParameterSource);
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new FreelasException("Erro interno ao adicionar dependencias no projeto", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public void removeSkillNecessary(Long[] skillIds, Long projectId) {
        String query = "DELETE FROM TB_SKILLS_NECESSARIAS_PROJETO WHERE FK_ID_SKILL = :idSkill " +
                 "AND FK_ID_PROJETO = :idProject";

        try{
            for (int i = 0; i < skillIds.length; i++) {
                SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                        .addValue("idSkill", skillIds[i])
                        .addValue("idProject", projectId);
                        
                namedParameterJdbcTemplate.update(query, sqlParameterSource);
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new FreelasException("Erro interno ao remover dependencias no projeto", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    
    @Override
    public List<String> getSkillsByProjectId(Long projectId) {
        String query = "SELECT * FROM get_skills_by_project_id(:idProject)";
        
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("idProject", projectId);
        try {
            List<String> skills = namedParameterJdbcTemplate.query(query, namedParameters, (rs, rowNum) -> rs.getString("nm_skill_name"));
            return skills;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FreelasException("Erro interno ao buscar dependências do projeto", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }


    @Override
    public StatusProject checkStatusProject(Long idProject) {

        String query = "SELECT TP_STATUS FROM TB_PROJETO WHERE NR_ID_PROJETO = :idProject";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("idProject", idProject);
        try{
            String status = namedParameterJdbcTemplate.queryForObject(query, sqlParameterSource, String.class);
            return StatusProject.fromDescription(status);
        }catch(Exception e){
            e.printStackTrace();
            throw new FreelasException("Erro interno ao buscar status do projeto", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public BigDecimal priceByProjectId(Long id) {
        String query = "SELECT vl_preco FROM TB_PROJETO WHERE NR_ID_PROJETO = :id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("id", id);
        try{
            BigDecimal price = namedParameterJdbcTemplate.queryForObject(query, sqlParameterSource, BigDecimal.class);
        
            if(price == null){
                throw new FreelasException("Projeto nao encontrado", HttpStatus.NOT_FOUND.value());
            }
            return price;
        }catch(Exception e){
            e.printStackTrace();
            throw new FreelasException("Erro interno ao buscar preço do projeto", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    private SimpleJdbcCall createJdbcCall(String functionName) {
        return new SimpleJdbcCall(jdbcTemplate).withFunctionName(functionName);
    }
}