/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Aluno
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ErroInternoException extends RuntimeException {

    public ErroInternoException(String message) {
        super(message);
    }

    public ErroInternoException(String message, Throwable cause) {
        super(message, cause);
    }
}
