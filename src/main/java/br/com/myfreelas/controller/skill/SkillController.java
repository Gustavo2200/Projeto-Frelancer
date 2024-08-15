package br.com.myfreelas.controller.skill;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.com.myfreelas.dto.skill.SkillDto;
import br.com.myfreelas.model.Skill;
import br.com.myfreelas.service.SkillService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping(value = "/skill", produces = {"application/json"})
public class SkillController implements SwaggerSkillController {

    @Autowired
    private SkillService skillService;

    @PostMapping(path = "/save-skill", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveSkill(@RequestHeader("Authorization") String token, @RequestBody Skill skill) {

        skillService.saveSkill(skill);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/list-skills")
    public ResponseEntity<?> getSkills(@RequestHeader("Authorization") String token) {
        
        return new ResponseEntity<>(skillService.getSkills(), HttpStatus.OK);
        
    }

    @DeleteMapping(path = "/delete-skill")
    public ResponseEntity<?> deleteSkill(@RequestHeader("Authorization") String token, @RequestParam("id") Long id) {
        
        skillService.deleteSkill(id);
        return new ResponseEntity<>(HttpStatus.OK);  
    }

    @PutMapping(path = "/update-skill", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateSkill(@RequestHeader("Authorization") String token, @RequestBody SkillDto skill) {
        
        skillService.updateSkill(skill);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
