package raf.microservice.components.userservice.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class ManagerCreateDto {

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 5, max = 16, message = "Username must be between 5 and 16 characters")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters")
    private String password;

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

    @Override
    public String toString() {
        return "ManagerCreateDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", dateBirth=" + dateBirth +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", sportsHall='" + sportsHall + '\'' +
                ", dateEmployment=" + dateEmployment +
                '}';
    }
}
