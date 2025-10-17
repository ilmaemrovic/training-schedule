package raf.microservice.components.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.microservice.components.userservice.model.Admin;

import java.util.Optional;

@Repository
public interface  AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findAdminByUsernameAndPassword(String username, String password);
    Optional<Admin> findAdminByUsername(String username);
}
