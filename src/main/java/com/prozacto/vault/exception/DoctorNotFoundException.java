package com.prozacto.vault.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DoctorNotFoundException extends ResponseStatusException {
    public DoctorNotFoundException(Long id){
        super(HttpStatus.NOT_FOUND, "Doctor with id "+id+" was not found");
    }
}
