package br.com.myfreelas.dao.project;

import java.math.BigDecimal;
import java.util.List;

import br.com.myfreelas.dto.project.ProjectDtoRequest;
import br.com.myfreelas.dto.project.ProjectDtoResponse;
import br.com.myfreelas.dto.project.UpdateProjectDtoRequest;
import br.com.myfreelas.enums.StatusProject;
import br.com.myfreelas.model.Project;

public interface ProjectDao {
    
    void saveProject(Long clientId, ProjectDtoRequest project, Long[] idSkills);

    List<ProjectDtoResponse> listProjectsByStatus(String status);

    void updateProject(UpdateProjectDtoRequest project);

    void deleteProjectById(Long id);
    
    Project getProjectById(Long id);

    boolean checkProjectExists(Long id);

    Long customerIdByProjectId(Long id);

    List<Project> listProjectsByUserId(Long idUser);

    List<Project> listProjectsByUserIdAndStatus(Long idUser, String status);

    void addSkillNecessary(Long[] skillIds, Long projectId);

    void removeSkillNecessary(Long[] skillIds, Long projectId);

    List<String> getSkillsByProjectId(Long projectId);

    StatusProject checkStatusProject(Long idProject);

    BigDecimal priceByProjectId(Long idProject);
}
