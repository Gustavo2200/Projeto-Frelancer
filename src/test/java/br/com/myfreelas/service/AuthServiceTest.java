package br.com.myfreelas.service;

import br.com.myfreelas.config.utils.TokenUtils;
import br.com.myfreelas.dao.user.UserDao;
import br.com.myfreelas.dto.loguin.LoguinDto;
import br.com.myfreelas.enums.TypeUser;
import br.com.myfreelas.err.exceptions.FreelasException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static  org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    TokenUtils tokenUtils;

    @Mock
    UserDao userDao;

    @Mock
    PasswordEncoder passwordEncoder;

    @Autowired
    @InjectMocks
    AuthService service;

    @SuppressWarnings("deprecation")
    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Retorna o token com sucesso")
    void getTokenSuccess() {
        when(userDao.getUser(any())).thenReturn(getDataAuthenticationSuccess());
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        service.getToken(getuser());

        verify(tokenUtils, times(1)).getToken(any());
    }

    @Test
    @DisplayName("Usuario não existe no banco de dados")
    void getTokenErrorCase1(){
        when(userDao.getUser(any())).thenReturn(null);

        Exception e = Assertions.assertThrows(FreelasException.class, () ->{
            service.getToken(new LoguinDto("teste@gmail.com", "123456"));
        });

        assertEquals("Email não encontrado", e.getMessage());
    }

    @Test
    @DisplayName("Senha sendo incorreta deve retornar erro")
    void getTokenErrorCase2() {
        when(userDao.getUser(any())).thenReturn(getDataAuthenticationSuccess());
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        Exception e = assertThrows(FreelasException.class, () ->{
            service.getToken(getuser());
        });
        assertEquals("Senha inválida", e.getMessage());
    }



    Map<String, Object> getDataAuthenticationSuccess(){
        return new HashMap<String, Object>(){{
            put("NR_ID_USUAIO", 1L);
            put("DS_EMAIL", "teste@gmail.com");
            put("DS_SENHA", "123456");
            put("TP_TIPO_USUAIO", TypeUser.FREELANCER);
        }};
    }

    LoguinDto getuser(){
        return new LoguinDto("teste@gmail.com", "123456");
    }
}