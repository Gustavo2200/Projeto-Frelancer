package br.com.myfreelas.service;

import org.springframework.http.HttpStatus;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import br.com.myfreelas.err.exceptions.FreelasException;

@Service
public class CnpjValidator {
    
    private final String BASE_URL = "https://www.receitaws.com.br/v1/cnpj/";

    private RestTemplate restTemplate = new RestTemplate();

    public boolean validateCnpj(String cnpj) {
        try{
            String url = BASE_URL + cnpj;
            
            String requestBody = restTemplate.getForObject(url, String.class);

            JSONObject json = new JSONObject(requestBody);
            if(json.getString("status").equals("ERROR")){
                return false;
            }
            return true;

        }catch(HttpClientErrorException e){
            throw new FreelasException("Limite de tentativas excedido, tente novamente mais tarde", HttpStatus.TOO_MANY_REQUESTS.value());
        }
    }
}
