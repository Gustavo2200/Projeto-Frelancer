package br.com.myfrilas.dao.freelancer;

public interface FreelancerDao {
    
    void saveSkill(Long idSkill, Long idFreelancer);
    Long idBySkillName(String skill);
    boolean checkSkillExists(String skill);
    boolean checkSkillIsAlreadySaved(Long idSkill, Long idFreelancer);
}
