package br.com.myfreelas.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.myfreelas.contantutils.ErrorMessageContants;
import br.com.myfreelas.dao.freelancer.FreelancerDao;
import br.com.myfreelas.dao.project.ProjectDao;
import br.com.myfreelas.dto.project.ProjectDtoRequest;
import br.com.myfreelas.dto.project.ProjectDtoResponse;
import br.com.myfreelas.dto.project.UpdateProjectDtoRequest;
import br.com.myfreelas.enums.StatusProject;
import br.com.myfreelas.err.exceptions.FreelasException;
import br.com.myfreelas.model.Project;
import br.com.myfreelas.model.Skill;

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

    public void updateProject(UpdateProjectDtoRequest project, Long id) {
        checkBeforeUpdate(project.getId(), id);
        projectDao.updateProject(project);
    }

    public Project findProjectById(Long id) {
        if(!projectDao.checkProjectExists(id)) {
            throw new FreelasException(ErrorMessageContants.PROJECT_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND.value());
        }
        return projectDao.getProjectById(id);
    }

    public void deleteProject(Long idProject, Long idCustomer, String roleUser) {
        if(!roleUser.equals("ADMIN")) {
            checkBeforeUpdate(idProject, idCustomer);
        }
        else{
            if(!projectDao.checkProjectExists(idProject)) {
                throw new FreelasException(ErrorMessageContants.PROJECT_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND.value());
            }
        }
        
        StatusProject statusProject = projectDao.checkStatusProject(idProject);
        if(statusProject.equals(StatusProject.IN_PROGRESS) && !roleUser.equals("ADMIN")) {
            throw new FreelasException(ErrorMessageContants.CANNOT_DELETE_ONGOING_PROJECTS.getMessage(), HttpStatus.FORBIDDEN.value());
        }
        projectDao.deleteProjectById(idProject);
    }

    public List<Project> listProjectsByUserId(Long idUser) {
        return projectDao.listProjectsByUserId(idUser);
    }
    
    public List<Project> listProjectsByUSerIdAndStatus(Long idUser, String status) {
        checkStatus(status);
        return projectDao.listProjectsByUserIdAndStatus(idUser, status);
    }

    public void addSkillNecessary(List<Skill> skills, Long projectId, Long idCustomer) {
        checkBeforeUpdate(projectId, idCustomer);
        checkBeforeAddSkills(skills, projectId);
        Long[] idSkills = findIdSkills(skills);
        projectDao.addSkillNecessary(idSkills, projectId);
    }

    public void removeSkillNecessary(List<Skill> skills, Long projectId, Long idCustomer) {
        checkBeforeUpdate(projectId, idCustomer);
        if(checkBeforeDeleteSkills(skills, projectId)){
            Long[] idSkills = findIdSkills(skills);
            projectDao.removeSkillNecessary(idSkills, projectId);
        }
        else{
            throw new FreelasException(ErrorMessageContants.SKILL_NOT_FOUND_TO_PROJECT.getMessage(), HttpStatus.NOT_FOUND.value());
        }
    }

    private boolean checkBeforeDeleteSkills(List<Skill> skills, Long projectId) {
        List<String> skillsProject = projectDao.getSkillsByProjectId(projectId);
        for(Skill skill : skills) {
            for(String skillProject : skillsProject) {
                if(skill.getSkill().equals(skillProject)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void checkBeforeAddSkills(List<Skill> skills, Long projectId) {
        if(skills.size() == 0) {
            throw new FreelasException(ErrorMessageContants.NO_SKILLS_REPORTED.getMessage(), HttpStatus.BAD_REQUEST.value());
        }
        List<String> skillsProject = projectDao.getSkillsByProjectId(projectId);
        for(Skill skill : skills) {
            for(String skillProject : skillsProject) {
                if(skill.getSkill().equals(skillProject)) {
                    throw new FreelasException(ErrorMessageContants.SKILL_ALREADY_SAVED.getMessage(), HttpStatus.BAD_REQUEST.value());
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
            throw new FreelasException(ErrorMessageContants.STATUS_PROJECT_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND.value());
        }
    }
    private void checkBeforeUpdate(Long  idProject, Long idCustomer) {
        if(!projectDao.checkProjectExists(idProject)){
            
            throw new FreelasException(ErrorMessageContants.PROJECT_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND.value());
        }
        else if(projectDao.customerIdByProjectId(idProject) != idCustomer){
            throw new FreelasException(ErrorMessageContants.CUSTOMER_NOT_OWNER.getMessage(), HttpStatus.FORBIDDEN.value());
        }
    }
}
