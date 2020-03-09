package com.prozacto.vault.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmptyFieldException extends ResponseStatusException {
    public EmptyFieldException(String field){
        super(HttpStatus.BAD_REQUEST, "Required field '"+field+"' is missing.");
    }
}
