package br.com.myfreelas.controller.freelancer;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.com.myfreelas.dto.proposal.ProposalDtoRequest;
import br.com.myfreelas.model.Skill;
import br.com.myfreelas.service.AuthService;
import br.com.myfreelas.service.FreelancerService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping(value = "/freelancers", produces = {"application/json"})
public class FreelancerController implements SwaggerFreelancerController {

    @Autowired
    private FreelancerService freelancerService;

    @Autowired
    private AuthService authService;
    
    @PostMapping(path = "/save-skill", produces = MediaType.APPLICATION_JSON_VALUE)
      public ResponseEntity<?> saveSkill(@RequestHeader("Authorization") String token, @RequestBody List<Skill> skills) {
           
            freelancerService.saveSkill(authService.userIdByToken(token),skills);
            return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete-skill", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteSkill(@RequestHeader("Authorization") String token, @RequestBody List<Skill> skills) {

        freelancerService.deleteSkill(authService.userIdByToken(token), skills);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/send-proposal", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendProposal(@RequestHeader("Authorization") String token ,@RequestBody ProposalDtoRequest proposal) {

        freelancerService.sendProposal(proposal, authService.userIdByToken(token));
        return new ResponseEntity<>(HttpStatus.OK);
    }   
}
