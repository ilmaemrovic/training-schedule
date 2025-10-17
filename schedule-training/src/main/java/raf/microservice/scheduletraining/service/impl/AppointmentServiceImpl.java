package raf.microservice.scheduletraining.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import raf.microservice.scheduletraining.domain.Appointment;
import raf.microservice.scheduletraining.domain.Gym;
import raf.microservice.scheduletraining.domain.Sport;
import raf.microservice.scheduletraining.domain.Training;
import raf.microservice.scheduletraining.dto.*;
import raf.microservice.scheduletraining.helper.MessageHelper;
import raf.microservice.scheduletraining.mapper.AppointmentMapper;
import raf.microservice.scheduletraining.repository.AppointmentRepository;
import raf.microservice.scheduletraining.repository.GymRepository;
import raf.microservice.scheduletraining.repository.SportRepository;
import raf.microservice.scheduletraining.security.ParseHelper;
import raf.microservice.scheduletraining.service.AppointmentService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Service
public class AppointmentServiceImpl implements AppointmentService {

    private AppointmentRepository appointmentRepository;
    private AppointmentMapper appointmentMapper;
    private GymRepository gymRepository;
    private SportRepository sportRepository;
    private RestTemplate userServiceRestTemplate;
    private JmsTemplate jmsTemplate;
    private MessageHelper messageHelper;
    @Value("${secret.help}")
    private String sh;
    private RetryTemplate retryTemplate;


    @Override
    public AppointmentDto add(AppointmentDto apDTO, String aut) {
        Appointment appointment = appointmentMapper.appointmentDtoToAppointment(apDTO);
        if(appointment.getScheduledTime().isAfter(LocalDateTime.now().plusWeeks(2)))
            throw new IllegalArgumentException("You can't schedule that much earlier!");
        if(appointmentRepository.findAppointmentByTimeAndGym(appointment.getScheduledTime(),appointment.getTraining().getGym()).size()!=0)
            throw new IllegalArgumentException("You can't schedule taken appointment!");


        ResponseEntity<ClientDto> res = retryTemplate.execute(x-> {
            HttpHeaders hh = new HttpHeaders();
            hh.add("Authorization",aut);
            return userServiceRestTemplate.exchange
                    ("/client/me", HttpMethod.GET, new HttpEntity<>(hh), ClientDto.class);
        });

        int discount =  Objects.requireNonNull(res.getBody()).getScheduledTrainingCount();
        discount++;
        if (discount % appointment.getTraining().getGym().getDiscountAfter() == 0)
            appointment.getTraining().setPrice(0);
        appointmentRepository.save(appointment);
        res.getBody().setScheduledTrainingCount(discount);
        HttpHeaders hh = new HttpHeaders();

        hh.add("Authorization","Bearer " + sh);
        userServiceRestTemplate.exchange
                ("/client/edit/training-count",HttpMethod.PUT,new HttpEntity<>(res.getBody()),ClientDto.class);


        TransferDto transferDto = new TransferDto(res.getBody().getEmail(),"SCHED_TR", new HashMap<>(),
                res.getBody().getUsername());

        jmsTemplate.convertAndSend("send_mail_destination",
                messageHelper.createTextMessage(transferDto));

        return appointmentMapper.appointmentToAppointmentDto(appointment);
    }

    @Override
    public AppointmentDto addWithSport(AppointmentDto apDTO, String aut) {
        Appointment appointment = appointmentMapper.appointmentDtoToAppointment(apDTO);
        List<Appointment> tmp = appointmentRepository.findAppointmentByTimeAndGym(appointment.getScheduledTime(),appointment.getTraining().getGym());
        Appointment tmpp = tmp.get(0);
        Sport t = tmpp.getTraining().getSport();
        appointment.getTraining().setSport(t);
        if(appointment.getScheduledTime().isAfter(LocalDateTime.now().plusWeeks(2)))
            throw new IllegalArgumentException("You can't schedule that much earlier!");
//        if(appointmentRepository.findAppointmentByTimeAndGym(appointment.getScheduledTime(),appointment.getTraining().getGym()) != null)
//            throw new IllegalArgumentException("You can't schedule taken appointment!");

        HttpHeaders hh = new HttpHeaders();
        hh.add("Authorization",aut);
        ResponseEntity<ClientDto> res =  userServiceRestTemplate.exchange
                ("/client/me",HttpMethod.GET,new HttpEntity<>(hh),ClientDto.class);

        int discount =  Objects.requireNonNull(res.getBody()).getScheduledTrainingCount();
        discount++;
        if (discount % appointment.getTraining().getGym().getDiscountAfter() == 0)
            appointment.getTraining().setPrice(0);
        appointmentRepository.save(appointment);
        res.getBody().setScheduledTrainingCount(discount);
        hh = new HttpHeaders();

        hh.add("Authorization","Bearer " + sh);
        userServiceRestTemplate.exchange
                ("/client/edit/training-count",HttpMethod.PUT,new HttpEntity<>(res.getBody()),ClientDto.class);


        TransferDto transferDto = new TransferDto(res.getBody().getEmail(),"SCHED_TR", new HashMap<>(),
                res.getBody().getUsername());

        jmsTemplate.convertAndSend("send_mail_destination",
                messageHelper.createTextMessage(transferDto));

        return appointmentMapper.appointmentToAppointmentDto(appointment);
    }

