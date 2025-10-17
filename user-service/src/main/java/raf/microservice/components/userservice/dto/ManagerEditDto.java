package raf.microservice.components.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public class ManagerEditDto {
    @NotBlank(message = "Email cannot be empty")
    @Email
    private String email;
    @Past
    private LocalDate dateBirth;
    @NotBlank
    private String name;
    @NotBlank
    private String lastName;
    @NotBlank
    private String sportsHall;
    @PastOrPresent
    private LocalDate dateEmployment;

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
}
