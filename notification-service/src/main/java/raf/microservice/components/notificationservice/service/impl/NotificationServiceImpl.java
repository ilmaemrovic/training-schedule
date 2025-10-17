package raf.microservice.components.notificationservice.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.microservice.components.notificationservice.dto.FilterDto;
import raf.microservice.components.notificationservice.dto.NotificationDto;
import raf.microservice.components.notificationservice.exceptions.NotFoundException;
import raf.microservice.components.notificationservice.mapper.NotificationMapper;
import raf.microservice.components.notificationservice.mapper.model.Notification;
import raf.microservice.components.notificationservice.mapper.model.Type;
import raf.microservice.components.notificationservice.repository.NotificationRepository;
import raf.microservice.components.notificationservice.repository.TypeRepository;
import raf.microservice.components.notificationservice.security.services.JwtService;
import raf.microservice.components.notificationservice.service.NotificationService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final JwtService jwtService;
    private final TypeRepository typeRepository;
    public NotificationServiceImpl(NotificationRepository notificationRepository, NotificationMapper notificationMapper,
                                   JwtService jwtService, TypeRepository typeRepository){
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.jwtService = jwtService;
        this.typeRepository = typeRepository;
    }


    @Override
    public Page<NotificationDto> findAll(Pageable pageable, FilterDto filterDto) {

        Long type = null;
        Optional<Type> typeOptional = typeRepository.findTypeByName(filterDto.getType());

        if(typeOptional.isPresent()) type = typeOptional.get().getId();
        if(filterDto.getType() != null && typeOptional.isEmpty())
            throw new NotFoundException("Notification list not found");

        Page<Notification> notificationList = notificationRepository.findByFiltersAll(type, filterDto.getEmail(),
                filterDto.getDateFrom(), filterDto.getDateTo(), pageable);


        if(notificationList.isEmpty())  throw new NotFoundException("Notification list not found");

        List<NotificationDto> notificationDtoList = notificationList
                .stream()
                .map(notificationMapper::notificationToNotificationDto).collect(Collectors.toList());

        return new PageImpl<>(notificationDtoList, pageable, notificationList.getTotalElements());
    }

    @Override
    public Page<NotificationDto> findMe(String authorization, Pageable pageable) {
        String token = authorization.substring(7);

        Page<Notification> notificationList = notificationRepository.findNotificationByUsername(jwtService.extractUsername(token), pageable);

        if(notificationList.isEmpty()) throw new NotFoundException("Notification not found");

        List<NotificationDto> notificationDtoList = notificationList
                .stream()
                .map(notificationMapper::notificationToNotificationDto).collect(Collectors.toList());

        return new PageImpl<>(notificationDtoList, pageable, notificationList.getTotalElements());

    }

    @Override
    public Page<NotificationDto> findMeFiltered(String authorization, Pageable pageable, FilterDto filterDto) {

        Long type = null;
        String token = authorization.substring(7);

        Optional<Type> typeOptional = typeRepository.findTypeByName(filterDto.getType());

        if(typeOptional.isPresent()) type = typeOptional.get().getId();
        if(filterDto.getType() != null && typeOptional.isEmpty())
            throw new NotFoundException("Notification list not found");

        Page<Notification> notificationList = notificationRepository.findByFilters(jwtService.extractUsername(token),
                type, filterDto.getEmail(), filterDto.getDateFrom(), filterDto.getDateTo(), pageable);


        if(notificationList.isEmpty())  throw new NotFoundException("Notification list not found");

        List<NotificationDto> notificationDtoList = notificationList
                .stream()
                .map(notificationMapper::notificationToNotificationDto).collect(Collectors.toList());

        return new PageImpl<>(notificationDtoList, pageable, notificationList.getTotalElements());
    }
}
