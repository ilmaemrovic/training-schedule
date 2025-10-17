package raf.microservice.components.userservice.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;

public interface JwtService {
    String extractUsername(String jwt);
    String generateToken(Map<String, Object> claims, UserDetails userDetails, long expiration);
    String generateToken(UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);
    boolean isTokenValid(String token, UserDetails userDetails);
    public List<String> extractRoles(String token);
}
