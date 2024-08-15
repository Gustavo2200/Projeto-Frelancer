package br.com.myfreelas.dao.freelancer;

import java.util.List;

import br.com.myfreelas.dto.proposal.ProposalDtoRequest;

public interface FreelancerDao {
    
    void saveSkill(List<Long> idSkills, Long idFreelancer);

    void deleteSkill(List<Long> idSkills, Long idFreelancer);

    Long idBySkillName(String skill);

    boolean checkSkillIsAlreadySaved(Long idSkill, Long idFreelancer);

    void sendProposal(ProposalDtoRequest proposal, Long idFreelancer);

    List<String> getSkillsByFreelancerId(Long idFreelancer);
}
