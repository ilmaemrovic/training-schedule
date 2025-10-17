package raf.microservice.scheduletraining.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ClientDto {

    private String username;
    private String email;
    private LocalDate dateBirth;
    private String name;
    private String lastName;
    private String membershipCardId;
    private int scheduledTrainingCount;
}
