package br.com.myfreelas.contantutils;

public enum RegexUtils {
    
    VALID_EMAIL("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"),
    VALID_NAME("[a-zA-Z]+(?:\\\\s[a-zA-Z]+)*"),
    ONLY_NUMBERS("[^0-9]");

    RegexUtils(String regex) {
        this.regex = regex;
    }

    private String regex;
    public String getRegex() {
        return regex;
    }
}
