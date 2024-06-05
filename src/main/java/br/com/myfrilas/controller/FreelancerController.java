package br.com.myfrilas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.myfrilas.config.utils.TokenUtils;
import br.com.myfrilas.dto.freelancer.SkillDtoRequest;
import br.com.myfrilas.service.FreelancerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
public class FreelancerController {

    @Autowired
    private FreelancerService freelancerService;

    @Autowired
    private TokenUtils tokenUtils;
    
    @PostMapping("/freelancers/save-skill")
      public ResponseEntity<?> saveSkill(@RequestHeader("Authorization") String token, @RequestBody SkillDtoRequest skill) {
            DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7)); // Remove o prefixo "Bearer "
            String role = decodedJWT.getClaim("role").asString();

            if (!"FREELANCER".equals(role)) {
                return new ResponseEntity<>("Acesso permitido somente a Freelancers", HttpStatus.FORBIDDEN);
            }

            String userId = decodedJWT.getClaim("user_id").asString(); // Extrai o ID do usuário
            
            freelancerService.saveSkill(Long.parseLong(userId),skill.getSkill());
            return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
