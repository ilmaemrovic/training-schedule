package raf.microservice.components.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FilterDto {

    @JsonProperty("type_name")
    private String type;

    @JsonProperty("email")
    private String email;

    @JsonProperty("date_from")
    private LocalDateTime dateFrom;

    @JsonProperty("date_to")
    private LocalDateTime dateTo;

}
