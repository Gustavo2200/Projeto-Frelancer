package br.com.myfreelas.controller;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.myfreelas.config.utils.TokenUtils;
import br.com.myfreelas.dto.skill.SkillDto;
import br.com.myfreelas.err.ErrResponse;
import br.com.myfreelas.err.exceptions.FreelasException;
import br.com.myfreelas.model.Skill;
import br.com.myfreelas.service.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping(produces = {"application/json"})
@Tag(name = "skills controller")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @Autowired
    private TokenUtils tokenUtils;

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
    @PostMapping(path = "/save-skill", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveSkill(@RequestHeader("Authorization") String token, @RequestBody Skill skill) {

        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));

        String role = decodedJWT.getClaim("role").asString();
        if(!"ADMIN".equals(role)) {
            throw new FreelasException("Acesso nao permitido", HttpStatus.UNAUTHORIZED.value());
        }
        
        skillService.saveSkill(skill);

        return new ResponseEntity<>(HttpStatus.OK);
    }

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
    @GetMapping(path = "/list-skills")
    public ResponseEntity<?> getSkills(@RequestHeader("Authorization") String token) {
        
        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        String role = decodedJWT.getClaim("role").asString();
        if(!"ADMIN".equals(role)) {
            throw new FreelasException("Acesso nao permitido", HttpStatus.UNAUTHORIZED.value());
        }
        return new ResponseEntity<>(skillService.getSkills(), HttpStatus.OK);
        
    }

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
    @DeleteMapping(path = "/delete-skill")
    public ResponseEntity<?> deleteSkill(@RequestHeader("Authorization") String token, 
        @Parameter(description = "Id da skill", required = true)@RequestParam("id") Long id) {
        
        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        String role = decodedJWT.getClaim("role").asString();
        if(!"ADMIN".equals(role)) {
            throw new FreelasException("Acesso nao permitido", HttpStatus.UNAUTHORIZED.value());
        }
        skillService.deleteSkill(id);
        return new ResponseEntity<>(HttpStatus.OK);  
    }

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
    @PutMapping(path = "/update-skill", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateSkill(@RequestHeader("Authorization") String token, @RequestBody SkillDto skill) {
        
        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        String role = decodedJWT.getClaim("role").asString();
        if(!"ADMIN".equals(role)) {
            throw new FreelasException("Acesso nao permitido", HttpStatus.UNAUTHORIZED.value());
        }
        skillService.updateSkill(skill);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
