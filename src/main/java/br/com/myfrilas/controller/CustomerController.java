package br.com.myfrilas.controller;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.myfrilas.config.utils.TokenUtils;
import br.com.myfrilas.err.exceptions.FreelasException;
import br.com.myfrilas.service.CustomerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/customer")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;

    @Autowired
    private TokenUtils tokenUtils;

    @GetMapping("/view-proposals")
    public ResponseEntity<?> viewProposalsByProjectId(@RequestHeader("Authorization") String token,
                @RequestParam("idProject") Long idProject) {

        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        String role = decodedJWT.getClaim("role").asString();
        if("FREELANCER".equals(role)) {
            throw new FreelasException("Acesso não permitido a Freelancers", HttpStatus.UNAUTHORIZED.value());
        }

        String userId = decodedJWT.getClaim("user_id").asString();
        return new ResponseEntity<>(customerService.viewProposalsByProjectId(idProject, Long.parseLong(userId)), HttpStatus.OK);
    }

    @DeleteMapping("/reject-proposal")
    public ResponseEntity<?> rejectProposal(@RequestHeader("Authorization") String token,
                @RequestParam("idProposal") Long idProposal) {
        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        String role = decodedJWT.getClaim("role").asString();
        if("FREELANCER".equals(role)) {
            throw new FreelasException("Acesso não permitido a Freelancers", HttpStatus.UNAUTHORIZED.value());
        }
        String userId = decodedJWT.getClaim("user_id").asString();
        customerService.rejectProposal(idProposal, Long.parseLong(userId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/accept-proposal")
    public ResponseEntity<?> acceptProposal(@RequestHeader("Authorization") String token,
                @RequestParam("idProposal") Long idProposal) {
                    
        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        String role = decodedJWT.getClaim("role").asString();
        if("FREELANCER".equals(role)) {
            throw new FreelasException("Acesso não permitido a Freelancers", HttpStatus.UNAUTHORIZED.value());
        }
        String userId = decodedJWT.getClaim("user_id").asString();
        customerService.acceptProposal(idProposal, Long.parseLong(userId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/complete-project")
    public ResponseEntity<?> completeProject(@RequestHeader("Authorization") String token,
                @RequestParam("idProject") Long idProject) {

        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        String role = decodedJWT.getClaim("role").asString();
        if("FREELANCER".equals(role)) {
            throw new FreelasException("Acesso não permitido a Freelancers", HttpStatus.UNAUTHORIZED.value());
        }
        String userId = decodedJWT.getClaim("user_id").asString();
        customerService.completeProject(idProject, Long.parseLong(userId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/deposit-balance")
    public ResponseEntity<?> depositBalance(@RequestHeader("Authorization") String token,
                @RequestBody HashMap<String, BigDecimal> body) {
        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        String role = decodedJWT.getClaim("role").asString();
        if("FREELANCER".equals(role)) {
            throw new FreelasException("Acesso não permitido a Freelancers", HttpStatus.UNAUTHORIZED.value());
        }
        String userId = decodedJWT.getClaim("user_id").asString();
        customerService.depositBalance(Long.parseLong(userId), body.get("value"));
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
