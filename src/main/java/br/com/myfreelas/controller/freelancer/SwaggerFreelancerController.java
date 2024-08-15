package br.com.myfreelas.controller.freelancer;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import br.com.myfreelas.dto.proposal.ProposalDtoRequest;
import br.com.myfreelas.err.ErrResponse;
import br.com.myfreelas.model.Skill;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "freelancer controller")
public interface SwaggerFreelancerController {
    
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
    ResponseEntity<?> saveSkill(@RequestHeader("Authorization") String token, @RequestBody List<Skill> skills);

    //=========================================================================================================

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
    ResponseEntity<?> deleteSkill(@RequestHeader("Authorization") String token, @RequestBody List<Skill> skills);

    //========================================================================================================

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
    ResponseEntity<?> sendProposal(@RequestHeader("Authorization") String token ,@RequestBody ProposalDtoRequest proposal);
}
