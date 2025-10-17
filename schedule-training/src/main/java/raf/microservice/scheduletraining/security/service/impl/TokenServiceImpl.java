package raf.microservice.scheduletraining.security.service.impl;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import raf.microservice.scheduletraining.security.service.TokenService;

@Service
@AllArgsConstructor
public class TokenServiceImpl implements TokenService {

    @Value("${jwt.secret.key}")
    private String jwtSecret;

    @Override
    public Claims parseToken(String jwt) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
        return claims;
    }
}
