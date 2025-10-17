package raf.microservice.components.userservice.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import raf.microservice.components.userservice.service.impl.CustomUserDetailsService;
import raf.microservice.components.userservice.service.impl.JwtServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@Component
//@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtServiceImpl jwtService;
    private final UserDetailsService userDetailsService;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JwtServiceImpl jwtService, UserDetailsService userDetailsService,
                                   CustomUserDetailsService customUserDetailsService){
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {  // check if auth header exists
            filterChain.doFilter(request, response); // check next req
            return;
        }

        jwt = authHeader.substring(7);
        final String username = jwtService.extractUsername(jwt);
        final Claims role = jwtService.extractAllClaims(jwt);
        Object o = role.get("role");
        customUserDetailsService.setUserType((((LinkedHashMap<?, ?>)((ArrayList<?>)o).get(0)).
                get("authority").toString().substring(5)));

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username); // exception banned
            if (jwtService.isTokenValid(jwt, userDetails)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
