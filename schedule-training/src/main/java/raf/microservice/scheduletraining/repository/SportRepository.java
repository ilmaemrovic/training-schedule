package raf.microservice.scheduletraining.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import raf.microservice.scheduletraining.domain.Sport;

import java.util.Optional;

public interface SportRepository extends JpaRepository<Sport, Long> {


    Sport findBySportName(String sportName);
}
