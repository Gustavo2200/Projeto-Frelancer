package br.com.myfrilas.service;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.myfrilas.config.utils.TokenUtils;
import br.com.myfrilas.dao.user.UserDao;
import br.com.myfrilas.dto.loguin.LoguinDto;
import br.com.myfrilas.dto.loguin.TokenJwtResponse;
import br.com.myfrilas.err.exceptions.FreelasException;

@Service
public class AuthService {
    
    private TokenUtils tokenUtils; 
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;

    public AuthService(TokenUtils tokenUtils, UserDao userDao, PasswordEncoder passwordEncoder) {
        this.tokenUtils = tokenUtils;
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public TokenJwtResponse getToken(LoguinDto loguinDto){
        var dataAuthentication = getDataAuthentication(loguinDto);
        return tokenUtils.getToken(dataAuthentication);
    }

    private Map<String, Object> getDataAuthentication(LoguinDto loguinDto) {
        Map<String, Object> dataAuthentication = userDao.getUser(loguinDto.getEmail());

        if (dataAuthentication == null) {
            throw new FreelasException("Login inválido", HttpStatus.UNAUTHORIZED.value());
        }

        String passwordFromDb = (String) dataAuthentication.get("DS_SENHA");
        if (passwordFromDb == null) {
            throw new FreelasException("Senha não encontrada para o usuário", HttpStatus.UNAUTHORIZED.value());
        }

        checkPassword(loguinDto.getPassword(), passwordFromDb);
        return dataAuthentication;
    }

    private void checkPassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new FreelasException("Senha inválida", HttpStatus.UNAUTHORIZED.value());
        }
    }
}
