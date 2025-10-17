package raf.microservice.scheduletraining.mapper;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import raf.microservice.scheduletraining.domain.Appointment;
import raf.microservice.scheduletraining.domain.Gym;
import raf.microservice.scheduletraining.domain.Sport;
import raf.microservice.scheduletraining.domain.Training;
import raf.microservice.scheduletraining.dto.AppointmentDto;
import raf.microservice.scheduletraining.dto.FreeAppointmentDto;
import raf.microservice.scheduletraining.repository.GymRepository;
import raf.microservice.scheduletraining.repository.SportRepository;
import raf.microservice.scheduletraining.repository.TrainingRepository;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class AppointmentMapper {
    GymRepository gymRepository;
    SportRepository sportRepository;
    TrainingRepository trainingRepository;
    public AppointmentDto appointmentToAppointmentDto(Appointment appointment) {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setId(appointment.getId());
        appointmentDto.setClientId(appointment.getClientId());
        appointmentDto.setIndividual(appointment.getTraining().getSport().isIndividual());
        appointmentDto.setPrice(appointment.getTraining().getPrice());
        appointmentDto.setGymName(appointment.getTraining().getGym().getGymName());
        appointmentDto.setScheduledTime(appointment.getScheduledTime());
        appointmentDto.setShortDescription(appointment.getTraining().getGym().getShortDescription());
        appointmentDto.setTrainingDuration(appointment.getTraining().getGym().getTrainingDuration());
        appointmentDto.setSportName(appointment.getTraining().getSport().getSportName());
        return appointmentDto;
    }

    public Appointment appointmentDtoToAppointment(AppointmentDto appointmentDto) {
        Appointment appointment = new Appointment();
        appointment.setId(appointmentDto.getId());
        appointment.setClientId(appointmentDto.getClientId());
        appointment.setScheduledTime(appointmentDto.getScheduledTime());

        Sport s = sportRepository.findBySportName(appointmentDto.getSportName());
        Gym g = gymRepository.findByGymName(appointmentDto.getGymName());
        System.out.println(appointmentDto);
        if(g == null || s == null)throw new IllegalArgumentException("Wrong");

        Training training = trainingRepository.findTraining(g,s);

        if(training == null)throw new IllegalArgumentException("Wrong");

        appointment.setTraining(training);

        return appointment;
    }

    public Appointment updateAppo(Appointment appointment, AppointmentDto appointmentDto){
        if(appointmentDto.getSportName() != null || appointmentDto.getPrice()>-1
                || appointmentDto.getShortDescription() != null
                || appointmentDto.getTrainingDuration() >0
                || appointmentDto.getGymName() != null)
            throw new IllegalArgumentException("Cannot change non-appointment properties");
        if(appointmentDto.getClientId() != null)
           appointment.setClientId(appointmentDto.getClientId());
       if(appointmentDto.getScheduledTime() != null)
           appointment.setScheduledTime(appointmentDto.getScheduledTime());

        return appointment;
    }

    public FreeAppointmentDto makeAppointmentDto(Gym gym, LocalDateTime ldt) {
        FreeAppointmentDto dto = new FreeAppointmentDto();
        dto.setScheduledTime(ldt);
        dto.setTrainingDuration(gym.getTrainingDuration());
        dto.setName(gym.getGymName());
        dto.setShortDescription(gym.getShortDescription());
        dto.setNumberOfCoaches(dto.getNumberOfCoaches());
        dto.setId(gym.getId());
        return dto;
    }
}
