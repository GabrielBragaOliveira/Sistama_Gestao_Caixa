/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.commons;

import com.senai.GestaoEstoqueCaixa.gestao.exceptions.ConflitoException;
import com.senai.GestaoEstoqueCaixa.gestao.exceptions.ErroInternoException;
import com.senai.GestaoEstoqueCaixa.gestao.exceptions.ErroValidacaoException;
import com.senai.GestaoEstoqueCaixa.gestao.exceptions.RecursoNaoEncontradoException;
import com.senai.GestaoEstoqueCaixa.gestao.exceptions.RequisicaoInvalidaException;
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

    @ExceptionHandler(RequisicaoInvalidaException.class)
    public ResponseEntity<ErrorResponseSpring> handleRequisicaoInvalidaException(RequisicaoInvalidaException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<ErrorResponseSpring> handleConflitoException(ConflitoException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(ErroValidacaoException.class)
    public ResponseEntity<ErrorResponseSpring> handleErroValidacaoException(ErroValidacaoException ex) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(ErroInternoException.class)
    public ResponseEntity<ErrorResponseSpring> handleErroInternoException(ErroInternoException ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    private ResponseEntity<ErrorResponseSpring> buildResponse(HttpStatus status, String message) {
        ErrorResponseSpring error = new ErrorResponseSpring(status.value(), message, LocalDateTime.now());
        return new ResponseEntity<>(error, status);
    }
}
