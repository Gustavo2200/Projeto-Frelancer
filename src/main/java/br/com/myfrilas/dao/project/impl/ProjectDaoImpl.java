package br.com.myfrilas.dao.project.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import br.com.myfrilas.dao.project.ProjectDao;
import br.com.myfrilas.dao.user.UserDao;
import br.com.myfrilas.dto.project.ProjectDtoRequest;
import br.com.myfrilas.dto.project.ProjectDtoResponse;
import br.com.myfrilas.dto.project.UpdateProjectDtoRequest;
import br.com.myfrilas.dto.user.UserDto;
import br.com.myfrilas.enums.StatusProject;
import br.com.myfrilas.model.Project;

@Repository
public class ProjectDaoImpl implements ProjectDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ProjectDaoImpl(JdbcTemplate jdbcTemplate, UserDao userDao , NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
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
            throw new RuntimeException("Erro interno ao salvar o projeto");
        }
    }

    @Override
    public List<ProjectDtoResponse> listProjectsByStatus(String status) {
        String query = "SELECT NR_ID_PROJETO, NM_TITULO, NM_DESCRICAO, TP_STATUS, TB_USUARIO.NM_NOME FROM TB_PROJETO \r\n" + //
                        "JOIN TB_USUARIO ON FK_NR_ID_CLIENTE = NR_ID_USUARIO WHERE TP_STATUS = ?"; 
        
        var result = jdbcTemplate.queryForList(query, status);
        List<ProjectDtoResponse> projects = new ArrayList<>();
        for(var r: result){
            ProjectDtoResponse project = new ProjectDtoResponse();
            project.setId(((Number)r.get("NR_ID_PROJETO")).longValue());
            project.setTitle((String)r.get("NM_TITULO"));
            project.setDescription((String)r.get("NM_DESCRICAO"));
            project.setStatus(StatusProject.fromDescription((String)r.get("TP_STATUS")));
            project.setCustomerName((String)r.get("NM_NOME"));
            project.setSkills(getSkillsByProjectId(((Number) r.get("NR_ID_PROJETO")).longValue()));
            projects.add(project);
        }
        return projects;
    }

    @Override
    public void updateProject(UpdateProjectDtoRequest project) {
       String query = "UPDATE TB_PROJETO SET NM_TITULO = ?, NM_DESCRICAO = ? WHERE NR_ID_PROJETO = ?";

       try{
        jdbcTemplate.update(query, project.getTitle(), project.getDescription(), project.getId());
       
    }catch(Exception e){
        e.printStackTrace();
        throw new RuntimeException("Erro interno ao atualizar o projeto");
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
            throw new RuntimeException("Erro interno ao deletar o projeto");
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
    
            Number customerId = (Number) result.get("fk_nr_id_cliente");
            if (customerId != null) {
                var customer = userDao.userById(customerId.longValue());
                project.setClient(new UserDto(customer.getName(), customer.getEmail()));
            }
    
            Number freelancerId = (Number) result.get("fk_nr_id_freelancer");
            if (freelancerId != null) {
                var freelancer = userDao.userById(freelancerId.longValue());
                project.setFreelancer(new UserDto(freelancer.getName(), freelancer.getEmail()));
            } else {
                project.setFreelancer(null);
            }

            project.setSkills(getSkillsByProjectId(id));
    
            return project;
    
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro interno ao buscar projeto");
        }
    }

    @Override
    public List<Project> listProjectsByCustomerId(Long id) {
        String query = "SELECT * FROM TB_PROJETO WHERE FK_NR_ID_CLIENTE = :id";
        
        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("id", id);

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

            Number customerId = (Number) r.get("fk_nr_id_cliente");
            if (customerId != null) {
                var customer = userDao.userById(customerId.longValue());
                project.setClient(new UserDto(customer.getName(), customer.getEmail()));
            }
    
            Number freelancerId = (Number) r.get("fk_nr_id_freelancer");
            if (freelancerId != null) {
                var freelancer = userDao.userById(freelancerId.longValue());
                project.setFreelancer(new UserDto(freelancer.getName(), freelancer.getEmail()));
            } else {
                project.setFreelancer(null);
            }
            project.setSkills(getSkillsByProjectId(((Number) r.get("NR_ID_PROJETO")).longValue()));
            projects.add(project);
        }
        return projects;
    }

    @Override
    public List<Project> listProjectsByFreelancerId(Long id) {

        String query = "SELECT * FROM TB_PROJETO WHERE FK_NR_ID_FREELANCER = :id"; 
        
        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("id", id);
        
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

            Number customerId = (Number) r.get("fk_nr_id_cliente");
            if (customerId != null) {
                var customer = userDao.userById(customerId.longValue());
                project.setClient(new UserDto(customer.getName(), customer.getEmail()));
            }
    
            Number freelancerId = (Number) r.get("fk_nr_id_freelancer");
            if (freelancerId != null) {
                var freelancer = userDao.userById(freelancerId.longValue());
                project.setFreelancer(new UserDto(freelancer.getName(), freelancer.getEmail()));
            } 

            project.setSkills(getSkillsByProjectId(((Number) r.get("NR_ID_PROJETO")).longValue()));
            projects.add(project);
        }
        return projects;
    }

    @Override
    public boolean checkProjectExists(Long id) {
        String query = "SELECT COUNT(1) FROM TB_PROJETO WHERE NR_ID_PROJETO = :id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("id", id);
        Integer count = namedParameterJdbcTemplate.queryForObject(query, sqlParameterSource, Integer.class);
        return count != null && count > 0;
    }
    @Override
    public Long customerIdByProjectId(Long id) {
        String query = "SELECT FK_NR_ID_CLIENTE FROM TB_PROJETO WHERE NR_ID_PROJETO = :id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("id", id);
        Long idCustomer = namedParameterJdbcTemplate.queryForObject(query, sqlParameterSource, Long.class);
        return idCustomer;
    }
    @Override
    public void addSkillNecessary(Long[] skillIds, Long projectId) {
        String query = "INSERT INTO TB_SKILLS_NECESSARIAS_PROJETO (FK_ID_PROJETO, FK_ID_SKILL) " +
                 "VALUES (:idProject, :idSkill)";

        for (int i = 0; i < skillIds.length; i++) {
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                    .addValue("idProject", projectId)
                    .addValue("idSkill", skillIds[i]);

            namedParameterJdbcTemplate.update(query, sqlParameterSource);
        }
    }

    @Override
    public void removeSkillNecessary(Long[] skillIds, Long projectId) {
        String query = "DELETE FROM TB_SKILLS_NECESSARIAS_PROJETO WHERE FK_ID_SKILL = :idSkill " +
                 "AND FK_ID_PROJETO = :idProject";

        for (int i = 0; i < skillIds.length; i++) {
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                    .addValue("idSkill", skillIds[i])
                    .addValue("idProject", projectId);
                    
            namedParameterJdbcTemplate.update(query, sqlParameterSource);
        }
    }
    
    @Override
    public List<String> getSkillsByProjectId(Long projectId) {
        String query = "SELECT nm_skill_name FROM TB_SKILL s " +
                   "JOIN TB_SKILLS_NECESSARIAS_PROJETO psk ON s.nr_id_skill = psk.fk_id_skill " +
                   "WHERE psk.fk_id_projeto = :idProject";
        
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("idProject", projectId);

        var result = namedParameterJdbcTemplate.queryForList(query, namedParameters);
    
        List<String> skills = new ArrayList<>();

        for(var r : result){
            skills.add((String) r.get("nm_skill_name"));
        }
        return skills;
    }

    @Override
    public boolean checkStatusProject(Long idProject) {

        String query = "SELECT TP_STATUS FROM TB_PROJETO WHERE NR_ID_PROJETO = :idProject";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("idProject", idProject);
        String status = namedParameterJdbcTemplate.queryForObject(query, sqlParameterSource, String.class);

        if(status == null){
            return false;
        }
        return status.equals("ABERTO");
    }

    @Override
    public BigDecimal priceByProjectId(Long id) {
        String query = "SELECT vl_preco FROM TB_PROJETO WHERE NR_ID_PROJETO = :id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("id", id);
        try{
            BigDecimal price = namedParameterJdbcTemplate.queryForObject(query, sqlParameterSource, BigDecimal.class);
        
            if(price == null){
                throw new RuntimeException("Projeto não encontrado");
            }
            return price;
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("Erro interno ao buscar preço do projeto", e);
        }
    }

    private SimpleJdbcCall createJdbcCall(String functionName) {
        return new SimpleJdbcCall(jdbcTemplate).withFunctionName(functionName);
    }
}