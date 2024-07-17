package br.com.myfreelas.controller;
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

import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.myfreelas.config.utils.TokenUtils;
import br.com.myfreelas.dto.project.ProjectDtoRequest;
import br.com.myfreelas.dto.project.ProjectDtoResponse;
import br.com.myfreelas.dto.project.UpdateProjectDtoRequest;
import br.com.myfreelas.err.ErrResponse;
import br.com.myfreelas.err.exceptions.FreelasException;
import br.com.myfreelas.model.Project;
import br.com.myfreelas.model.Skill;
import br.com.myfreelas.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping(value = "/project", produces = {"application/json"})
@Tag(name = "project controller")
@SecurityRequirement(name = "bearerAuth")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private TokenUtils tokenUtils;

    @Operation(summary = "Cria um novo projeto e o salva no banco de dados", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Projeto criado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "404", description = "Skill nao encontada", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        })
    })
    @PostMapping(path = "/save-project", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveProject(@RequestHeader("Authorization") String token, @RequestBody ProjectDtoRequest project) {

         DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7)); // Remove o prefixo "Bearer "
         String role = decodedJWT.getClaim("role").asString();

        if("FREELANCER".equals(role)) {
            throw new FreelasException("Acesso nao permitido a Freelancers", HttpStatus.UNAUTHORIZED.value());
        }
        String userId = decodedJWT.getClaim("user_id").asString(); // Extrai o ID do usuário
        projectService.saveProject(Long.parseLong(userId), project);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Busca todos os projetos com o status informado", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Projetos buscados com sucesso", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProjectDtoResponse.class)))
        }),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado ou token inválido", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "404", description = "Status não definido", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        })
    })
    @GetMapping(path = "/list")
    public ResponseEntity<?> listProjectsByStatus(@Parameter(description = "Status do projeto", required = true) 
        @RequestParam("status") String status) {
        
            List<ProjectDtoResponse> projects = projectService.listProjectsByStatus(status);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @Operation(summary = "Atualiza um projeto no banco de dados", method = "PUT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Projeto atualizado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "403", description = "Cliente não é o dono do projeto", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "404", description = "Projeto não encontrado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        })
    })
    @PutMapping(path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
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

    @Operation(summary = "Busca o projeto pelo ID no banco de dados", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Projeto encontrado com sucesso", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Project.class))
        }),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "404", description = "Projeto não encontrado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        })
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> findProjectById(@PathVariable Long id) {
        Project project = projectService.findProjectById(id);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @Operation(summary = "Apaga o projeto pelo ID do banco de dados", method = "DELETE")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Projeto excluído com sucesso"),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "403", description = "Cliente não é o dono do projeto ou projeto esta EM ANDAMENTO", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "404", description = "Projeto não encontrado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        })
    })
    @DeleteMapping(path = "/delete")
    public ResponseEntity<?> deleteProject(@RequestHeader("Authorization") String token, 
                @Parameter(description = "ID do projeto", required = true) @RequestParam("id") Long id) {

        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        String role = decodedJWT.getClaim("role").asString();
        if("FREELANCER".equals(role)) {
            throw new FreelasException("Acesso nao permitido a Freelancers", HttpStatus.UNAUTHORIZED.value());
        }
        String userId = decodedJWT.getClaim("user_id").asString(); 
        projectService.deleteProject(id, Long.parseLong(userId), role);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Busca todos os projetos pelo ID do dono do projeto ou do freelancer que esta trabalhando nele", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Projetos encontrados com sucesso", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Project.class)))
        }),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "404", description = "Nenhum projeto encontrado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        })
    })
    @GetMapping(path = "/my-projects")
    public ResponseEntity<?> listProjectsByUserId(@RequestHeader("Authorization") String token) {

        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        String userId = decodedJWT.getClaim("user_id").asString();
        return new ResponseEntity<>(projectService.listProjectsByUserId(Long.parseLong(userId)), HttpStatus.OK);
    }

    @Operation(summary = "Busca todos os projetos pelo ID do dono do projeto ou do freelancer que esta trabalhando nele com base no status informado", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Projetos encontrados com sucesso", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Project.class)))
        }),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "404", description = "Nenhum projeto encontrado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        })
    })
    @GetMapping("/my-projectsBy")
    public ResponseEntity<?> listProjectsByUserId(@RequestHeader("Authorization") String token,
                @Parameter(description = "Status do projeto", required = true) @RequestParam("status") String status) {

        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        String userId = decodedJWT.getClaim("user_id").asString();
        return new ResponseEntity<>(projectService.listProjectsByUSerIdAndStatus(Long.parseLong(userId), status), HttpStatus.OK);
    }

    @Operation(summary = "Adiciona skills de dependência a um projeto", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Skills adicionadas com sucesso"),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "403", description = "Cliente não é o dono do projeto", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "404", description = "Projeto não encontrado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        })
    })
    @PostMapping(path = "/add-skill", consumes = MediaType.APPLICATION_JSON_VALUE)
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

    @Operation(summary = "Remove skills de dependência de um projeto", method = "DELETE")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Skills removidas com sucesso"),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "403", description = "Cliente não é o dono do projeto", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "404", description = "Projeto não encontrado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        })
    })
    @DeleteMapping("/remove-skill")
    public ResponseEntity<?> removeSkillNecessary(@RequestHeader("Authorization") String token, 
                @Parameter(description = "ID do projeto", required = true) @RequestParam("id") Long idProject, 
                @RequestBody List<Skill> skills) {
                    
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
