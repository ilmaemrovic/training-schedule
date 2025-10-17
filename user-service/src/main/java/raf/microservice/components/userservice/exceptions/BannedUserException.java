package raf.microservice.components.userservice.exceptions;

import org.springframework.http.HttpStatus;

public class BannedUserException extends CustomException{

    public BannedUserException(String message) {
        super(message, ErrorCode.USER_BANNED, HttpStatus.UNAUTHORIZED);
    }
}
