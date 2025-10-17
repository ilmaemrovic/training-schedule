package raf.microservice.components.userservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(unique = true)
    public String token;

    @Column
    public boolean revoked;

    @Column
    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public Users usersDetails;

    public Token(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public Users getUsersDetails() {
        return usersDetails;
    }

    public void setUsersDetails(Users usersDetails) {
        this.usersDetails = usersDetails;
    }
}
