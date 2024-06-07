package br.com.myfrilas.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.myfrilas.dao.freelancer.FreelancerDao;
import br.com.myfrilas.dao.project.ProjectDao;
import br.com.myfrilas.dto.project.ProjectDtoRequest;
import br.com.myfrilas.dto.project.ProjectDtoResponse;
import br.com.myfrilas.dto.project.UpdateProjectDtoRequest;
import br.com.myfrilas.enums.StatusProject;
import br.com.myfrilas.err.exceptions.FreelasException;
import br.com.myfrilas.model.Project;
import br.com.myfrilas.model.Skill;

@Service
public class ProjectService {

    private final ProjectDao projectDao;
    private final FreelancerDao freelancerDao;

    public ProjectService(ProjectDao projectDao, FreelancerDao freelancerDao) {
        this.projectDao = projectDao;
        this.freelancerDao = freelancerDao;
    }

    public void saveProject(Long clientId, ProjectDtoRequest project) {
        Long[] idSkills = findIdSkills(project.getSkills());
        projectDao.saveProject(clientId, project, idSkills);
    }

    public List<ProjectDtoResponse> listProjectsByStatus(String status) {
        checkStatus(status);
        return projectDao.listProjectsByStatus(status);
    }

    public void updateProject(UpdateProjectDtoRequest project, Integer id) {
        checkBeforeUpdate(project.getId(), id);
        projectDao.updateProject(project);
    }

    public Project findProjectById(Long id) {
        if(!projectDao.checkProjectExists(id)) {
            throw new FreelasException("Projeto não encontrado", HttpStatus.NOT_FOUND.value());
        }
        return projectDao.getProjectById(id);
    }

    public void deleteProject(Long idProject, Integer idCustomer) {
        checkBeforeUpdate(idProject, idCustomer);
        projectDao.deleteProjectById(idProject);
    }

    public List<Project> listProjectsByCustomerId(Long id) {
        List<Project> projects = projectDao.listProjectsByCustomerId(id);
        if(projects.size() == 0) {
            throw new FreelasException("Nenhum projeto encontrado", HttpStatus.NOT_FOUND.value());
        }
        return projects;
    }
    
    public List<Project> listProjectsByFreelancerId(Long id) {
        List<Project> projects = projectDao.listProjectsByFreelancerId(id);
        if(projects.size() == 0) {
            throw new FreelasException("Nenhum projeto encontrado", HttpStatus.NOT_FOUND.value());
        }
        return projects;
    }

    public void addSkillNecessary(List<Skill> skills, Long projectId, Integer idCustomer) {
        checkBeforeUpdate(projectId, idCustomer);
        checkBeforeAddSkills(skills, projectId);
        Long[] idSkills = findIdSkills(skills);
        projectDao.addSkillNecessary(idSkills, projectId);
    }

    public void removeSkillNecessary(List<Skill> skills, Long projectId, Integer idCustomer) {
        checkBeforeUpdate(projectId, idCustomer);
        if(checkBeforeDeleteSkills(skills, projectId)){
            Long[] idSkills = findIdSkills(skills);
            projectDao.removeSkillNecessary(idSkills, projectId);
        }
    }

    private boolean checkBeforeDeleteSkills(List<Skill> skills, Long projectId) {
        List<Skill> skillsProject = projectDao.getSkillsByProjectId(projectId);
        for(Skill skill : skills) {
            for(Skill skillProject : skillsProject) {
                if(skill.getSkill().equals(skillProject.getSkill())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void checkBeforeAddSkills(List<Skill> skills, Long projectId) {
        if(skills.size() == 0) {
            throw new FreelasException("Nenhum skill informado", HttpStatus.BAD_REQUEST.value());
        }
        List<Skill> skillsProject = projectDao.getSkillsByProjectId(projectId);
        for(Skill skill : skills) {
            for(Skill skillProject : skillsProject) {
                if(skill.getSkill().equals(skillProject.getSkill())) {
                    throw new FreelasException("Skill ja adicionada no projeto", HttpStatus.BAD_REQUEST.value());
                }
            }
        }
    }
    

    private Long[] findIdSkills(List<Skill> skills) {

        Long[] idSkills = new Long[skills.size()];
        if(skills.size() > 0){
            for(int i = 0; i < skills.size(); i++) {
                idSkills[i] = freelancerDao.idBySkillName(skills.get(i).getSkill());
            }
        }
        return idSkills;
    }

    private void checkStatus(String status) {
        if(!status.equalsIgnoreCase(StatusProject.OPEN.getDescription())
        && !status.equalsIgnoreCase(StatusProject.IN_PROGRESS.getDescription())
        && !status.equalsIgnoreCase(StatusProject.DONE.getDescription())) {
            throw new FreelasException("Status não definido", HttpStatus.NOT_FOUND.value());
        }
    }
    private void checkBeforeUpdate(Long  idProject, Integer idCustomer) {
        if(!projectDao.checkProjectExists(idProject)){
            
            throw new FreelasException("Projeto não encontrado", HttpStatus.NOT_FOUND.value());
        }
        else if(projectDao.customerIdByProjectId(idProject) != idCustomer){
            throw new FreelasException("Você so pode atualizar seus projetos", HttpStatus.FORBIDDEN.value());
        }
    }
}
