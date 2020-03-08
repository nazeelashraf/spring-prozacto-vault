package com.prozacto.vault.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ClinicNotFoundException extends ResponseStatusException {
    public ClinicNotFoundException(Long id){
        super(HttpStatus.NOT_FOUND, "Clinic with id "+id+" was not found");
    }
}
