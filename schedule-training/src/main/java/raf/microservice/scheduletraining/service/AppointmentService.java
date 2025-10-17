package raf.microservice.scheduletraining.service;

import org.springframework.stereotype.Service;
import raf.microservice.scheduletraining.dto.AppointmentDto;
import raf.microservice.scheduletraining.dto.ClientIdDto;
import raf.microservice.scheduletraining.dto.FreeAppointmentDto;

import java.util.List;

@Service
public interface AppointmentService {
    AppointmentDto add(AppointmentDto apDTO, String aut);
    AppointmentDto addWithSport(AppointmentDto apDTO, String aut);
    AppointmentDto updateById(Long apId, AppointmentDto apDTO);
    AppointmentDto findById(Long apId);
    List<AppointmentDto> findAllReserved();
    List<FreeAppointmentDto> findAllFree();
    List<AppointmentDto> filterByType(boolean individual, Long id);
    List<FreeAppointmentDto> filterByDay(String day);
    List<FreeAppointmentDto> sortByTime();
    void cancelForManager(Long id, ClientIdDto clientIdDto, String auth);
    void deleteById(Long apId, String aut, ClientIdDto clientIdDto);
    List<AppointmentDto> findAllForClientId(Long id);

    List<AppointmentDto>findAllReservedForManager(Long id);


}
