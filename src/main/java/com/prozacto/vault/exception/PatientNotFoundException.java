package com.prozacto.vault.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PatientNotFoundException extends ResponseStatusException {
    public PatientNotFoundException(Long id){
        super(HttpStatus.NOT_FOUND, "Patient with id "+id+" was not found");
    }
}
