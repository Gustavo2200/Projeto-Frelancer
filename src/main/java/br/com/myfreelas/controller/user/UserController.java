package br.com.myfreelas.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.com.myfreelas.model.User;
import br.com.myfreelas.service.AuthService;
import br.com.myfreelas.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping(value = "/user", produces = {"application/json"})
public class UserController implements SwaggerUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping(path = "/save-user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveUser(@RequestBody  User user) {
        
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(path = "/find")
    public ResponseEntity<?> findUserById(@RequestParam Long id){

        return new ResponseEntity<>(userService.userById(id), HttpStatus.OK);

    }

    @GetMapping("/historic")
    public ResponseEntity<?> transferHistory(@RequestHeader ("Authorization") String token){
    
        return new ResponseEntity<>(userService.transfrerHistoryByUserId(authService.userIdByToken(token)), HttpStatus.OK);
    }

    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(@RequestHeader ("Authorization") String token){
        return new ResponseEntity<>(userService.getBalance(authService.userIdByToken(token)), HttpStatus.OK);
    }
}
