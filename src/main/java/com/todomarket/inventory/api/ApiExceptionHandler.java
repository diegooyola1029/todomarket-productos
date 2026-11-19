package com.todomarket.inventory.api;

import java.time.LocalDateTime;
import java.util.Locale;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> manejarSolicitudInvalida(IllegalArgumentException excepcion) {
        String mensaje = excepcion.getMessage() == null ? "Solicitud no válida." : excepcion.getMessage();
        String normalizado = mensaje.toLowerCase(Locale.ROOT);
        HttpStatus estado = normalizado.contains("no encontrado")
                ? HttpStatus.NOT_FOUND
                : normalizado.contains("ya existe") ? HttpStatus.CONFLICT : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(estado).body(new ApiError(mensaje, LocalDateTime.now()));
    }
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ApiError> manejarDuplicado(DuplicateKeyException excepcion) {
        String mensaje = "Ya existe un producto con ese nombre.";
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError(mensaje, LocalDateTime.now()));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiError> manejarFormatoInvalido(Exception excepcion) {
        String mensaje = "El identificador o el cuerpo de la solicitud tiene un formato inválido.";
        return ResponseEntity.badRequest().body(new ApiError(mensaje, LocalDateTime.now()));
    }

    @ExceptionHandler({DataAccessResourceFailureException.class, QueryTimeoutException.class})
    public ResponseEntity<ApiError> manejarBaseNoDisponible(Exception excepcion) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ApiError("MongoDB no está disponible.", LocalDateTime.now()));
    }
}
