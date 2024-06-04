package br.com.myfrilas.err.exceptions;

import java.util.List;

import br.com.myfrilas.err.ErrResponse;

public class FreelasException extends RuntimeException {
    
    private int status;
    private List<ErrResponse> errors;

    public FreelasException(String message, int status){
        super(message);
        this.status = status;
    }

    public FreelasException(List<ErrResponse> errors, int status){
        this.errors = errors;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public List<ErrResponse> getErrors() {
        return errors;
    }
    
}
