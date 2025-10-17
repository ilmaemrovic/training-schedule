package raf.microservice.scheduletraining.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import raf.microservice.scheduletraining.domain.Gym;

import java.util.Optional;


public interface GymRepository extends JpaRepository<Gym, Long> {


    Gym findByGymName(String gymName);
    @Query("SELECT g FROM Gym g WHERE g.manager_id = :managerId")
    Optional<Gym> findGymByManagerId(@Param("managerId") Long managerId);
}
