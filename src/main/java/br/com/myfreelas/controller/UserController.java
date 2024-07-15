package br.com.myfreelas.controller;
 
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.myfreelas.config.utils.TokenUtils;
import br.com.myfreelas.dto.freelancer.FreelancerDtoResponse;
import br.com.myfreelas.dto.user.UserDtoResponse;
import br.com.myfreelas.enums.TypeUser;
import br.com.myfreelas.err.ErrResponse;
import br.com.myfreelas.model.Transaction;
import br.com.myfreelas.model.User;
import br.com.myfreelas.service.UserService;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping(value = "/user", produces = {"application/json"})
@Tag(name = "user controller")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenUtils tokenUtils;

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
    @PostMapping(path = "/save-user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveUser(@RequestBody  User user) {
        
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

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
    @GetMapping(path = "/find")
    public ResponseEntity<?> findUserById(@Parameter(description = "Id do usuario", required = true)
                        @RequestParam Long id){
        
        TypeUser type = userService.typeUserById(id);

        if(type.equals(TypeUser.CUSTOMER)) {
            return new ResponseEntity<>(userService.customerById(id), HttpStatus.OK);
        }
        else if(type.equals(TypeUser.FREELANCER)) {
            return new ResponseEntity<>(userService.freelancerById(id), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @Operation(summary = "Busca o historico de transferencia de um usuario pelo id", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historico encontrado com sucesso", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Transaction.class)))
        }),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        })
    })
    @GetMapping("/historic")
    public ResponseEntity<?> transferHistory(@Parameter(description = "token do usuario", required = false)@RequestHeader ("Authorization") String token){
    
        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        Long id = Long.parseLong(decodedJWT.getClaim("user_id").asString());

        return new ResponseEntity<>(userService.transfrerHistoryByUserId(id), HttpStatus.OK);
    }

    @Operation(summary = "Busca o saldo de um usuario pelo id", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Saldo encontrado com sucesso", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = BigDecimal.class))
        }),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrResponse.class))
        })
    })
    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(@RequestHeader ("Authorization") String token){
        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        Long id = Long.parseLong(decodedJWT.getClaim("user_id").asString());
        return new ResponseEntity<>(userService.getBalance(id), HttpStatus.OK);
    }
}
