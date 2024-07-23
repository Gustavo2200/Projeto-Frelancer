package br.com.myfreelas.controller;

import org.springframework.web.bind.annotation.RestController;

import br.com.myfreelas.dto.loguin.LoguinDto;
import br.com.myfreelas.dto.loguin.TokenJwtResponse;
import br.com.myfreelas.err.ErrResponse;
import br.com.myfreelas.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;



@RestController
@RequestMapping(produces = {"application/json"})
@Tag(name = "auth controller")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Realiza o login de usuarios retornando o token de acesso", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Retorna o token de acesso",
            content = {
                @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                schema = @Schema(implementation = TokenJwtResponse.class)) 
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
    @PostMapping(path = "/get-token", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getToken(@RequestBody LoguinDto loguin) {
        
        return ResponseEntity.ok(authService.getToken(loguin));
    }    
}
