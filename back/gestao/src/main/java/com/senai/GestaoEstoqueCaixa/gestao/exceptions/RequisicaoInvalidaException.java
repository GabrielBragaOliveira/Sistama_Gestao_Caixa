/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author CacatsViado
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequisicaoInvalidaException extends RuntimeException {

    //Erro 400 
    //Usada quando o cliente envia dados inválidos.
    public RequisicaoInvalidaException(String message) {
        super(message);
    }

    public RequisicaoInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }
}
