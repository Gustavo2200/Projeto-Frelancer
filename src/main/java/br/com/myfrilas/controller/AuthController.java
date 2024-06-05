package br.com.myfrilas.controller;

import org.springframework.web.bind.annotation.RestController;

import br.com.myfrilas.dto.loguin.LoguinDto;
import br.com.myfrilas.service.AuthService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;



@RestController
public class AuthController {

    @Autowired
    private AuthService authService;
    
    @PostMapping("/get-token")
    public ResponseEntity<?> getToken(@RequestBody LoguinDto loguin) {
        
        return ResponseEntity.ok(authService.getToken(loguin));
    }    
}
