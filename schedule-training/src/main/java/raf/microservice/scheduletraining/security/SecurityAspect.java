package raf.microservice.scheduletraining.security;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import raf.microservice.scheduletraining.security.service.TokenService;

@Aspect
@Configuration
public class SecurityAspect {

    private TokenService tokenService;

    public SecurityAspect(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Around("@annotation(raf.microservice.scheduletraining.security.CheckSecurity)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        String token = null;
        for (int i = 0; i < methodSignature.getParameterNames().length; i++) {
            if (methodSignature.getParameterNames()[i].equals("authorization")) {
                if (joinPoint.getArgs()[i].toString().startsWith("Bearer")) {
                    token = joinPoint.getArgs()[i].toString().split(" ")[1];
                }
            }
        }

        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Claims claims = tokenService.parseToken(token);
        if (claims == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        CheckSecurity checkSecurity = method.getAnnotation(CheckSecurity.class);
        @SuppressWarnings("unchecked")
        List<Map<String,String>> role = claims.get("role", List.class);

        if (checkSecurity.roles().length == 0 || Arrays.asList(checkSecurity.roles()).contains(role.get(0).get("authority"))) {
            return joinPoint.proceed();
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

}