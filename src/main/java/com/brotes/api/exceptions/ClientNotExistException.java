package com.brotes.api.exceptions;

public class ClientNotExistException extends RuntimeException {
    public ClientNotExistException(String msg) {
        super(msg);
    }

}
