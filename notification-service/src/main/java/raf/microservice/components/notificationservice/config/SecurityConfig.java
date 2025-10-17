package raf.microservice.components.notificationservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import raf.microservice.components.notificationservice.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;

    private static final String[] WHITE_LIST_URL = {
    };

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter){
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/types/**").hasRole("ADMIN")
                        .requestMatchers("/notification/all").hasRole("ADMIN")
                        .requestMatchers("/notification/me").hasAnyRole("ADMIN", "CLIENT", "MANAGER")
                        .requestMatchers("/notification/me/filter").hasAnyRole("ADMIN", "CLIENT", "MANAGER")
                        .requestMatchers("/type/**").hasRole("ADMIN")
                        .requestMatchers(WHITE_LIST_URL).permitAll() //  allow paths to access without auth
                        .anyRequest().authenticated() // all other paths need auth
                )
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(ses -> ses.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);  //  add filter which will check every request


        return httpSecurity.build();
    }

}
