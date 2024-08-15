package br.com.myfreelas.controller.skill;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.myfreelas.dto.skill.SkillDto;
import br.com.myfreelas.err.ErrResponse;
import br.com.myfreelas.model.Skill;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "skills controller")
public interface SwaggerSkillController {
    
    @Operation(summary = "Cria uma nova skill e o salva no banco de dados", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Skill criado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "409", description = "Skill já existe no banco de dados", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        })
    })
    ResponseEntity<?> saveSkill(@RequestHeader("Authorization") String token, @RequestBody Skill skill);

    //=====================================================================================================

    @Operation(summary = "Lista todas as skills", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Skills listadas com sucesso", content = {
            @Content(mediaType = "application/json",array = @ArraySchema(schema = @Schema(implementation = SkillDto.class)))
        }),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        })
    })
    ResponseEntity<?> getSkills(@RequestHeader("Authorization") String token);

    //======================================================================================================
    
    @Operation(summary = "Deleta uma skill do banco de dados", method = "DELETE")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Skill deletada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "404", description = "Skill não encontrada", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        })
    })
    ResponseEntity<?> deleteSkill(@RequestHeader("Authorization") String token, 
        @Parameter(description = "Id da skill", required = true)@RequestParam("id") Long id);

    //======================================================================================================

    @Operation(summary = "Atualiza uma skill do banco de dados", method = "PUT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Skill atualizada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "404", description = "Skill não encontrada", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        })
    })
    ResponseEntity<?> updateSkill(@RequestHeader("Authorization") String token, @RequestBody SkillDto skill);
}
