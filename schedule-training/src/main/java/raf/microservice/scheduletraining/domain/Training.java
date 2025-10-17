package raf.microservice.scheduletraining.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "training")
@NoArgsConstructor
@Data
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Sport sport;
    @ManyToOne
    private Gym gym;
    @PositiveOrZero
    private int price;

    public Training(Sport sport, Gym gym, int price) {
        this.sport = sport;
        this.gym = gym;
        this.price = price;
    }
}
