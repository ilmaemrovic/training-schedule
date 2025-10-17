package raf.microservice.components.notificationservice.security.services;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public interface JwtService {
    boolean isTokenValid(String token);
    Collection<? extends GrantedAuthority> extractRoles(String token);
    String extractUsername(String jwt);
}
