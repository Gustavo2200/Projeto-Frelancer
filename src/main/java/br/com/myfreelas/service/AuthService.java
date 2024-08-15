package br.com.myfreelas.service;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.myfreelas.config.utils.TokenUtils;
import br.com.myfreelas.contantutils.ErrorMessageContants;
import br.com.myfreelas.dao.user.UserDao;
import br.com.myfreelas.dto.loguin.LoguinDto;
import br.com.myfreelas.dto.loguin.TokenJwtResponse;
import br.com.myfreelas.err.exceptions.FreelasException;

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

    public Long userIdByToken(String token) {
        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        return Long.parseLong(decodedJWT.getClaim("user_id").asString());
    }

    public String getRoleByToken(String token) {
        DecodedJWT decodedJWT = tokenUtils.verifyToken(token.substring(7));
        return decodedJWT.getClaim("role").asString();
    }

    private Map<String, Object> getDataAuthentication(LoguinDto loguinDto) {
        Map<String, Object> dataAuthentication = userDao.getUser(loguinDto.getEmail());

        if (dataAuthentication == null) {
            throw new FreelasException(ErrorMessageContants.USER_NOT_FOUND_WITH_EMAIL.getMessage(), HttpStatus.NOT_FOUND.value());
        }

        String passwordFromDb = (String) dataAuthentication.get("DS_SENHA");

        checkPassword(loguinDto.getPassword(), passwordFromDb);
        return dataAuthentication;
    }

    private void checkPassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new FreelasException(ErrorMessageContants.INCORRECT_PASSWORD.getMessage(), HttpStatus.BAD_REQUEST.value());
        }
    }
}
