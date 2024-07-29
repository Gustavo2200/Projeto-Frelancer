package br.com.myfreelas.controller;
 
import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.myfreelas.config.utils.TokenUtils;
import br.com.myfreelas.err.ErrResponse;
import br.com.myfreelas.err.exceptions.FreelasException;
import br.com.myfreelas.model.Proposal;
import br.com.myfreelas.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping(value = "/customer", produces = {"application/json"})
@Tag(name = "customer controller")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;

    @Autowired
    private TokenUtils tokenUtils;

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
    @GetMapping(path = "/view-proposals") 
    public ResponseEntity<?> viewProposalsByProjectId(@RequestHeader("Authorization") String token,
                @Parameter(description = "Id do projeto", required = true)@RequestParam("idProject") Long idProject) {

        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        String role = decodedJWT.getClaim("role").asString();
        if("FREELANCER".equals(role)) {
            throw new FreelasException("Acesso não permitido a Freelancers", HttpStatus.UNAUTHORIZED.value());
        }

        String userId = decodedJWT.getClaim("user_id").asString();
        return new ResponseEntity<>(customerService.viewProposalsByProjectId(idProject, Long.parseLong(userId)), HttpStatus.OK);
    }

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
    @DeleteMapping(path = "/reject-proposal")
    public ResponseEntity<?> rejectProposal(@RequestHeader("Authorization") String token,
                @Parameter(description = "Id da proposta", required = true)@RequestParam("idProposal") Long idProposal) {
        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        String role = decodedJWT.getClaim("role").asString();
        if("FREELANCER".equals(role)) {
            throw new FreelasException("Acesso não permitido a Freelancers", HttpStatus.UNAUTHORIZED.value());
        }
        String userId = decodedJWT.getClaim("user_id").asString();
        customerService.rejectProposal(idProposal, Long.parseLong(userId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

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
    @PostMapping(path = "/accept-proposal")
    public ResponseEntity<?> acceptProposal(@RequestHeader("Authorization") String token,
                @Parameter(description = "Id da proposta", required = true)@RequestParam("idProposal") Long idProposal) {
                    
        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        String role = decodedJWT.getClaim("role").asString();
        if("FREELANCER".equals(role)) {
            throw new FreelasException("Acesso não permitido a Freelancers", HttpStatus.UNAUTHORIZED.value());
        }
        String userId = decodedJWT.getClaim("user_id").asString();
        customerService.acceptProposal(idProposal, Long.parseLong(userId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

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
    @PostMapping(path = "/complete-project")
    public ResponseEntity<?> completeProject(@RequestHeader("Authorization") String token,
                @Parameter(description = "Id do projeto", required = true)@RequestParam("idProject") Long idProject) {

        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        String role = decodedJWT.getClaim("role").asString();
        if("FREELANCER".equals(role)) {
            throw new FreelasException("Acesso não permitido a Freelancers", HttpStatus.UNAUTHORIZED.value());
        }
        String userId = decodedJWT.getClaim("user_id").asString();
        customerService.completeProject(idProject, Long.parseLong(userId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

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
    @PostMapping(path = "/deposit-balance", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> depositBalance(@RequestHeader("Authorization") String token,
                @RequestBody HashMap<String, BigDecimal> body) {
        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        String role = decodedJWT.getClaim("role").asString();
        if("FREELANCER".equals(role)) {
            throw new FreelasException("Acesso não permitido a Freelancers", HttpStatus.UNAUTHORIZED.value());
        }
        String userId = decodedJWT.getClaim("user_id").asString();
        customerService.depositBalance(Long.parseLong(userId), body.get("value"));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
