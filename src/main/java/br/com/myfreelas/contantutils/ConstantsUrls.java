package br.com.myfreelas.contantutils;

public enum ConstantsUrls {
    
    API_RECEITA_CNPJ_VALIDATOR("https://www.receitaws.com.br/v1/cnpj/");

    ConstantsUrls(String url) {
        this.url = url;
    } 

    private String url;
    public String getUrl() {
        return url;
    }
}
