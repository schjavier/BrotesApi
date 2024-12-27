package com.brotes.api.exceptions;

public class ProductoDesactivadoException extends RuntimeException {
    public ProductoDesactivadoException(String msg) {
        super(msg);
    }
}
