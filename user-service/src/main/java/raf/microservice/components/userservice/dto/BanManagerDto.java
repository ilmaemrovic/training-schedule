package raf.microservice.components.userservice.dto;

public class BanManagerDto {

    private ManagerDto manager;
    private boolean banned;

    public ManagerDto getManager() {
        return manager;
    }

    public void setManager(ManagerDto manager) {
        this.manager = manager;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }
}
