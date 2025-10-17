package raf.microservice.components.notificationservice.service;

public interface EmailService {
    void sendSimpleMessage(String emailReceiver, String name, String messageToSend);
}
