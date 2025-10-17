package raf.microservice.scheduletraining.dto;

import lombok.Data;

@Data
public class EditGymDto {

    private long id;
    private String name;
    private String shortDescription;
    private int trainingDuration;
    private int numberOfCoaches;
    private Long manager_id;

    public EditGymDto(long id, String name, String shortDescription, int trainingDuration, int numberOfCoaches, Long manager_id) {
        this.id = id;
        this.name = name;
        this.shortDescription = shortDescription;
        this.trainingDuration = trainingDuration;
        this.numberOfCoaches = numberOfCoaches;
        this.manager_id = manager_id;
    }
}
