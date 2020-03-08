package com.prozacto.vault.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DownloadFileException extends ResponseStatusException {
    public DownloadFileException(){
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Could not retrieve file.");
    }
}
