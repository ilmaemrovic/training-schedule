package raf.microservice.scheduletraining.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import raf.microservice.scheduletraining.domain.Gym;
import raf.microservice.scheduletraining.domain.Training;
import raf.microservice.scheduletraining.dto.TrainingDto;
import raf.microservice.scheduletraining.mapper.TrainingMapper;
import raf.microservice.scheduletraining.repository.TrainingRepository;
import raf.microservice.scheduletraining.service.TrainingService;

import java.util.List;

@AllArgsConstructor
@Service
public class TrainingServisImpl implements TrainingService {

    TrainingRepository trainingRepository;
    TrainingMapper trainingMapper;
    @Override
    public TrainingDto add(TrainingDto trainingDTO) {
        Training training = trainingMapper.trainingDtoToTraining(trainingDTO);
        trainingRepository.save(training);
        return trainingMapper.trainingToTrainingDto(training);
    }

    @Override
    public TrainingDto findById(Long Id) {
        return trainingRepository.findById(Id)
                .map(trainingMapper::trainingToTrainingDto)
                .orElseThrow(() ->
                        new RuntimeException(String.format
                                ("Gym with id: %d does not exists.", Id)));
    }

    @Override
    public List<TrainingDto> findAll() {
        return trainingRepository.findAll().stream().map(trainingMapper::trainingToTrainingDto).toList();
    }

    @Override
    public void deleteById(Long id) {
        trainingRepository.deleteById(id);
    }

    @Override
    public TrainingDto updateById(Long id, int price) {
        Training training = trainingRepository.findById(id).orElseThrow(() ->
                new RuntimeException(String.format
                        ("Training with id: %d does not exists.", id)));
        training.setPrice(price);
        trainingRepository.save(training);
        return trainingMapper.trainingToTrainingDto(training);
    }
}
