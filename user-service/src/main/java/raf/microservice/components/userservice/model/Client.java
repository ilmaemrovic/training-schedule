package raf.microservice.components.userservice.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "clients")
public class Client extends Users implements UserDetails {

    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "date_birth", nullable = false)
    private LocalDate dateBirth;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "membership_card_id", unique = true, nullable = false)
    private String membershipCardId;

    @PrePersist
    public void generateMembershipCardId() {
        if (membershipCardId == null) {
            membershipCardId = UUID.randomUUID().toString();
        }
    }

    @Column(name="scheduled_training_count", nullable = false, columnDefinition = "integer default 0")
    private Integer scheduledTrainingCount = 0;
    @ManyToOne(optional = false)
    private Role role;

    public Client(long id, String username, String password, String email, LocalDate dateBirth, String name, String lastName,
                  String membershipCardId, Integer scheduledTrainingCount, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.dateBirth = dateBirth;
        this.name = name;
        this.lastName = lastName;
        this.membershipCardId = membershipCardId;
        this.scheduledTrainingCount = scheduledTrainingCount;
        this.role = role;
    }

    public Client(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(LocalDate dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMembershipCardId() {
        return membershipCardId;
    }

    public void setMembershipCardId(String membershipCardId) {
        this.membershipCardId = membershipCardId;
    }

    public Integer getScheduledTrainingCount() {
        return scheduledTrainingCount;
    }

    public void setScheduledTrainingCount(Integer scheduledTrainingCount) {
        this.scheduledTrainingCount = scheduledTrainingCount;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority((role.getName())));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
