package br.com.myfreelas.controller.project;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.myfreelas.dto.project.ProjectDtoRequest;
import br.com.myfreelas.dto.project.ProjectDtoResponse;
import br.com.myfreelas.dto.project.UpdateProjectDtoRequest;
import br.com.myfreelas.err.ErrResponse;
import br.com.myfreelas.model.Project;
import br.com.myfreelas.model.Skill;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "project controller")
@SecurityRequirement(name = "bearerAuth")
public interface SwaggerProjectController {
    
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
    ResponseEntity<?> saveProject(@RequestHeader("Authorization") String token, @RequestBody ProjectDtoRequest project);

    //================================================================================================================

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
    ResponseEntity<?> listProjectsByStatus(@Parameter(description = "Status do projeto", required = true) 
        @RequestParam("status") String status);

    //======================================================================================================

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
    public ResponseEntity<?> updateProject(@RequestHeader("Authorization") String token, @RequestBody UpdateProjectDtoRequest project);

    //======================================================================================================

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
    ResponseEntity<?> findProjectById(@PathVariable Long id);

    //======================================================================================================

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
    ResponseEntity<?> deleteProject(@RequestHeader("Authorization") String token, 
                @Parameter(description = "ID do projeto", required = true) @RequestParam("id") Long id);

    //======================================================================================================

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
    ResponseEntity<?> listProjectsByUserId(@RequestHeader("Authorization") String token);

    //======================================================================================================

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
    ResponseEntity<?> listProjectsByUserId(@RequestHeader("Authorization") String token,
                @Parameter(description = "Status do projeto", required = true) @RequestParam("status") String status);

    //=======================================================================================================

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
    ResponseEntity<?> addSkillNecessary(@RequestHeader("Authorization") String token, 
                @Parameter(description = "ID do projeto", required = true) @RequestParam("id") Long idProject, 
                @RequestBody List<Skill> skills);
            
    //=======================================================================================================

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
    ResponseEntity<?> removeSkillNecessary(@RequestHeader("Authorization") String token, 
                @Parameter(description = "ID do projeto", required = true) @RequestParam("id") Long idProject, 
                @RequestBody List<Skill> skills);
}
