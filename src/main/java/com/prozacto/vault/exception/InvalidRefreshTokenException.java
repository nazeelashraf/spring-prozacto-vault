package com.prozacto.vault.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidRefreshTokenException extends ResponseStatusException {

    public InvalidRefreshTokenException(){
        super(HttpStatus.NOT_ACCEPTABLE, "The refresh token is invalid");
    }
}
