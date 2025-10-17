package raf.microservice.scheduletraining.security.service;

import io.jsonwebtoken.Claims;

public interface TokenService {

    Claims parseToken(String jwt);
}