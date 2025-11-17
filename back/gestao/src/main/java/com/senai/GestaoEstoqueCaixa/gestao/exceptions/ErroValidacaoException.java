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
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class ErroValidacaoException extends RuntimeException {

    //Erro 422
    //Quando os dados estão corretos estruturalmente, mas inválidos semanticamente.
    public ErroValidacaoException(String message) {
        super(message);
    }

    public ErroValidacaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
