package br.com.myfreelas.service;

import br.com.myfreelas.err.exceptions.FreelasException;
import org.json.JSONException;
import org.json.JSONObject;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CnpjValidatorTest {

    @Mock
    RestTemplate restTemplate;

    @Autowired
    @InjectMocks
    CnpjValidator cnpjValidator;

    @SuppressWarnings("deprecation")
    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Valida se cnpj valido com sucesso e retorna true")
    void validateCnpjSucess() throws JSONException {

        String url = "https://www.receitaws.com.br/v1/cnpj/";
        String cnpj = "cnpj";

        when(restTemplate.getForObject(url + cnpj, String.class)).thenReturn(getResponseBodySuccess());

        assertTrue(cnpjValidator.validateCnpj(cnpj));
    }

    @Test
    @DisplayName("Cnpj invalido deve retornar false")
    void validateCnpjErrorCase1() throws JSONException {

        String url = "https://www.receitaws.com.br/v1/cnpj/";
        String cnpj = "cnpj";

        when(restTemplate.getForObject(url + cnpj, String.class)).thenReturn(getResponseBodyError());

        assertFalse(cnpjValidator.validateCnpj(cnpj));
    }

    @Test
    @DisplayName("Limite de tentativas exedido deve retornar erro")
    void validateCnpjErrorCase2() throws JSONException {

        String url = "https://www.receitaws.com.br/v1/cnpj/";
        String cnpj = "cnpj";

        when(restTemplate.getForObject(url + cnpj, String.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS));

        Exception e = Assertions.assertThrows(FreelasException.class, () ->{
            cnpjValidator.validateCnpj(cnpj);
        });
        Assertions.assertEquals("Limite de tentativas excedido, tente novamente mais tarde", e.getMessage());
    }

    String getResponseBodySuccess() throws JSONException {
        return new JSONObject().put("status", "OK").toString();
    }

    String getResponseBodyError() throws JSONException {
        return new JSONObject().put("status", "ERROR").toString();
    }
}