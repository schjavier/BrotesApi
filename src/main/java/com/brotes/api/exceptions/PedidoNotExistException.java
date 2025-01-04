package com.brotes.api.exceptions;

public class PedidoNotExistException extends RuntimeException {

    public PedidoNotExistException(String msg){
        super(msg);
    }
}
