package raf.microservice.components.userservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class MessageHelper {

    private final ObjectMapper objectMapper;

    public MessageHelper(Validator validator, ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    public String createTextMessage(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Problem with creating text message");
        }
    }
}
