package raf.microservice.scheduletraining.mapper;

import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import raf.microservice.scheduletraining.domain.Gym;
import raf.microservice.scheduletraining.domain.Sport;
import raf.microservice.scheduletraining.domain.Training;
import raf.microservice.scheduletraining.dto.SportDto;
import raf.microservice.scheduletraining.dto.TrainingDto;
import raf.microservice.scheduletraining.repository.GymRepository;
import raf.microservice.scheduletraining.repository.SportRepository;

@Component
public class TrainingMapper {
    SportRepository sportRepository;
    GymRepository gymRepository;

    @Autowired
    public TrainingMapper(SportRepository sportRepository, GymRepository gymRepository) {
        this.sportRepository = sportRepository;
        this.gymRepository = gymRepository;
    }

    public TrainingDto trainingToTrainingDto(Training training){
        TrainingDto trainingDto = new TrainingDto();
        trainingDto.setSportName(training.getSport().getSportName());
        trainingDto.setIndividual(training.getSport().isIndividual());
        trainingDto.setTrainingDuration(training.getGym().getTrainingDuration());
        trainingDto.setPrice(training.getPrice());
        trainingDto.setGymName(training.getGym().getGymName());
        return trainingDto ;
    }

    public Training trainingDtoToTraining(TrainingDto trainingDto){
        Training training = new Training();
        Sport s= sportRepository.findBySportName(trainingDto.getSportName());
        Gym g = gymRepository.findByGymName(trainingDto.getGymName());
        if(s == null|| g == null)throw new IllegalArgumentException("Wrong");
        training.setPrice(trainingDto.getPrice());
        training.setGym(g);
        training.setSport(s);
        return training;
    }
}
