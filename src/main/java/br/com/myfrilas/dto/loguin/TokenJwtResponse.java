package br.com.myfrilas.dto.loguin;

public class TokenJwtResponse {
    
    private String token;

    public TokenJwtResponse(String token) {
        this.token = token;
    }

    public TokenJwtResponse() {}
    
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
