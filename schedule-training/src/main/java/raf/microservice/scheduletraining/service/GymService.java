package raf.microservice.scheduletraining.service;


import org.springframework.data.domain.Pageable;
import raf.microservice.scheduletraining.dto.EditGymDto;
import raf.microservice.scheduletraining.dto.GymDto;

import java.util.List;

public interface GymService {
    GymDto add(GymDto gymDTO);
    GymDto updateById(Long gymId, GymDto gymDTO);
    GymDto findById(Long gymId);
    EditGymDto findByManagerId(Long managerId);
    List<GymDto> findAll();
    void deleteById(Long gymId);
}
