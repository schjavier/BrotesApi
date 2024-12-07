package com.brotes.api.modelo.pedidos;

public class ClientNotExistException extends Exception {
    public ClientNotExistException(String msg) {
        super(msg);
    }

}
