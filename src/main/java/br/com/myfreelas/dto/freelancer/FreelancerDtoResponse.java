package br.com.myfreelas.dto.freelancer;

import java.util.List;

import br.com.myfreelas.dto.user.UserDtoResponse;
import br.com.myfreelas.enums.TypeUser;
import br.com.myfreelas.model.User;

public class FreelancerDtoResponse extends UserDtoResponse {
    
    private List<String> skills;

    public FreelancerDtoResponse() {}

    public FreelancerDtoResponse(Long id, String name, String cpf, String email, String phone, List<String> skills, TypeUser typeUser) {
        super(id, name, cpf, email, phone, typeUser);
        this.skills = skills;
    }

    public FreelancerDtoResponse(User user, Long idUser, List<String> skills){
        super(user, idUser);
        this.skills = skills;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
}