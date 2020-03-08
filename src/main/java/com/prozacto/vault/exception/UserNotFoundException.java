package com.prozacto.vault.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserNotFoundException extends ResponseStatusException {
    public UserNotFoundException(Long id){
        super(HttpStatus.NOT_FOUND, "User with id "+id+" was not found");
    }
}
