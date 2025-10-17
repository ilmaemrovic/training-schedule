package raf.microservice.components.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.microservice.components.userservice.model.Manager;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Optional<Manager> findManagerByUsernameAndPassword(String username, String password);
    Optional<Manager> findManagerByUsername(String username);
}
