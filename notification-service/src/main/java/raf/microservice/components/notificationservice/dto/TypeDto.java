package raf.microservice.components.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TypeDto {


    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String format;

}
