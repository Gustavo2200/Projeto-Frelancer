package br.com.myfreelas.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.myfreelas.contantutils.ErrorMessageContants;
import br.com.myfreelas.contantutils.RegexUtils;
import br.com.myfreelas.dao.freelancer.FreelancerDao;
import br.com.myfreelas.dao.user.UserDao;
import br.com.myfreelas.dto.freelancer.FreelancerDtoResponse;
import br.com.myfreelas.dto.user.UserDtoResponse;
import br.com.myfreelas.enums.TypeUser;
import br.com.myfreelas.err.ErrResponse;
import br.com.myfreelas.err.exceptions.FreelasException;
import br.com.myfreelas.model.Transaction;
import br.com.myfreelas.model.User;

@Service
public class UserService {

    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private FreelancerDao freelancerDao;
    private CnpjValidator cnpjValidator;

    public UserService(UserDao userDao, PasswordEncoder passwordEncoder, FreelancerDao freelancerDao, CnpjValidator cnpjValidator) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.freelancerDao = freelancerDao;
        this.cnpjValidator = cnpjValidator;
    }

    public void saveUser(User user) {
        user.setPhone(user.getPhone().replaceAll(RegexUtils.ONLY_NUMBERS.getRegex(), ""));
        user.setCpfCNPJ(user.getCpfCNPJ().replaceAll(RegexUtils.ONLY_NUMBERS.getRegex(), ""));
        checkRegex(user);
        checkBeforeSaving(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.saveUser(user);
    }
    
     public UserDtoResponse userById(Long idUser) {

        TypeUser typeUser = typeUserById(idUser);

        User user = userDao.userById(idUser);
        if(user == null) {
            throw new FreelasException(ErrorMessageContants.USER_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND.value());
        }

        if(typeUser.equals(TypeUser.CUSTOMER) || typeUser.equals(TypeUser.ADMIN)) {
            return new UserDtoResponse(user, idUser);
        }
        else if(typeUser.equals(TypeUser.FREELANCER)) {
            return new FreelancerDtoResponse(user, idUser, freelancerDao.getSkillsByFreelancerId(idUser));
        }

        throw new FreelasException(ErrorMessageContants.USER_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND.value());
        
    }

    public List<Transaction> transfrerHistoryByUserId(Long idUser) {
        return userDao.transfrerHistoryByUserId(idUser);
    }

    public BigDecimal getBalance(Long id) {
        return userDao.getBalance(id);
    }

    private TypeUser typeUserById(Long id){
        return userDao.typeUserById(id);
    }

    private void checkBeforeSaving(User user) {
        List<ErrResponse> erros = new ArrayList<>();

        if (userDao.checkCpfExists(user.getCpfCNPJ())) {
            if(user.getCpfCNPJ().length() == 14) {
                erros.add(new ErrResponse(ErrorMessageContants.CNPJ_ALREADY_REGISTERED.getMessage(), HttpStatus.CONFLICT.value()));
            }
            else{
                erros.add(new ErrResponse(ErrorMessageContants.CPF_ALREADY_REGISTERED.getMessage(), HttpStatus.CONFLICT.value()));
            }
        }

        if (userDao.checkEmailExists(user.getEmail())) {
            erros.add(new ErrResponse(ErrorMessageContants.EMAIL_ALREADY_REGISTERED.getMessage(), HttpStatus.CONFLICT.value()));
        }

        if (userDao.checkPhoneExists(user.getPhone())) {
            erros.add(new ErrResponse(ErrorMessageContants.PHONE_ALREADY_REGISTERED.getMessage(), HttpStatus.CONFLICT.value()));
        }

        if (!erros.isEmpty()) {
            throw new FreelasException(erros, HttpStatus.CONFLICT.value());
        }
    }

    private void checkRegex(User user) {
        List<ErrResponse> erros = new ArrayList<>();

        if(!user.getName().matches(RegexUtils.VALID_NAME.getRegex())) {
            erros.add(new ErrResponse(ErrorMessageContants.NAME_INVALID.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
        
        if(user.getCpfCNPJ().length() < 11 || user.getCpfCNPJ().length() > 14) {
            erros.add(new ErrResponse(ErrorMessageContants.CPF_INVALID.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
        else if(user.getCpfCNPJ().length() == 11){
            if(!checkCpf(user.getCpfCNPJ())) {
                erros.add(new ErrResponse(ErrorMessageContants.CPF_INVALID.getMessage(), HttpStatus.BAD_REQUEST.value()));
            }
        }
        else{
            if(!cnpjValidator.validateCnpj(user.getCpfCNPJ())){
                erros.add(new ErrResponse(ErrorMessageContants.CNPJ_INVALID.getMessage(), HttpStatus.BAD_REQUEST.value()));
            }
        }

        if(!user.getEmail().matches(RegexUtils.VALID_EMAIL.getRegex())) {
            erros.add(new ErrResponse(ErrorMessageContants.EMAIL_INVALID.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }

        if(user.getPhone().length() != 11) {
            erros.add(new ErrResponse(ErrorMessageContants.PHONE_INVALID.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }

        if(user.getPassword().length() < 6 || user.getPassword().length() > 20) {
            erros.add(new ErrResponse(ErrorMessageContants.PASSWORD_INVALID.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }

        if(!user.getTypeUser().equals(TypeUser.FREELANCER) && 
        !user.getTypeUser().equals(TypeUser.CUSTOMER) &&
        !user.getTypeUser().equals(TypeUser.ADMIN)) {
            
            throw new FreelasException(ErrorMessageContants.TYPE_USER_INVALID.getMessage(), HttpStatus.BAD_REQUEST.value());
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
