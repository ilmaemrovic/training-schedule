package raf.microservice.components.userservice.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "managers")
public class Manager extends Users implements UserDetails {

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

    @Column(name = "sports_hall", nullable = false, unique = true)
    private String sportsHall;

    @Column(name = "date_employment", nullable = false)
    private LocalDate dateEmployment;

    @ManyToOne(optional = false)
    private Role role;

    public Manager(){
    }

    public Manager(long id, String username, String password, String email, LocalDate dateBirth, String name,
                   String lastName, String sportsHall, LocalDate dateEmployment, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.dateBirth = dateBirth;
        this.name = name;
        this.lastName = lastName;
        this.sportsHall = sportsHall;
        this.dateEmployment = dateEmployment;
        this.role = role;
    }

    public String getUsername() {
        return username;
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

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority((role.getName())));
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

    public String getSportsHall() {
        return sportsHall;
    }

    public void setSportsHall(String sportsHall) {
        this.sportsHall = sportsHall;
    }

    public LocalDate getDateEmployment() {
        return dateEmployment;
    }

    public void setDateEmployment(LocalDate dateEmployment) {
        this.dateEmployment = dateEmployment;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
