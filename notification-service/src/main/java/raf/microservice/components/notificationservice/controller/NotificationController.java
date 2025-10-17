package raf.microservice.components.notificationservice.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.microservice.components.notificationservice.dto.FilterDto;
import raf.microservice.components.notificationservice.dto.NotificationDto;
import raf.microservice.components.notificationservice.service.NotificationService;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService){
        this.notificationService = notificationService;
    }

    @GetMapping("/me")
    public ResponseEntity<Page<NotificationDto>> getMyNotifications(@RequestHeader("Authorization") String authorization,
                                                                    Pageable pageable){
        return new ResponseEntity<>(notificationService.findMe(authorization, pageable) ,HttpStatus.OK);
    }

    @PostMapping("/me/filter")
    public ResponseEntity<Page<NotificationDto>> getMyNotificationsFiltered(@RequestHeader("Authorization") String authorization,
                                                                    Pageable pageable, @RequestBody @Valid FilterDto filterDto){
        return new ResponseEntity<>(notificationService.findMeFiltered(authorization, pageable, filterDto) ,HttpStatus.OK);
    }

    @PostMapping("/all")  //
    public ResponseEntity<Page<NotificationDto>> getAllNotifications(Pageable pageable, @RequestBody @Valid FilterDto filterDto){
        return new ResponseEntity<>(notificationService.findAll(pageable, filterDto) ,HttpStatus.OK);
    }
}
