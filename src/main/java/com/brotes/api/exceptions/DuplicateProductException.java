package com.brotes.api.exceptions;

public class DuplicateProductException extends RuntimeException {

    public DuplicateProductException(String msg) {
        super(msg);
    }
}
