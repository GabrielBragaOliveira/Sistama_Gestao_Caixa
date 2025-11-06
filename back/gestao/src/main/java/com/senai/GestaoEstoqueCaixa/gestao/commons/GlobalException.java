/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.commons;

import com.senai.GestaoEstoqueCaixa.gestao.exceptions.RecursoNaoEncontradoException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 *
 * @author Aluno
 */
@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErrorResponseSpring> handleRecursoNaoEncontradoException(RecursoNaoEncontradoException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    private ResponseEntity<ErrorResponseSpring> buildResponse(HttpStatus status, String message) {
        ErrorResponseSpring error = new ErrorResponseSpring(status.value(), message, LocalDateTime.now());
        return new ResponseEntity<>(error, status);
    }
}
