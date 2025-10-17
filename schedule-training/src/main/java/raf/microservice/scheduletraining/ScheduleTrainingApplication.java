package raf.microservice.scheduletraining;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import raf.microservice.scheduletraining.domain.Appointment;
import raf.microservice.scheduletraining.domain.Gym;
import raf.microservice.scheduletraining.domain.Sport;
import raf.microservice.scheduletraining.domain.Training;
import raf.microservice.scheduletraining.repository.AppointmentRepository;
import raf.microservice.scheduletraining.repository.GymRepository;
import raf.microservice.scheduletraining.repository.SportRepository;
import raf.microservice.scheduletraining.repository.TrainingRepository;

import java.time.LocalDateTime;

@SpringBootApplication
@Log
@AllArgsConstructor
//@Profile({"default"})
public class ScheduleTrainingApplication implements CommandLineRunner {
	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleTrainingApplication.class);
	private  GymRepository gymRepository;
	private SportRepository sportRepository;
	private TrainingRepository trainingRepository;
	private AppointmentRepository appointmentRepository;


	public static void main(String[] args) {
		SpringApplication.run(ScheduleTrainingApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			add();
		}catch (Exception e){
			LOGGER.debug("Podaci su vec dodati u bazu i nece se dodati opet", e);
		}
	}

	private void add(){
		Gym gym1 = new Gym();
		gym1.setGymName("gym1");
		gym1.setNumberOfCoaches(2);
		gym1.setShortDescription("desc1");
		Gym gym2 = new Gym();
		gym2.setGymName("gym2");
		gym2.setNumberOfCoaches(3);
		gym2.setShortDescription("desc2");

		Sport tot1 = new Sport("powerlifting",true);
		Sport tot2 = new Sport("pilates",false);
		Training tr1 = new Training(tot1,gym1,1200);
		Training tr2 = new Training(tot2,gym1,1300);
		gymRepository.save(gym1);
		gymRepository.save(gym2);

		sportRepository.save(tot1);
		sportRepository.save(tot2);

		trainingRepository.save(tr1);
		trainingRepository.save(tr2);
		Appointment a = new Appointment();
		a.setTraining(tr1);
		a.setClientId(1L);
		a.setScheduledTime(LocalDateTime.of(2024,1,2,18,0));
		appointmentRepository.save(a);
	}
}
