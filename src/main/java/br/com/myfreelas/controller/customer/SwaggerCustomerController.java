package br.com.myfreelas.controller.customer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.myfreelas.dto.customer.BalanceDtoRequest;
import br.com.myfreelas.err.ErrResponse;
import br.com.myfreelas.model.Proposal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "customer controller")
public interface SwaggerCustomerController {
    
        @Operation(summary = "Busca todas as propostas de um projeto específico", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna as propostas do projeto", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Proposal.class)))
        }),
        @ApiResponse(responseCode = "404", description = "Projeto não encontrado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "400", description = "Cliente não é autor do projeto", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        })
    })
    ResponseEntity<?> viewProposalsByProjectId(@RequestHeader("Authorization") String token,
                @Parameter(description = "Id do projeto", required = true)@RequestParam("idProject") Long idProject);

//====================================================================================================================
    
    @Operation(summary = "Recusa uma proposta, apagando-a", method = "DELETE")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Proposta recusada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Projeto não encontrado ou proposta não encontrada", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "400", description = "Cliente não é autor do projeto", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        })
    })
    ResponseEntity<?> rejectProposal(@RequestHeader("Authorization") String token,
    @Parameter(description = "Id da proposta", required = true)@RequestParam("idProposal") Long idProposal);
    
    //==================================================================================================

    @Operation(summary = "Aceita uma proposta, atualizando a situação do projeto no banco de dados, também apaga todas as demais propostas do projeto", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso na aceição da proposta"),
        @ApiResponse(responseCode = "404", description = "Projeto não encontrado ou proposta não encontrada", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "400", description = "Cliente não é autor do projeto", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        })
    })
    ResponseEntity<?> acceptProposal(@RequestHeader("Authorization") String token,
            @Parameter(description = "Id da proposta", required = true)@RequestParam("idProposal") Long idProposal);
    
    //===================================================================================================

    @Operation(summary = "Completa o projeto, atualizando a situação do projeto no banco de dados e efetua o pagamento de maneira automática", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso na conclusão do projeto"),
        @ApiResponse(responseCode = "404", description = "Projeto não encontrado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "400", description = "Cliente não é autor do projeto, ou saldo insuficiente", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "403", description = "Só é possível completar o projeto se seu status for EM PROGRESSO", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        })
    })
    ResponseEntity<?> completeProject(@RequestHeader("Authorization") String token,
                @Parameter(description = "Id do projeto", required = true)@RequestParam("idProject") Long idProject);
    
    //====================================================================================================

    @Operation(summary = "Deposita saldo à conta do cliente na plataforma", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso ao depositar saldo"),
        @ApiResponse(responseCode = "400", description = "Entrada de dados não numéricos", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        })
    })
    ResponseEntity<?> depositBalance(@RequestHeader("Authorization") String token,
                @RequestBody BalanceDtoRequest balance);
}
