package br.com.myfrilas.dto.freelancer;

public class SkillDtoRequest {
    
    private String Skill;

    public SkillDtoRequest() {}
    public SkillDtoRequest(String skill) { this.Skill = skill; }

    public String getSkill() {
        return Skill;
    }
    public void setSkill(String skill) {
        Skill = skill;
    }
}
