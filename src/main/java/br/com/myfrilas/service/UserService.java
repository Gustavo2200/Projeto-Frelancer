package br.com.myfrilas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.myfrilas.dao.user.UserDao;
import br.com.myfrilas.err.ErrResponse;
import br.com.myfrilas.err.exceptions.FreelasException;
import br.com.myfrilas.model.User;

@Service
public class UserService {

    private UserDao userDao;
    private PasswordEncoder passwordEncoder;

    public UserService(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(User user) {
        checkRegex(user);
        checkBeforeSaving(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.saveUser(user);
    }
    
    private void checkBeforeSaving(User user) {
        List<ErrResponse> erros = new ArrayList<>();

        if (userDao.checkCpfExists(user.getCpf())) {
            erros.add(new ErrResponse("Cpf já registrado", HttpStatus.CONFLICT.value()));
        }

        if (userDao.checkEmailExists(user.getEmail())) {
            erros.add(new ErrResponse("Email já registrado", HttpStatus.CONFLICT.value()));
        }

        if (userDao.checkPhoneExists(user.getPhone())) {
            erros.add(new ErrResponse("Telefone já registrado", HttpStatus.CONFLICT.value()));
        }

        if (!erros.isEmpty()) {
            throw new FreelasException(erros, HttpStatus.CONFLICT.value());
        }
    }

    private void checkRegex(User user) {
        List<ErrResponse> erros = new ArrayList<>();
        String regexEmail = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        String regexCpf = "[0-9]{11}";
        String regexName = "[a-zA-Z]+(?:\\\\s[a-zA-Z]+)*";
        String regexPhone = "^\\(?(?:\\d{2})\\)?[-.\\s]?\\d{4,5}[-.\\s]?\\d{4}$";

        if(!user.getEmail().matches(regexEmail)) {
            erros.add(new ErrResponse("Email invalido", HttpStatus.BAD_REQUEST.value()));
        }

        if(!user.getCpf().matches(regexCpf)) {
            erros.add(new ErrResponse("Cpf invalido", HttpStatus.BAD_REQUEST.value()));
        }

        if(!user.getName().matches(regexName)) {
            erros.add(new ErrResponse("Nome não pode conter caracteres especiais ou numéricos", HttpStatus.BAD_REQUEST.value()));
        }

        if(!user.getPhone().matches(regexPhone)) {
            erros.add(new ErrResponse("Telefone invalido", HttpStatus.BAD_REQUEST.value()));
        }

        if(user.getPassword().length() < 6 && user.getPassword().length() > 20) {
            erros.add(new ErrResponse("Senha deve ter entre 6 e 20 caracteres", HttpStatus.BAD_REQUEST.value()));
        }

        if( user.getTypeUser() == null) {
            erros.add(new ErrResponse("Tipo de usuario invalido, deve ser FREELANCER ou CUSTOMER", HttpStatus.BAD_REQUEST.value()));
        }

        if (!erros.isEmpty()) {
            throw new FreelasException(erros, HttpStatus.BAD_REQUEST.value());
        }

    }
}
