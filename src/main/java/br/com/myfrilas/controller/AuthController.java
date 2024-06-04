package br.com.myfrilas.controller;

import org.springframework.web.bind.annotation.RestController;

import br.com.myfrilas.dto.loguin.LoguinDto;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
public class AuthController {
    
    @PostMapping("/get-token")
    public String postMethodName() {
        
        
        return "Deu certo";
    }

    @GetMapping("/oi")
    public String getMethodName() {
        return "Entrei sem token";
    }
    
    
}
