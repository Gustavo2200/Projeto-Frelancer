package br.com.myfrilas.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.myfrilas.dao.freelancer.FreelancerDao;
import br.com.myfrilas.dao.user.UserDao;
import br.com.myfrilas.dto.freelancer.FreelancerDtoResponse;
import br.com.myfrilas.dto.user.UserDtoResponse;
import br.com.myfrilas.enums.TypeUser;
import br.com.myfrilas.err.ErrResponse;
import br.com.myfrilas.err.exceptions.FreelasException;
import br.com.myfrilas.model.Transaction;
import br.com.myfrilas.model.User;

@Service
public class UserService {

    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private FreelancerDao freelancerDao;

    public UserService(UserDao userDao, PasswordEncoder passwordEncoder, FreelancerDao freelancerDao) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.freelancerDao = freelancerDao;
    }

    public void saveUser(User user) {
        checkRegex(user);
        checkBeforeSaving(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.saveUser(user);
    }
    
    public TypeUser typeUserById(Long id){
        return userDao.typeUserById(id);
    }
     public FreelancerDtoResponse freelancerById(Long idFreelancer) {
        User user = userDao.userById(idFreelancer);
        if(user == null) {
            throw new FreelasException("Usuario com esse id não encontrado", HttpStatus.NOT_FOUND.value());
        }
        return new FreelancerDtoResponse(user, freelancerDao.getSkillsByFreelancerId(idFreelancer), idFreelancer);
    }

    public UserDtoResponse customerById(Long idUser) {
        User user = userDao.userById(idUser);
        if(user == null) {
            throw new FreelasException("Usuario com esse id não encontrado", HttpStatus.NOT_FOUND.value());
        }
        return new UserDtoResponse(user, idUser);
    }

    public List<Transaction> transfrerHistoryByCustomerId(Long idCustomer) {
        return userDao.transfrerHistoryByCustomerId(idCustomer);
    }

    public List<Transaction> transfrerHistoryByFreelancerId(Long idFreelancer) {
        return userDao.transfrerHistoryByFreelancerId(idFreelancer);
    }

    public BigDecimal getBalance(Long id) {
        User user = userDao.userById(id);

        if (user == null) {
            throw new FreelasException("Usuario não encontrado", HttpStatus.NOT_FOUND.value());
        }
        return userDao.getBalance(id);
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
        String regexName = "[a-zA-Z]+(?:\\\\s[a-zA-Z]+)*";
        String regexPhone = "^\\(?(?:\\d{2})\\)?[-.\\s]?\\d{4,5}[-.\\s]?\\d{4}$";

        if(!user.getEmail().matches(regexEmail)) {
            erros.add(new ErrResponse("Email invalido", HttpStatus.BAD_REQUEST.value()));
        }

        if(!checkCpf(user.getCpf())) {
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

    private boolean checkCpf(String CPF){

     // considera-se erro CPF"s formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000") ||
            CPF.equals("11111111111") ||
            CPF.equals("22222222222") || CPF.equals("33333333333") ||
            CPF.equals("44444444444") || CPF.equals("55555555555") ||
            CPF.equals("66666666666") || CPF.equals("77777777777") ||
            CPF.equals("88888888888") || CPF.equals("99999999999") ||
            (CPF.length() != 11))
            return(false);

        char dig10, dig11;
        int sm, i, r, num, peso;

        // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
        // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i=0; i<9; i++) {
        // converte o i-esimo caractere do CPF em um numero:
        // por exemplo, transforma o caractere "0" no inteiro 0
        // (48 eh a posicao de "0" na tabela ASCII)
            num = (int)(CPF.charAt(i) - 48);
            sm = sm + (num * peso);
            peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char)(r + 48); // converte no respectivo caractere numerico

        // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for(i=0; i<10; i++) {
            num = (int)(CPF.charAt(i) - 48);
            sm = sm + (num * peso);
            peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                 dig11 = '0';
            else dig11 = (char)(r + 48);

        // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                 return(true);
            else return(false);
                } catch (Exception erro) {
                return(false);
            }
        }
}
