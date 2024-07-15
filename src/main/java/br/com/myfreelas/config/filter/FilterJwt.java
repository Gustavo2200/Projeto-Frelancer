package br.com.myfreelas.config.filter;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.myfreelas.config.utils.TokenUtils;
import br.com.myfreelas.err.ErrResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterJwt extends OncePerRequestFilter {

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
       
            String token = request.getHeader("Authorization");

            try{
                if(token != null){
                    token = token.replace("Bearer ", "");
                    var decodedJwt = tokenUtils.verifyToken(token);
                    SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(decodedJwt.getClaim("email").asString(),
                            decodedJwt.getClaim("password").asString(),
                            Collections.emptyList()));
                }
            }catch(Exception e){
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                var err = new ErrResponse("Token inválido", 401, request.getRequestURI());
                ObjectMapper mapper = new ObjectMapper();
                response.getWriter().write(mapper.writeValueAsString(err));
                response.setStatus(401);
                return;
            }

            filterChain.doFilter(request, response);
    }

    
}