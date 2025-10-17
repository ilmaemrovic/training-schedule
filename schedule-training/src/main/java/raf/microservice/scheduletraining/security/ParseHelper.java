package raf.microservice.scheduletraining.security;

import io.jsonwebtoken.Claims;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import raf.microservice.scheduletraining.security.service.TokenService;

import java.lang.reflect.Method;
@Configuration
public class ParseHelper {

    private TokenService tokenService;

    public ParseHelper(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public Long giveMeId(String token){
        token = token.split(" ")[1];
        Claims claims = tokenService.parseToken(token);
        if (claims == null) {
            return null;
        }
        return claims.get("id",Long.class);
    }
}