    @Override
    public AppointmentDto updateById(Long apId, AppointmentDto apDTO) {
        Appointment appointment = appointmentRepository.findById(apId).orElseThrow(() ->
                new RuntimeException(String.format
                        ("Appointment with id: %d does not exists.", apId)));
        appointment = appointmentMapper.updateAppo(appointment,apDTO);
        appointmentRepository.save(appointment);
        return appointmentMapper.appointmentToAppointmentDto(appointment);
    }

    @Override
    public AppointmentDto findById(Long apId) {
        return appointmentRepository.findById(apId)
                .map(appointmentMapper::appointmentToAppointmentDto)
                .orElseThrow(() ->
                        new RuntimeException(String.format
                                ("Gym with id: %d does not exists.", apId)));
    }

    @Override
    public List<AppointmentDto> findAllReserved() {
        LocalDateTime ldt = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        List<Appointment> apps = appointmentRepository.findAppointmentsByScheduledTimeAfter(ldt);
        List<AppointmentDto> dtList = new ArrayList<>();
        for(Appointment a: apps){
            System.out.println(a);
            if(!ldt.plusDays(1).isBefore(a.getScheduledTime()) &&
                    !a.getTraining().getSport().isIndividual() &&
                    appointmentRepository.findAppointmentsByGroupTraining(a.getScheduledTime(),a.getTraining().getGym())<3)
                a.setCanceled(true);
            if(a.isCanceled())
                continue;
            dtList.add(appointmentMapper.appointmentToAppointmentDto(a));
        }

        return dtList;
    }

    @Override
    public List<FreeAppointmentDto> findAllFree() {
        LocalDateTime ldt = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        LocalDateTime now = ldt;
        LocalDateTime end = ldt.plusWeeks(2);
        List<FreeAppointmentDto> free = new ArrayList<>();
        List<Gym> allGyms = gymRepository.findAll();
        long c = 0;
        for(Gym gym: allGyms){
            while(ldt.isBefore(end)){
                int duration = gym.getTrainingDuration();
                int h = ldt.getHour() - ldt.getHour()%duration;
                ldt = ldt.withHour(h);
                for(int i = 0; i< 24 ; i+=duration, ldt = ldt.plusHours(duration)){
                    Appointment tmp = null;
                    FreeAppointmentDto dto = appointmentMapper.makeAppointmentDto(gym, ldt);
                    List<Appointment> tmpp = appointmentRepository.findAppointmentByTimeAndGym(ldt,gym);
                    if(!tmpp.isEmpty())
                        tmp = tmpp.get(0);

                    if(tmp != null){
                        if(tmp.getTraining().getSport().isIndividual())
                            continue;

                        if(appointmentRepository.findAppointmentsByGroupTraining(ldt,gym) == 12)
                            continue;
                        if(appointmentRepository.findAppointmentsByGroupTraining(ldt,gym) > 0){
                            Sport sport = tmp.getTraining().getSport();
                            dto.setSport(sport);
                        }
                    }


                    dto.setId(c++);
                    free.add(dto);
                }
            }
            ldt = now;
        }
        return free;
    }

    @Override
    public List<AppointmentDto> filterByType(boolean individual,Long id) {
        List<AppointmentDto> dtList = new ArrayList<>();
        List<Appointment> apps = appointmentRepository.findAllByClientId(id);
        for(Appointment a: apps){
            if(a.isCanceled()) continue;
            if(a.getTraining().getSport().isIndividual() == individual)
                dtList.add(appointmentMapper.appointmentToAppointmentDto(a));
        }
        return dtList;
    }

    @Override
    public List<FreeAppointmentDto> filterByDay(String day) {
        List<FreeAppointmentDto> dtList = new ArrayList<>();
        List<FreeAppointmentDto> apps = findAllFree();
        for(FreeAppointmentDto a: apps){
            if(a.getScheduledTime().getDayOfWeek().name().equalsIgnoreCase(day))
                dtList.add(a);
        }
        return dtList;
    }

    @Override
    public List<FreeAppointmentDto> sortByTime() {
        List<FreeAppointmentDto> dtList = new ArrayList<>();
        List<FreeAppointmentDto> apps =this.findAllFree();

        //for(Appointment a: apps){
            //dtList.add(appointmentMapper.appointmentToAppointmentDto(a));
       // }
        return apps;
    }

