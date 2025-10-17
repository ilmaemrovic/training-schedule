package raf.microservice.components.notificationservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import raf.microservice.components.notificationservice.dto.FilterDto;
import raf.microservice.components.notificationservice.dto.NotificationDto;

public interface NotificationService {
    Page<NotificationDto> findAll(Pageable pageable, FilterDto filterDto);

    Page<NotificationDto> findMe(String authorization, Pageable pageable);

    Page<NotificationDto> findMeFiltered(String authorization, Pageable pageable, FilterDto filterDto);
}
