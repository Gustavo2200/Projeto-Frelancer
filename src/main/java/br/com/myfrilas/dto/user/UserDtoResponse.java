package br.com.myfrilas.dto.user;

import br.com.myfrilas.enums.TypeUser;
import br.com.myfrilas.model.User;

public class UserDtoResponse {
    private Long id;
    private String name;
    private String cpf;
    private String email;
    private String phone;
    private TypeUser typeUser;

    public UserDtoResponse() {}
    
    public UserDtoResponse(Long id, String name, String cpf, String email, String phone, TypeUser typeUser) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.phone = phone;
        this.typeUser = typeUser;
    }

    public UserDtoResponse(User user, Long id) {
        this.id = id;
        this.name = user.getName();
        this.cpf = user.getCpf();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.typeUser = user.getTypeUser();
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public TypeUser getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(TypeUser typeUser) {
        this.typeUser = typeUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}