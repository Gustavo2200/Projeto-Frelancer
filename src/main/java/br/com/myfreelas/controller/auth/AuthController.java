package br.com.myfreelas.controller.auth;

import org.springframework.web.bind.annotation.RestController;

import br.com.myfreelas.dto.loguin.LoguinDto;
import br.com.myfreelas.service.AuthService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;



@RestController
@RequestMapping(produces = {"application/json"})
public class AuthController implements SwaggerAuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(path = "/get-token", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getToken(@RequestBody LoguinDto loguin) {
        
        return ResponseEntity.ok(authService.getToken(loguin));
    }    
}
