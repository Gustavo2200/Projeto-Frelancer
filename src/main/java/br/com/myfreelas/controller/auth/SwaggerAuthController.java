package br.com.myfreelas.controller.auth;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.myfreelas.dto.loguin.LoguinDto;
import br.com.myfreelas.dto.loguin.TokenJwtResponse;
import br.com.myfreelas.err.ErrResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "auth controller")
public interface SwaggerAuthController {

    @Operation(summary = "Realiza o login de usuarios retornando o token de acesso", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna o token de acesso", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TokenJwtResponse.class))
        }),
        @ApiResponse(responseCode = "404", description = "Email não encontrado", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "400", description = "Senha incorreta" , content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrResponse.class))
        })
    })
    ResponseEntity<?> getToken(@RequestBody LoguinDto loguin);
} 

