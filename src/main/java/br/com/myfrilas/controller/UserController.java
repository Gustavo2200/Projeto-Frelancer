package br.com.myfrilas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.myfrilas.config.utils.TokenUtils;
import br.com.myfrilas.enums.TypeUser;
import br.com.myfrilas.model.User;
import br.com.myfrilas.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenUtils tokenUtils;

    @PostMapping(path = "/save-user")
    public ResponseEntity<?> saveUser(@RequestBody  User user) {
        
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/find")
    public ResponseEntity<?> findUserById(@RequestParam Long id){
        
        TypeUser type = userService.typeUserById(id);

        if(type.equals(TypeUser.CUSTOMER)) {
            return new ResponseEntity<>(userService.customerById(id), HttpStatus.OK);
        }
        else if(type.equals(TypeUser.FREELANCER)) {
            return new ResponseEntity<>(userService.freelancerById(id), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping("/historic")
    public ResponseEntity<?> transferHistory(@RequestHeader ("Authorization") String token){
    
        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        Long id = Long.parseLong(decodedJWT.getClaim("user_id").asString());

        return new ResponseEntity<>(userService.transfrerHistoryByUserId(id), HttpStatus.OK);
    }

    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(@RequestHeader ("Authorization") String token){
        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        Long id = Long.parseLong(decodedJWT.getClaim("user_id").asString());
        return new ResponseEntity<>(userService.getBalance(id), HttpStatus.OK);
    }
    
    
    
    
}
