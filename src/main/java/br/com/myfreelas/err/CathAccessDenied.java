package br.com.myfreelas.err;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.myfreelas.contantutils.ErrorMessageContants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CathAccessDenied  implements AccessDeniedHandler{

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        
                ErrResponse error = new ErrResponse(
            ErrorMessageContants.ACCESS_DENIED.getMessage(),
            HttpStatus.FORBIDDEN.value(),
            request.getRequestURI());

            response.setStatus(error.getStatus());
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(error));
    }


    
}
