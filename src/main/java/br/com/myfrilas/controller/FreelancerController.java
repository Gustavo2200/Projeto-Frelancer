package br.com.myfrilas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.myfrilas.config.utils.TokenUtils;
import br.com.myfrilas.dto.proposal.ProposalDtoRequest;
import br.com.myfrilas.err.exceptions.FreelasException;
import br.com.myfrilas.model.Skill;
import br.com.myfrilas.service.FreelancerService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/freelancers")
public class FreelancerController {

    @Autowired
    private FreelancerService freelancerService;

    @Autowired
    private TokenUtils tokenUtils;
    
    @PostMapping("/save-skill")
      public ResponseEntity<?> saveSkill(@RequestHeader("Authorization") String token, @RequestBody List<Skill> skills) {
            DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7)); // Remove o prefixo "Bearer "
            String role = decodedJWT.getClaim("role").asString();

            if ("CUSTOMER".equals(role)) {
                throw new FreelasException("Acesso permitido somente a Freelancers", HttpStatus.UNAUTHORIZED.value());
            }

            String userId = decodedJWT.getClaim("user_id").asString(); // Extrai o ID do usuário
            
            freelancerService.saveSkill(Long.parseLong(userId),skills);
            return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete-skill")
    public ResponseEntity<?> deleteSkill(@RequestHeader("Authorization") String token, @RequestBody List<Skill> skills) {

        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        String role = decodedJWT.getClaim("role").asString();

        if ("CUSTOMER".equals(role)) {
            throw new FreelasException("Acesso permitido somente a Freelancers", HttpStatus.UNAUTHORIZED.value());
        }

        String userId = decodedJWT.getClaim("user_id").asString();

        freelancerService.deleteSkill(Long.parseLong(userId), skills);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/find")
    public ResponseEntity<?> findFreelancerById(@RequestParam Long id) {

        return new ResponseEntity<>(freelancerService.freelancerById(id), HttpStatus.OK);
    }

    @PostMapping("/send-proposal")
    public ResponseEntity<?> sendProposal(@RequestHeader("Authorization") String token ,@RequestBody ProposalDtoRequest proposal) {

        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        String role = decodedJWT.getClaim("role").asString();

        if ("CUSTOMER".equals(role)) {
            throw new FreelasException("Acesso permitido somente a Freelancers", HttpStatus.UNAUTHORIZED.value());
        }

        String userId = decodedJWT.getClaim("user_id").asString();
        freelancerService.sendProposal(proposal, Long.parseLong(userId));
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    
    
}
