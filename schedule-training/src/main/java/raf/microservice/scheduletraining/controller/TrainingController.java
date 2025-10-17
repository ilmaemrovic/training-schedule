package raf.microservice.scheduletraining.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.microservice.scheduletraining.dto.GymDto;
import raf.microservice.scheduletraining.dto.TrainingDto;
import raf.microservice.scheduletraining.security.CheckSecurity;
import raf.microservice.scheduletraining.service.GymService;
import raf.microservice.scheduletraining.service.TrainingService;

import java.util.List;
@RestController
@RequestMapping("/trainings")
public class TrainingController {

    private TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @GetMapping
    @CheckSecurity
    public ResponseEntity<List<TrainingDto>> findAll(@RequestHeader("Authorization") String authorization) {
        return new ResponseEntity<>(trainingService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @CheckSecurity
    public ResponseEntity<TrainingDto> findById(@RequestHeader("Authorization") String authorization, @PathVariable("id") Long id) {
        return new ResponseEntity<>(trainingService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TrainingDto> add(@RequestHeader("Authorization") String authorization, @RequestBody @Valid TrainingDto trainingDto) {
        return new ResponseEntity<>(trainingService.add(trainingDto), HttpStatus.CREATED);
    }
    @PostMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<TrainingDto> changePrice(@RequestHeader("Authorization") String authorization, @RequestBody @Valid TrainingDto trainingDto, @PathVariable String id) {
        return new ResponseEntity<>(trainingService.updateById(Long.valueOf(id),trainingDto.getPrice()), HttpStatus.CREATED);
    }

}
