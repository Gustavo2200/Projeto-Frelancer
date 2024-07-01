package br.com.myfrilas.dao.freelancer;

import java.util.List;

import br.com.myfrilas.dto.freelancer.FreelancerDtoResponse;
import br.com.myfrilas.dto.proposal.ProposalDtoRequest;

public interface FreelancerDao {
    
    void saveSkill(List<Long> idSkills, Long idFreelancer);

    void deleteSkill(List<Long> idSkills, Long idFreelancer);

    Long idBySkillName(String skill);

    boolean checkSkillExists(String skill);

    boolean checkSkillIsAlreadySaved(Long idSkill, Long idFreelancer);

    FreelancerDtoResponse freelancerById(Long idFreelancer);

    void sendProposal(ProposalDtoRequest proposal, Long idFreelancer);
}
