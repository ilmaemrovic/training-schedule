package raf.microservice.components.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.microservice.components.userservice.model.Client;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findClientByUsernameAndPassword(String username, String password);

    Optional<Client> findClientByUsername(String username);
}
