package br.com.myfreelas.controller.customer;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.myfreelas.dto.customer.BalanceDtoRequest;
import br.com.myfreelas.service.AuthService;
import br.com.myfreelas.service.CustomerService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping(value = "/customer", produces = {"application/json"})
public class CustomerController implements SwaggerCustomerController {
    
    @Autowired
    private CustomerService customerService;

    @Autowired
    private AuthService authService;

    @GetMapping(path = "/view-proposals") 
    public ResponseEntity<?> viewProposalsByProjectId(@RequestHeader("Authorization") String token,
                            @RequestParam("idProject") Long idProject) {

        return new ResponseEntity<>(customerService.viewProposalsByProjectId(idProject, authService.userIdByToken(token)), HttpStatus.OK);
    }

    @DeleteMapping(path = "/reject-proposal")
    public ResponseEntity<?> rejectProposal(@RequestHeader("Authorization") String token,
                    @RequestParam("idProposal") Long idProposal) {
        customerService.rejectProposal(idProposal, authService.userIdByToken(token));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/accept-proposal")
    public ResponseEntity<?> acceptProposal(@RequestHeader("Authorization") String token,
                @RequestParam("idProposal") Long idProposal) {
                
        customerService.acceptProposal(idProposal, authService.userIdByToken(token));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/complete-project")
    public ResponseEntity<?> completeProject(@RequestHeader("Authorization") String token,
               @RequestParam("idProject") Long idProject) {
        
        customerService.completeProject(idProject, authService.userIdByToken(token));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/deposit-balance", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> depositBalance(@RequestHeader("Authorization") String token,
                @RequestBody BalanceDtoRequest balance) {
        
        customerService.depositBalance(authService.userIdByToken(token), balance.getValue());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
