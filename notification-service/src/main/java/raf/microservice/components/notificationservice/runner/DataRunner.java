package raf.microservice.components.notificationservice.runner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import raf.microservice.components.notificationservice.dto.TransferDto;
import raf.microservice.components.notificationservice.listener.helper.MessageHelper;
import raf.microservice.components.notificationservice.mapper.model.Notification;
import raf.microservice.components.notificationservice.mapper.model.Type;
import raf.microservice.components.notificationservice.repository.NotificationRepository;
import raf.microservice.components.notificationservice.repository.TypeRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;

@Profile({"default"})
@Component
public class DataRunner implements CommandLineRunner {

    private final TypeRepository typeRepository;
    private final NotificationRepository notificationRepository;
    private final JmsTemplate jmsTemplate;
    private final String sendEmailDestination;
    private final MessageHelper messageHelper;

    public DataRunner(TypeRepository typeRepository, NotificationRepository notificationRepository
    , JmsTemplate jmsTemplate, @Value("${destination.sendEmails}") String sendEmailDestination,
                      MessageHelper messageHelper){
        this.typeRepository = typeRepository;
        this.notificationRepository = notificationRepository;
        this.jmsTemplate = jmsTemplate;
        this.sendEmailDestination = sendEmailDestination;
        this.messageHelper = messageHelper;
    }

    @Override
    public void run(String... args) throws Exception {
//        Type type = new Type();
//        type.setName("REGISTER_USER");
//        type.setFormat("Hello %name% %lastname% ! To verify your account please open following link %link%");
//        typeRepository.save(type);
//
//            Type type1 = new Type();
//            type1.setName("CANCELED_TR");
//            type1.setFormat("Hello! Training on %date% is canceled.");
//
//            typeRepository.save(type1);
////
//        Type type2 = new Type();
//        type2.setName("SCHED_TR");
//        type2.setFormat("Training scheduled successfully!");
//        typeRepository.save(type2);
//
//        Type type3 = new Type();
//        type3.setName("CHANGED_PASSWORD");
//        type3.setFormat("Hello %name% %lastname% ! Someone just logged in to your account.");
//        typeRepository.save(type3);
//
//        Notification notification = new Notification();
//        notification.setMessage("Poruka");
//        notification.setMailReceiver("karisik.mehmedalija@gmail.com");
//        notification.setType(type);
//        notification.setUsername("karisikm");
//        notification.setDateSent(LocalDateTime.of(2023, Month.DECEMBER, 31, 20, 5, 55));
//        notificationRepository.save(notification);
//
//        Notification notification1 = new Notification();
//        notification1.setMessage("Poruka2232");
//        notification1.setMailReceiver("karisik.mehmedalija@gmail.com");
//        notification1.setType(type);
//        notification1.setUsername("karisikm");
//        notification1.setDateSent(LocalDateTime.of(2023, Month.DECEMBER, 30, 14, 33, 22));
//        notificationRepository.save(notification1);
//
//        Notification notification2 = new Notification();
//        notification2.setMessage("Poruka2232");
//        notification2.setMailReceiver("karisik.mehmedalija@gmail.com");
//        notification2.setType(type);
//        notification2.setUsername("mking03321122");
//        notification2.setDateSent(LocalDateTime.of(2023, Month.DECEMBER, 29, 14, 33, 11));
//        notificationRepository.save(notification2);

//        HashMap<String, String> paramsMap = new HashMap<>();
//        paramsMap.put("%name%", "Mehmedalija");
//        paramsMap.put("%lastname%", "Karisik");
//        paramsMap.put("%link%", "/link.com");
////        type.setFormat("Hello %name% %lastname% ! To verify your account please open following link %link%");
//        TransferDto transferDto = new TransferDto("karisik.mehmedalija@gmail.com", "REGISTER_USER", paramsMap, "john_doe");
//
//
//        jmsTemplate.convertAndSend(sendEmailDestination, messageHelper.createTextMessage(transferDto));

    }
}
