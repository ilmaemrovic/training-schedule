package raf.microservice.components.notificationservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import raf.microservice.components.notificationservice.dto.NotificationDto;
import raf.microservice.components.notificationservice.dto.TypeDto;

public interface TypeService {
    Page<TypeDto> findAll(Pageable pageable);

    TypeDto editType(Long id, TypeDto typeDto);

    void deleteType(Long id);

    TypeDto add(TypeDto typeDto);
}
