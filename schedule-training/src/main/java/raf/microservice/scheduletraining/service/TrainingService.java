package raf.microservice.scheduletraining.service;

import raf.microservice.scheduletraining.dto.TrainingDto;

import java.util.List;

public interface TrainingService {
    TrainingDto add(TrainingDto trainingDTO);
    TrainingDto findById(Long Id);
    List<TrainingDto> findAll();
    void deleteById(Long id);
    TrainingDto updateById(Long id, int price);

}
