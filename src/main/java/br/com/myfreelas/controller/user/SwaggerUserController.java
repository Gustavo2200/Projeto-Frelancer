package br.com.myfreelas.controller.user;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.myfreelas.dto.freelancer.FreelancerDtoResponse;
import br.com.myfreelas.dto.user.UserDtoResponse;
import br.com.myfreelas.err.ErrResponse;
import br.com.myfreelas.model.Transaction;
import br.com.myfreelas.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "user controller")
public interface SwaggerUserController {
    
     @Operation(summary = "Cria um novo usuario e o salva no banco de dados", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados invalidos", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ErrResponse.class)))
        }),
        @ApiResponse(responseCode = "409", description = "Dados do usuario já cadastrados no sistema", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "429", 
        description = "Limite de tentativas na validação de CNPJ exedido, é necessario aguardar 1 minuto", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        })
    })
    ResponseEntity<?> saveUser(@RequestBody  User user);

    //=========================================================================================================

    @Operation(summary = "Busca um usuario pelo id", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado com sucesso", content = {
            @Content(mediaType = "application/json", schema = @Schema(oneOf = {UserDtoResponse.class, FreelancerDtoResponse.class}))
        }),
        @ApiResponse(responseCode = "404", description = "Usuario não encontrado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        })
    })
    ResponseEntity<?> findUserById(@Parameter(description = "Id do usuario", required = true)
                        @RequestParam Long id);

    //=====================================================================================================

    @Operation(summary = "Busca o historico de transferencia de um usuario pelo id", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historico encontrado com sucesso", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Transaction.class)))
        }),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        })
    })
    ResponseEntity<?> transferHistory(@RequestHeader ("Authorization") String token);

    //=====================================================================================================

    @Operation(summary = "Busca o saldo de um usuario pelo id", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Saldo encontrado com sucesso", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = BigDecimal.class))
        }),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        })
    })
    ResponseEntity<?> getBalance(@RequestHeader ("Authorization") String token);
}
