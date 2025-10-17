package raf.microservice.components.notificationservice.mapper.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "mail_receiver", nullable = false)
    private String mailReceiver;

    @ManyToOne(optional = false)
    private Type type;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "date_sent", nullable = false)
    private LocalDateTime dateSent;

    public Notification(){}

}
