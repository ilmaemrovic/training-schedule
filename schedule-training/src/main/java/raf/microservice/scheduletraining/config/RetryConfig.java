package raf.microservice.scheduletraining.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RetryConfig {

    @Bean
    public RetryTemplate retryTemplate(){
        return RetryTemplate.builder()
                .exponentialBackoff(100,1.5,2000)
                .build();
    }
}
