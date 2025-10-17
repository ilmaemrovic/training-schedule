package raf.microservice.components.notificationservice.security.services.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import raf.microservice.components.notificationservice.security.services.JwtService;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret.key}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private long jwtExpiration;
    @Value("${jwt.refresh.token.expiration}")
    private long refreshExpiration;

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    public Collection<? extends GrantedAuthority> extractRoles(String token) {
        return extractClaim(token, claims -> {
            Object rawRoles = claims.get("role");

            if (rawRoles instanceof String) {
                // Single role as a string
                return Collections.singletonList(new SimpleGrantedAuthority((String) rawRoles));
            } else if (rawRoles instanceof Collection) {
                // Nested JSON structure, extract "authority" field
                Collection<?> rolesCollection = (Collection<?>) rawRoles;

                return rolesCollection.stream()
                        .filter(role -> role instanceof Map)
                        .map(role -> (Map<?, ?>) role)
                        .filter(roleMap -> roleMap.containsKey("authority"))
                        .map(roleMap -> roleMap.get("authority"))
                        .filter(authority -> authority instanceof String)
                        .map(authority -> new SimpleGrantedAuthority((String) authority))
                        .collect(Collectors.toList());
            } else {
                return Collections.emptyList();
            }
        });
    }


    @Override
    public boolean isTokenValid(String token){
        return !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    @Override
    public String extractUsername(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
