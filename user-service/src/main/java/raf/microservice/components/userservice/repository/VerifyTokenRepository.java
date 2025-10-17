package raf.microservice.components.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import raf.microservice.components.userservice.model.Token;
import raf.microservice.components.userservice.model.VerifyToken;

import java.util.Optional;

public interface VerifyTokenRepository extends JpaRepository<VerifyToken, Long> {
    Optional<VerifyToken> findVerifyTokenByVerifyToken(String token);
}
