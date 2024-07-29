package br.com.myfreelas.dto.loguin;

public class LoguinDto {
    
    private String email;
    private String password;

    public LoguinDto(){}

    public LoguinDto(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
