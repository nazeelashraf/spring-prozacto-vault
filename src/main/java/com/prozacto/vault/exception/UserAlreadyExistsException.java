package com.prozacto.vault.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends ResponseStatusException {
	
	public UserAlreadyExistsException() {
		super(HttpStatus.CONFLICT, "This username is in use.");
	}
	
}
