package br.com.myfrilas.dao.user;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import br.com.myfrilas.enums.TypeUser;
import br.com.myfrilas.model.Transaction;
import br.com.myfrilas.model.User;

public interface UserDao {

    void saveUser(User user);
    User userById(Long id);
    boolean checkCpfExists(String cpf);
    boolean checkEmailExists(String email);
    boolean checkPhoneExists(String phone);
    Map<String, Object> getUser(String email);
    TypeUser typeUserById(Long id);
    List<Transaction> transfrerHistoryByUserId(Long idUser);
    BigDecimal getBalance(Long id);
}
