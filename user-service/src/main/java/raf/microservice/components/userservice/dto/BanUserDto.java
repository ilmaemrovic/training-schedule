package raf.microservice.components.userservice.dto;

public class BanUserDto {
    ClientDto user;

    boolean banned;

    public ClientDto getUser() {
        return user;
    }

    public void setUser(ClientDto user) {
        this.user = user;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }
}
