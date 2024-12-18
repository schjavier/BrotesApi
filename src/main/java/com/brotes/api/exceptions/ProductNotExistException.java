package com.brotes.api.exceptions;

public class ProductNotExistException extends RuntimeException {
    public ProductNotExistException(String msg) {
        super(msg);
    }
}
