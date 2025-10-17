package raf.microservice.scheduletraining.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class AppointmentDto {
    private Long id;
    private LocalDateTime scheduledTime;
    private String sportName;
    private boolean individual;
    private String gymName;
    private String shortDescription;
    private int trainingDuration;
    private int price = -1;
    private Long clientId;
}
