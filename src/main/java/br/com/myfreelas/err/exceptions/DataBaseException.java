package br.com.myfreelas.err.exceptions;

public class DataBaseException extends RuntimeException {

    private int status;

    public DataBaseException(String message, int status){
        super(message);
        this.status = status;
    }

    public DataBaseException(String message){
        super(message);
        this.status = 500;
    }

    public int getStatus() {
        return status;
    }
}  
