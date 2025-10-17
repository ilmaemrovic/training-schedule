package raf.microservice.components.userservice.security;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import raf.microservice.components.userservice.dto.ManagerCreateDto;
import raf.microservice.components.userservice.dto.ClientCreateDto;
import raf.microservice.components.userservice.service.ManagerService;
import raf.microservice.components.userservice.service.ClientService;

import java.lang.reflect.Method;

@Aspect
@Configuration
public class CheckExistsAspect {

    private ClientService clientService;
    private ManagerService managerService;

    public CheckExistsAspect(ClientService clientService, ManagerService managerService){
        this.clientService = clientService;
        this.managerService = managerService;
    }

    @Around("@annotation(raf.microservice.components.userservice.security.CheckExists)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String username = null;

        for (int i = 0; i < methodSignature.getParameterNames().length; i++) {
            if (methodSignature.getParameterNames()[i].equals("clientCreateDto") ||
                    methodSignature.getParameterNames()[i].equals("managerCreateDto")) {
                if (joinPoint.getArgs()[i] instanceof ClientCreateDto){
                    username =  ((ClientCreateDto)joinPoint.getArgs()[i]).getUsername();
                    if(clientService.findUsername(username)!=null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }


                if (joinPoint.getArgs()[i] instanceof ManagerCreateDto){
                    username =  ((ManagerCreateDto)joinPoint.getArgs()[i]).getUsername();
                    if(managerService.findUsername(username)!=null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }

                break;
            }
        }

        if(username == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return joinPoint.proceed();
    }

}
