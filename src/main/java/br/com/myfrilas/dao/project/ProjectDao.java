package br.com.myfrilas.dao.project;

import java.util.List;

import br.com.myfrilas.dto.project.ProjectDtoRequest;
import br.com.myfrilas.dto.project.ProjectDtoResponse;
import br.com.myfrilas.dto.project.UpdateProjectDtoRequest;
import br.com.myfrilas.model.Project;
import br.com.myfrilas.model.Skill;

public interface ProjectDao {
    
    void saveProject(Long clientId, ProjectDtoRequest project, Long[] idSkills);

    List<ProjectDtoResponse> listProjectsByStatus(String status);

    void updateProject(UpdateProjectDtoRequest project);

    void deleteProjectById(Long id);

    void acceptProposal(Project project);

    void rejectProposal(Project project);

    Project getProjectById(Long id);

    boolean checkProjectExists(Long id);

    Integer customerIdByProjectId(Long id);

    List<Project> listProjectsByCustomerId(Long id);

    List<Project> listProjectsByFreelancerId(Long id);

    void addSkillNecessary(Long[] skillIds, Long projectId);

    void removeSkillNecessary(Long[] skillIds, Long projectId);

    List<Skill> getSkillsByProjectId(Long projectId);
}
