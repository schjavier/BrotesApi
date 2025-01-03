package com.brotes.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientNotExistException.class)
    public ResponseEntity<String> handleClientNotExistException (ClientNotExistException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ProductNotExistException.class)
    public ResponseEntity<String> handleProductNotExistException(ProductNotExistException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ClienteDesactivadoException.class)
    public ResponseEntity<String> handleClienteDesactivadoException(ClienteDesactivadoException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ClienteActivadoException.class)
    public ResponseEntity<String> handleClienteActivadoException(ClienteActivadoException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ClienteDuplicadoException.class)
    public ResponseEntity<String> handleClienteDuplicadoException(ClienteDuplicadoException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
