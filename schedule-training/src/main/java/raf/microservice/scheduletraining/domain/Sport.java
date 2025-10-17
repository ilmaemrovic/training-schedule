package raf.microservice.scheduletraining.domain;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name="sport")
public class Sport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String sportName;
    private boolean individual;

    public Sport(String type, boolean individual) {
        this.sportName = type;
        this.individual = individual;
    }
}
