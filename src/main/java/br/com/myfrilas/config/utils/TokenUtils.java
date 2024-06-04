package br.com.myfrilas.config.utils;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.myfrilas.dto.loguin.TokenJwtResponse;

@Component
public class TokenUtils {

    @Value("${api.issuer}")
    private String issuer;

    @Value("${api.secret.key}")
    private String secretKey;

    @Value("${api.expiration.time}")
    private Long expiryTime;

    public TokenJwtResponse getToken(Map<String, Object> dataAuthentication) {
        String token = JWT.create()
                .withIssuer(issuer)
                .withSubject(dataAuthentication.get("email").toString())
                .withClaim("user_id", dataAuthentication.get("usuario_id").toString())
                .withClaim("email", dataAuthentication.get("email").toString())
                .withClaim("password", dataAuthentication.get("senha").toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * expiryTime)))
                .sign(Algorithm.HMAC256(secretKey));

        return new TokenJwtResponse(token);
    }
    
    public DecodedJWT verifyToken(String token) {
        return JWT.require(Algorithm.HMAC256(secretKey))
                .withIssuer(issuer)
                .build()
                .verify(token);
    }
}
