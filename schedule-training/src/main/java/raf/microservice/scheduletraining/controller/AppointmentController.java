package raf.microservice.scheduletraining.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import raf.microservice.scheduletraining.domain.Appointment;
import raf.microservice.scheduletraining.dto.AppointmentDto;
import raf.microservice.scheduletraining.dto.ClientIdDto;
import raf.microservice.scheduletraining.dto.FreeAppointmentDto;
import raf.microservice.scheduletraining.dto.GymDto;
import raf.microservice.scheduletraining.security.CheckSecurity;
import raf.microservice.scheduletraining.service.AppointmentService;
import raf.microservice.scheduletraining.service.impl.AppointmentServiceImpl;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }
    @GetMapping
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<List<AppointmentDto>> findAllReserved(@RequestHeader("Authorization") String authorization) {
        return new ResponseEntity<>(appointmentService.findAllReserved(),HttpStatus.OK);
    }
    @GetMapping("/free")
    @CheckSecurity
    public ResponseEntity<List<FreeAppointmentDto>> findAllFree(@RequestHeader("Authorization") String authorization) {
        return new ResponseEntity<>(appointmentService.findAllFree(), HttpStatus.OK);
    }

    @GetMapping("/manager/{id}")
    @CheckSecurity
    public ResponseEntity<List<AppointmentDto>> managerAppointments(@RequestHeader("Authorization") String authorization,@PathVariable("id") Long id) {
        return new ResponseEntity<>(appointmentService.findAllReservedForManager(id), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @CheckSecurity
    public ResponseEntity<AppointmentDto> findById(@RequestHeader("Authorization") String authorization, @PathVariable("id") Long id) {
        return new ResponseEntity<>(appointmentService.findById(id), HttpStatus.OK);
    }
    @PostMapping("/type/{val}")
    @CheckSecurity
    public ResponseEntity<List<AppointmentDto>>filterByType(@RequestHeader("Authorization") String authorization, @PathVariable String val,
                                                            @RequestBody @Valid Long id) {
        return new ResponseEntity<>(appointmentService.filterByType(Boolean.parseBoolean(val), id), HttpStatus.OK);
    }
    @GetMapping("/day/{val}")
    @CheckSecurity
    public ResponseEntity<List<FreeAppointmentDto>>filterByDay(@RequestHeader("Authorization") String authorization, @PathVariable String val) {
        return new ResponseEntity<>(appointmentService.filterByDay(val), HttpStatus.OK);
    }
    @GetMapping("/sort")
    @CheckSecurity
    public ResponseEntity<List<FreeAppointmentDto>> sortByTime(@RequestHeader("Authorization") String authorization) {
        return new ResponseEntity<>(appointmentService.sortByTime(), HttpStatus.OK);
    }
    @GetMapping("/client/{id}")
    @CheckSecurity
    public ResponseEntity<List<AppointmentDto>> clientAppointments(@RequestHeader("Authorization") String authorization,@PathVariable("id") Long id) {
        return new ResponseEntity<>(appointmentService.findAllForClientId(id), HttpStatus.OK);
    }

    @PostMapping("/add/sport")
    @CheckSecurity(roles = {"ROLE_ADMIN", "ROLE_CLIENT"})
    public ResponseEntity<AppointmentDto> add(@RequestHeader("Authorization") String authorization, @RequestBody @Valid AppointmentDto appointmentDto) {
        return new ResponseEntity<>(appointmentService.add(appointmentDto,authorization), HttpStatus.CREATED);
    }
    @PostMapping("/add")
    @CheckSecurity(roles = {"ROLE_ADMIN", "ROLE_CLIENT"})
    public ResponseEntity<AppointmentDto> addWithSport(@RequestHeader("Authorization") String authorization, @RequestBody @Valid AppointmentDto appointmentDto) {
        return new ResponseEntity<>(appointmentService.addWithSport(appointmentDto,authorization), HttpStatus.CREATED);
    }
    @PostMapping("/{id}")
    @CheckSecurity(roles = "ROLE_ADMIN")
    public ResponseEntity<AppointmentDto> updateById(@RequestHeader("Authorization") String authorization, @RequestBody @Valid AppointmentDto appointmentDto,  @PathVariable String id) {
        return new ResponseEntity<>(appointmentService.updateById(Long.valueOf(id),appointmentDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @CheckSecurity(roles = "ROLE_CLIENT")
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String authorization, @PathVariable("id") Long id,
                                    @RequestBody @Valid ClientIdDto clientIdDto) {
        appointmentService.deleteById(id,authorization, clientIdDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    @CheckSecurity(roles ="ROLE_MANAGER")
    public ResponseEntity<?> deleteForManager(@RequestHeader("Authorization") String authorization, @PathVariable("id") Long id,
    @RequestBody @Valid ClientIdDto clientIdDto) {
        appointmentService.cancelForManager(id, clientIdDto, authorization);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
