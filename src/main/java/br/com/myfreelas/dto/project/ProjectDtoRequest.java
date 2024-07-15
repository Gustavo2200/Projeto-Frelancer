package br.com.myfreelas.dto.project;

import java.util.List;

import br.com.myfreelas.model.Skill;

public class ProjectDtoRequest {
    
    private String title;
    private String description;
    private List<Skill> necessarySkills;

    public ProjectDtoRequest(String title, String description, List<Skill> necessarySkills) {
        this.title = title;
        this.description = description;
        this.necessarySkills = necessarySkills;
    }
    public ProjectDtoRequest() {}

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public List<Skill> getSkills() {
        return necessarySkills;
    }
    public void setSkills(List<Skill> necessarySkills) {
        this.necessarySkills = necessarySkills;
    }
    
}
