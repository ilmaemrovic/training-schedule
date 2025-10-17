package raf.microservice.scheduletraining.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import raf.microservice.scheduletraining.domain.Gym;
import raf.microservice.scheduletraining.domain.Sport;
import raf.microservice.scheduletraining.domain.Training;

public interface TrainingRepository extends JpaRepository<Training, Long> {

    @Query("SELECT t FROM Training t WHERE t.gym = :gym AND t.sport = :sport")
    Training findTraining(Gym gym, Sport sport);

}
