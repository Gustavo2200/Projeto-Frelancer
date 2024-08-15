package br.com.myfreelas.err;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.myfreelas.contantutils.ErrorMessageContants;
import br.com.myfreelas.err.exceptions.DataBaseException;
import br.com.myfreelas.err.exceptions.FreelasException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CatchExceptions extends ResponseEntityExceptionHandler {

    private List<ErrResponse> errors;

    @SuppressWarnings("null")
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

    @SuppressWarnings("null")
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrResponse errResponse = new ErrResponse(ErrorMessageContants.INVALID_DATA_TYPE.getMessage(), HttpStatus.BAD_REQUEST.value(),
                ((ServletWebRequest) request).getRequest().getRequestURI(), LocalDateTime.now().toString());
        return new ResponseEntity<>(errResponse, HttpStatus.BAD_REQUEST);
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

    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<?> runtimeException(DataBaseException ex, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrResponse(ex.getMessage(),
        ex.getStatus(),
        request.getRequestURI(),LocalDateTime.now().toString()),
        HttpStatus.valueOf(ex.getStatus()));
    }
}
