package br.com.myfrilas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.myfrilas.config.utils.TokenUtils;
import br.com.myfrilas.dto.skill.SkillDto;
import br.com.myfrilas.err.exceptions.FreelasException;
import br.com.myfrilas.model.Skill;
import br.com.myfrilas.service.SkillService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
public class SkillController {

    @Autowired
    private SkillService skillService;

    @Autowired
    private TokenUtils tokenUtils;

    @PostMapping("/save-skill")
    public ResponseEntity<?> saveSkill(@RequestHeader("Authorization") String token, @RequestBody Skill skill) {

        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));

        String role = decodedJWT.getClaim("role").asString();
        if(!"ADMIN".equals(role)) {
            throw new FreelasException("Acesso nao permitido", HttpStatus.UNAUTHORIZED.value());
        }
        
        skillService.saveSkill(skill);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/list-skills")
    public ResponseEntity<?> getSkills(@RequestHeader("Authorization") String token) {
        
        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        String role = decodedJWT.getClaim("role").asString();
        if(!"ADMIN".equals(role)) {
            throw new FreelasException("Acesso nao permitido", HttpStatus.UNAUTHORIZED.value());
        }
        return new ResponseEntity<>(skillService.getSkills(), HttpStatus.OK);
        
    }

    @DeleteMapping("/delete-skill")
    public ResponseEntity<?> deleteSkill(@RequestHeader("Authorization") String token, @RequestParam("id") Long id) {
        
        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        String role = decodedJWT.getClaim("role").asString();
        if(!"ADMIN".equals(role)) {
            throw new FreelasException("Acesso nao permitido", HttpStatus.UNAUTHORIZED.value());
        }
        skillService.deleteSkill(id);
        return new ResponseEntity<>(HttpStatus.OK);  
    }

    @PutMapping("/update-skill")
    public ResponseEntity<?> updateSkill(@RequestHeader("Authorization") String token, @RequestBody SkillDto skill) {
        
        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        String role = decodedJWT.getClaim("role").asString();
        if(!"ADMIN".equals(role)) {
            throw new FreelasException("Acesso nao permitido", HttpStatus.UNAUTHORIZED.value());
        }
        skillService.updateSkill(skill);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    
    
}
