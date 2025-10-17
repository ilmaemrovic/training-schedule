package raf.microservice.scheduletraining.dto;

import lombok.Data;
import raf.microservice.scheduletraining.domain.Sport;

import java.time.LocalDateTime;

@Data
public class FreeAppointmentDto {
    private Long id;
    private LocalDateTime scheduledTime;
    private String name;
    private String shortDescription;
    private int trainingDuration;
    private int numberOfCoaches;
    private Sport sport;
}