    @Override
    public void cancelForManager(Long id, ClientIdDto clientIdDto, String aut) {
        Long idM = clientIdDto.getId();
        List<Appointment> appointmentList = appointmentRepository.findAllReservedForManager(idM);
        Appointment a = appointmentRepository.findById(id).orElseThrow();
        Training trainingA = a.getTraining();
        LocalDateTime scheduledTime = a.getScheduledTime();
        List<Appointment> allForDelete = appointmentRepository.findAppsForTrainingAndTime(trainingA,scheduledTime);
        System.out.println(allForDelete);
        for(Appointment appointment: allForDelete) {
            if (!appointmentList.isEmpty() && appointmentList.contains(appointment)) {
                a.setCanceled(true);
                appointmentRepository.save(a);
            }
        }

            HttpHeaders hh = new HttpHeaders();
            hh.add("Authorization",aut);
            for(Appointment idClient: allForDelete){
                Long ids = idClient.getClientId();
                ResponseEntity<ClientDto> res =  userServiceRestTemplate.exchange
                        ("/client/"+ids,HttpMethod.GET,new HttpEntity<>(hh),ClientDto.class);

                HashMap<String, String> paramsMap = new HashMap<>();
                paramsMap.put("%date%", idClient.getScheduledTime().toString());
            TransferDto transferDto = new TransferDto(res.getBody().getEmail(),"CANCELED_TR", paramsMap,
                    res.getBody().getUsername());

            jmsTemplate.convertAndSend("send_mail_destination",
                    messageHelper.createTextMessage(transferDto));

                int tr = res.getBody().getScheduledTrainingCount();
                res.getBody().setScheduledTrainingCount(--tr);
                userServiceRestTemplate.exchange
                        ("/client/edit/training-count",HttpMethod.PUT,new HttpEntity<>(res.getBody()),ClientDto.class);
        }
    }

    @Override
    public void deleteById(Long apId, String aut, ClientIdDto clientIdDto) {
        Long id = clientIdDto.getId();
        System.out.println(id);
        List<Appointment> appointmentList = appointmentRepository.findAllByClientId(id);
        System.out.println(appointmentList);

        Appointment a = appointmentRepository.findById(apId).orElseThrow();
        if(!appointmentList.isEmpty() && appointmentList.contains(a)){
            appointmentRepository.deleteById(apId);

            HttpHeaders hh = new HttpHeaders();
            hh.add("Authorization",aut);

            ResponseEntity<ClientDto> res =  userServiceRestTemplate.exchange
                    ("/client/"+id,HttpMethod.GET,new HttpEntity<>(hh),ClientDto.class);

            HashMap<String, String> paramsMap = new HashMap<>();
            paramsMap.put("%date%", a.getScheduledTime().toString());
            TransferDto transferDto = new TransferDto(res.getBody().getEmail(),"CANCELED_TR", paramsMap,
                    res.getBody().getUsername());

            jmsTemplate.convertAndSend("send_mail_destination",
                    messageHelper.createTextMessage(transferDto));

            int tr = res.getBody().getScheduledTrainingCount();
            res.getBody().setScheduledTrainingCount(--tr);
            userServiceRestTemplate.exchange
                    ("/client/edit/training-count",HttpMethod.PUT,new HttpEntity<>(res.getBody()),ClientDto.class);
        }

    }

    @Override
    public List<AppointmentDto> findAllForClientId(Long id) {

        List<Appointment> apps =  appointmentRepository.findAllAppointmentsForClient(id);
        List<AppointmentDto>dtList = new ArrayList<>();
        for(Appointment a: apps){
            if(a.isCanceled()) continue;
            dtList.add(appointmentMapper.appointmentToAppointmentDto(a));
        }

        return dtList;
    }

    @Override
    public List<AppointmentDto> findAllReservedForManager(Long id) {
        List<AppointmentDto>dts = new ArrayList<>();
        List<Appointment>mngList = appointmentRepository.findAllReservedForManager(id);
        for(Appointment a: mngList){
            if(a.isCanceled()) continue;
            dts.add(appointmentMapper.appointmentToAppointmentDto(a));
        }

        return dts;
    }


    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.MINUTES)
    public void lessThen24Hours(){
        LocalDateTime now = LocalDateTime.now();
        for(Appointment tmp: appointmentRepository.findAll()){
            if(tmp.isCanceled())
                continue;
            if(!now.plusDays(1).isBefore(tmp.getScheduledTime()) &&
                    !tmp.getTraining().getSport().isIndividual() &&
                    appointmentRepository.findAppointmentsByGroupTraining(tmp.getScheduledTime(),tmp.getTraining().getGym())<3){
                tmp.setCanceled(true);

                Long ids = tmp.getClientId();
                HttpHeaders hh = new HttpHeaders();
                hh.add("Authorization",sh);
                ResponseEntity<ClientDto> res =  userServiceRestTemplate.exchange
                        ("/client/"+ids,HttpMethod.GET,new HttpEntity<>(hh),ClientDto.class);

                HashMap<String, String> paramsMap = new HashMap<>();
                paramsMap.put("%date%", tmp.getScheduledTime().toString());
                TransferDto transferDto = new TransferDto(res.getBody().getEmail(),"CANCELED_TR", paramsMap,
                        res.getBody().getUsername());

                jmsTemplate.convertAndSend("send_mail_destination",
                        messageHelper.createTextMessage(transferDto));
                int tr = res.getBody().getScheduledTrainingCount();
                res.getBody().setScheduledTrainingCount(--tr);
                userServiceRestTemplate.exchange
                        ("/client/edit/training-count",HttpMethod.PUT,new HttpEntity<>(res.getBody()),ClientDto.class);
            }

        }

    }
}
