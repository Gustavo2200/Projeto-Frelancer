package br.com.myfreelas.service;

import org.springframework.http.HttpStatus;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import br.com.myfreelas.contantutils.ConstantsUrls;
import br.com.myfreelas.contantutils.ErrorMessageContants;
import br.com.myfreelas.err.exceptions.FreelasException;

@Service
public class CnpjValidator {

    private RestTemplate restTemplate = new RestTemplate();

    public boolean validateCnpj(String cnpj) {
        try{
            String url = ConstantsUrls.API_RECEITA_CNPJ_VALIDATOR.getUrl() + cnpj;
            
            String requestBody = restTemplate.getForObject(url, String.class);

            JSONObject json = new JSONObject(requestBody);
            if(json.getString("status").equals("ERROR")){
                return false;
            }
            return true;

        }catch(HttpClientErrorException e){
            throw new FreelasException(ErrorMessageContants.TOO_MANY_REQUESTS.getMessage(), HttpStatus.TOO_MANY_REQUESTS.value());
        }
    }
}
