package raf.microservice.scheduletraining.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import raf.microservice.scheduletraining.security.Divides24;

@Entity
@Data
@NoArgsConstructor
@Table(name="gym")
public class Gym {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(unique = true)
    private String gymName;
    private String shortDescription;
    @Positive
    private int numberOfCoaches;
    @Divides24
    private int trainingDuration = 6;//in hours
    private int discountAfter = 10;//discount after given number of trainings
    private Long manager_id;



    public Gym(String gymName, String shortDescription, int numberOfCoaches, int trainingDuration, Long manager_id) {
        this.gymName = gymName;
        this.shortDescription = shortDescription;
        this.numberOfCoaches = numberOfCoaches;
        this.trainingDuration = trainingDuration;
        this.manager_id = manager_id;
    }
}
