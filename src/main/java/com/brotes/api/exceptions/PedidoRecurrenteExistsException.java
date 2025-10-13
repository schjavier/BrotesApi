package com.brotes.api.exceptions;

public class PedidoRecurrenteExistsException extends RuntimeException {

    public PedidoRecurrenteExistsException(String msg) {
        super(msg);
    }
}
