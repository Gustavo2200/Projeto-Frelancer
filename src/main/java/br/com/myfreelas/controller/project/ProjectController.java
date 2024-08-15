package br.com.myfreelas.controller.project;
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.myfreelas.dto.project.ProjectDtoRequest;
import br.com.myfreelas.dto.project.UpdateProjectDtoRequest;
import br.com.myfreelas.model.Skill;
import br.com.myfreelas.service.AuthService;
import br.com.myfreelas.service.ProjectService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping(value = "/project", produces = {"application/json"})
public class ProjectController implements SwaggerProjectController{

    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private AuthService authService;

    @PostMapping(path = "/save-project", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveProject(@RequestHeader("Authorization") String token, @RequestBody ProjectDtoRequest project) {

        projectService.saveProject(authService.userIdByToken(token), project);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(path = "/list")
    public ResponseEntity<?> listProjectsByStatus(@RequestParam("status") String status) {
        
        return new ResponseEntity<>(projectService.listProjectsByStatus(status), HttpStatus.OK);
    }

    @PutMapping(path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProject(@RequestHeader("Authorization") String token, @RequestBody UpdateProjectDtoRequest project) {
        
        projectService.updateProject(project, authService.userIdByToken(token));
        
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> findProjectById(@PathVariable Long id) { 
        return new ResponseEntity<>(projectService.findProjectById(id), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<?> deleteProject(@RequestHeader("Authorization") String token, 
                @RequestParam("id") Long id) {

        projectService.deleteProject(id, authService.userIdByToken(token), authService.getRoleByToken(token));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/my-projects")
    public ResponseEntity<?> listProjectsByUserId(@RequestHeader("Authorization") String token) {

        return new ResponseEntity<>(projectService.listProjectsByUserId(authService.userIdByToken(token)), HttpStatus.OK);
    }

    @GetMapping("/my-projectsBy")
    public ResponseEntity<?> listProjectsByUserId(@RequestHeader("Authorization") String token,
                @RequestParam("status") String status) {

        return new ResponseEntity<>(projectService.listProjectsByUSerIdAndStatus(authService.userIdByToken(token), status), HttpStatus.OK);
    }

    @PostMapping(path = "/add-skill", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addSkillNecessary(@RequestHeader("Authorization") String token, 
                @RequestParam("id") Long idProject, @RequestBody List<Skill> skills) {

        projectService.addSkillNecessary(skills, idProject, authService.userIdByToken(token));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/remove-skill")
    public ResponseEntity<?> removeSkillNecessary(@RequestHeader("Authorization") String token, 
                @RequestParam("id") Long idProject, @RequestBody List<Skill> skills) {
                    
        projectService.removeSkillNecessary(skills, idProject, authService.userIdByToken(token));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
