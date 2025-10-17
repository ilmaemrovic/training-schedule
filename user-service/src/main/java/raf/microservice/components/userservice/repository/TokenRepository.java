package raf.microservice.components.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import raf.microservice.components.userservice.model.Token;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findTokenByToken(String token);
    List<Token> findAllValidTokenByUsersDetails(UserDetails userDetails);
}
