package raf.microservice.components.userservice.dto;

import java.time.LocalDate;

public class ManagerDto {

    private long id;
    private String username;
    private String email;
    private LocalDate dateBirth;
    private String name;
    private String lastName;
    private String sportsHall;
    private LocalDate dateEmployment;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
