package br.com.myfrilas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.com.myfrilas.model.User;
import br.com.myfrilas.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/save-user")
    public ResponseEntity<?> saveUser(@RequestBody  User user) {
        
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    
}
