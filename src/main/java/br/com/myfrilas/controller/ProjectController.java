package br.com.myfrilas.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.myfrilas.config.utils.TokenUtils;
import br.com.myfrilas.dto.project.ProjectDtoRequest;
import br.com.myfrilas.dto.project.ProjectDtoResponse;
import br.com.myfrilas.dto.project.UpdateProjectDtoRequest;
import br.com.myfrilas.err.exceptions.FreelasException;
import br.com.myfrilas.model.Project;
import br.com.myfrilas.model.Skill;
import br.com.myfrilas.service.ProjectService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private TokenUtils tokenUtils;

    @PostMapping("/save-project")
    public ResponseEntity<?> saveProject(@RequestHeader("Authorization") String token, @RequestBody ProjectDtoRequest project) {

         DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7)); // Remove o prefixo "Bearer "
         String role = decodedJWT.getClaim("role").asString();

        if("FREELANCER".equals(role)) {
            throw new FreelasException("Acesso nao permitido a Freelancers", HttpStatus.UNAUTHORIZED.value());
        }
        String userId = decodedJWT.getClaim("user_id").asString(); // Extrai o ID do usuário
        projectService.saveProject(Long.parseLong(userId), project);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<?> listProjectsByStatus(@RequestParam("projects") String status) {
        List<ProjectDtoResponse> projects = projectService.listProjectsByStatus(status);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateProject(@RequestHeader("Authorization") String token, @RequestBody UpdateProjectDtoRequest project) {
        
        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7)); // Remove o prefixo "Bearer "
        String role = decodedJWT.getClaim("role").asString();
        if("FREELANCER".equals(role)) {
            throw new FreelasException("Acesso nao permitido a Freelancers", HttpStatus.UNAUTHORIZED.value());
        }
        String userId = decodedJWT.getClaim("user_id").asString(); // Extrai o ID do usuário
        projectService.updateProject(project, Long.parseLong(userId));
        
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findProjectById(@PathVariable Long id) {
        Project project = projectService.findProjectById(id);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProject(@RequestHeader("Authorization") String token, 
                @RequestParam("id") Long id) {

        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        String role = decodedJWT.getClaim("role").asString();
        if("FREELANCER".equals(role)) {
            throw new FreelasException("Acesso nao permitido a Freelancers", HttpStatus.UNAUTHORIZED.value());
        }
        String userId = decodedJWT.getClaim("user_id").asString(); 
        projectService.deleteProject(id, Long.parseLong(userId), role);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/my-projects")
    public ResponseEntity<?> listProjectsByUserId(@RequestHeader("Authorization") String token,
                @RequestParam("status") String status) {

        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        String userId = decodedJWT.getClaim("user_id").asString();
        String role = decodedJWT.getClaim("role").asString();

        List<Project> projects = new ArrayList<>();

        if("FREELANCER".equals(role)) {
            projects = projectService.listProjectsByFreelancerId(Long.parseLong(userId), status);
        }
        else if("CUSTOMER".equals(role)) {
            projects = projectService.listProjectsByCustomerId(Long.parseLong(userId), status);
        }
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @PostMapping("/add-skill")
    public ResponseEntity<?> addSkillNecessary(@RequestHeader("Authorization") String token, 
                @RequestParam("id") Long idProject, @RequestBody List<Skill> skills) {

        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        String role = decodedJWT.getClaim("role").asString();
        if("FREELANCER".equals(role)) {
            return new ResponseEntity<>("Acesso não permitido a Freelancers", HttpStatus.FORBIDDEN);
        }
        String userId = decodedJWT.getClaim("user_id").asString();
        projectService.addSkillNecessary(skills, idProject, Long.parseLong(userId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/remove-skill")
    public ResponseEntity<?> removeSkillNecessary(@RequestHeader("Authorization") String token, 
                @RequestParam("id") Long idProject, @RequestBody List<Skill> skills) {
        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        String role = decodedJWT.getClaim("role").asString();
        if("FREELANCER".equals(role)) {
            throw new FreelasException("Acesso nao permitido a Freelancers", HttpStatus.UNAUTHORIZED.value());
        }
        String userId = decodedJWT.getClaim("user_id").asString();
        projectService.removeSkillNecessary(skills, idProject, Long.parseLong(userId));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
