package com.prozacto.vault.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidRoleException extends ResponseStatusException {
    public InvalidRoleException(){
        super(HttpStatus.BAD_REQUEST, "Unable to resolve role");

    }
}
