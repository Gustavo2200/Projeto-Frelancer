package br.com.myfreelas.dto.skill;

public class SkillDto {
    
    private Long id;
    private String skill;

    public SkillDto() {}

    public SkillDto(Long id, String skill) {
        this.id = id;
        this.skill = skill;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;    
    }
    public String getSkill() {  
        return skill;
    }
    public void setSkill(String skill) {
        this.skill = skill; 
    }
}
