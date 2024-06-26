package br.com.myfrilas.err;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.myfrilas.err.exceptions.FreelasException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CatchExceptions extends ResponseEntityExceptionHandler {

    private List<ErrResponse> errors;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> {
            errors.add(new ErrResponse(err.getDefaultMessage(),HttpStatus.BAD_REQUEST.value(),
                    ((ServletWebRequest) request).getRequest().getRequestURI()));
        });

        return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FreelasException.class)
    public ResponseEntity<?> freelasException(FreelasException ex, HttpServletRequest request) {

        errors = ex.getErrors();
        if (errors != null) {
            for (var error : errors) {
                error.setPath(request.getRequestURI());
            }
            return new ResponseEntity<>(errors, HttpStatus.valueOf(ex.getStatus()));
        }
        return new ResponseEntity<>(new ErrResponse(ex.getMessage(),ex.getStatus(),request.getRequestURI(),
        LocalDateTime.now().toString()),
        HttpStatus.valueOf(ex.getStatus()));
}
}
