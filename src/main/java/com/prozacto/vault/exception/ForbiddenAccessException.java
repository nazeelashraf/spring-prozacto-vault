package com.prozacto.vault.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ForbiddenAccessException extends ResponseStatusException {
    public ForbiddenAccessException(Long id){
        super(HttpStatus.FORBIDDEN, "Access to patient id "+id+" is forbidden for your clinic");
    }
}
