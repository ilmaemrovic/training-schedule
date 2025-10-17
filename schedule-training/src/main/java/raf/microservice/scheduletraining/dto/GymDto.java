package raf.microservice.scheduletraining.dto;

import lombok.Data;
@Data
public class GymDto {

    private String name;
    private String shortDescription;
    private int trainingDuration;
    private int numberOfCoaches;
    private Long manager_id;

}
