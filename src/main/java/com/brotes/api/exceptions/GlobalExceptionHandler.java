package com.brotes.api.exceptions;

import com.brotes.api.modelo.pedidos.DiaDeEntrega;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.http.HttpResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientNotExistException.class)
    public ResponseEntity<String> handleClientNotExistException (ClientNotExistException ex){
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

    //Handlers de producto

    @ExceptionHandler(ProductNotExistException.class)
    public ResponseEntity<String> handleProductNotExistException(ProductNotExistException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(DuplicateProductException.class)
    public ResponseEntity<String> handleDuplicateProductEsception(DuplicateProductException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ProductoActivoException.class)
    public ResponseEntity<String> handleProductoActivoException(ProductoActivoException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ProductoDesactivadoException.class)
    public ResponseEntity<String> handleProductoDesactivadoException(ProductoDesactivadoException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // Handlers de Pedido

    @ExceptionHandler(PedidoNotExistException.class)
    public ResponseEntity<String> handlePedidoNotExistException(PedidoNotExistException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(PedidoDuplicadoException.class)
    public ResponseEntity<String> handlePedidoDuplicadoException(PedidoDuplicadoException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentException(MethodArgumentTypeMismatchException ex) {
        String errorMsg = "";
        if (ex.getRequiredType() != null && ex.getRequiredType().equals(DiaDeEntrega.class)) {
            errorMsg = "El dia de entrega '" + ex.getValue() +
                    "' es invalido. Los dias permitidos son Martes y Viernes";
        }
        return new ResponseEntity<>(errorMsg, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParameter(
            MissingServletRequestParameterException ex,
            HttpServletRequest request) {

        String path = request.getRequestURI();

        String errorMsg = switch (path) {
            case ("/pedidos/buscar") -> "El parametro 'dia' es requerido";
            case ("/cliente/buscar") -> "El parametro 'nombre' del cliente es requerido";
            case ("/producto/buscar") -> "El parametro 'nombre' del producto es requerido";
            default -> "";
        };

        return new ResponseEntity<>(errorMsg, HttpStatus.BAD_REQUEST);
    }
}
