package br.com.myfreelas.controller;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.myfreelas.config.utils.TokenUtils;
import br.com.myfreelas.dto.proposal.ProposalDtoRequest;
import br.com.myfreelas.err.ErrResponse;
import br.com.myfreelas.err.exceptions.FreelasException;
import br.com.myfreelas.model.Skill;
import br.com.myfreelas.service.FreelancerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping(value = "/freelancers", produces = {"application/json"})
@Tag(name = "freelancer controller")
public class FreelancerController {

    @Autowired
    private FreelancerService freelancerService;

    @Autowired
    private TokenUtils tokenUtils;
    
    @Operation(summary = "Salva skills para um freelancer" , method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Skills foram salvas com sucesso"),
        @ApiResponse(responseCode = "400", description = "Freelancer já possui essa skill", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
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
    @PostMapping(path = "/save-skill", produces = MediaType.APPLICATION_JSON_VALUE)
      public ResponseEntity<?> saveSkill(@RequestHeader("Authorization") String token, @RequestBody List<Skill> skills) {
            DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7)); // Remove o prefixo "Bearer "
            String role = decodedJWT.getClaim("role").asString();

            if ("CUSTOMER".equals(role)) {
                throw new FreelasException("Acesso permitido somente a Freelancers", HttpStatus.UNAUTHORIZED.value());
            }

            String userId = decodedJWT.getClaim("user_id").asString(); // Extrai o ID do usuário
            
            freelancerService.saveSkill(Long.parseLong(userId),skills);
            return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Deleta skills de um freelancer" , method = "DELETE")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Skills foram deletadas com sucesso"),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "404", description = "Skill não encontrada para o freelancer", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        })
    })
    @DeleteMapping(path = "/delete-skill", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteSkill(@RequestHeader("Authorization") String token, @RequestBody List<Skill> skills) {

        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        String role = decodedJWT.getClaim("role").asString();

        if ("CUSTOMER".equals(role)) {
            throw new FreelasException("Acesso permitido somente a Freelancers", HttpStatus.UNAUTHORIZED.value());
        }

        String userId = decodedJWT.getClaim("user_id").asString();

        freelancerService.deleteSkill(Long.parseLong(userId), skills);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Envvia a proposta do freelancer para o projeto" , method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Proposta enviada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Apenas projetos em aberto podem receber propostas", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "404", description = "Projeto não encontrado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "409", description = "Proposta ja enviada para o projeto", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        })
    })
    @PostMapping(path = "/send-proposal", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendProposal(@RequestHeader("Authorization") String token ,@RequestBody ProposalDtoRequest proposal) {

        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        String role = decodedJWT.getClaim("role").asString();

        if ("CUSTOMER".equals(role)) {
            throw new FreelasException("Acesso permitido somente a Freelancers", HttpStatus.UNAUTHORIZED.value());
        }

        String userId = decodedJWT.getClaim("user_id").asString();
        freelancerService.sendProposal(proposal, Long.parseLong(userId));
        return new ResponseEntity<>(HttpStatus.OK);
    }   
}
