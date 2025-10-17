package raf.microservice.components.notificationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.microservice.components.notificationservice.mapper.model.Type;

import java.util.Optional;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {
    Optional<Type> findTypeByName(String name);
}
