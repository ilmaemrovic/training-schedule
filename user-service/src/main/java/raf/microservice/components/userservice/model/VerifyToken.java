package raf.microservice.components.userservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "verify_tokens")
public class VerifyToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "verify_token", unique = true, nullable = false)
    public String verifyToken;

    @PrePersist
    public void generateVerifyToken() {
        if (verifyToken == null) {
            verifyToken = UUID.randomUUID().toString();
        }
    }

    @Column
    public boolean revoked;

    @Column(nullable = false)
    public String username;

    @Column
    public LocalDateTime dateValidTo;

    public VerifyToken(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVerifyToken() {
        return verifyToken;
    }

    public void setVerifyToken(String verifyToken) {
        this.verifyToken = verifyToken;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public LocalDateTime getDateValidTo() {
        return dateValidTo;
    }

    public void setDateValidTo(LocalDateTime dateValidTo) {
        this.dateValidTo = dateValidTo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "VerifyToken{" +
                "id=" + id +
                ", verifyToken='" + verifyToken + '\'' +
                ", revoked=" + revoked +
                ", dateValidTo=" + dateValidTo +
                '}';
    }
}
