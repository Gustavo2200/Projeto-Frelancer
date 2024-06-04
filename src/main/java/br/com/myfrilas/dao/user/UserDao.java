package br.com.myfrilas.dao.user;

import br.com.myfrilas.model.User;

public interface UserDao {

    void saveUser(User user);
    boolean checkCpfExists(String cpf);
    boolean checkEmailExists(String email);
    boolean checkPhoneExists(String phone);
    
}