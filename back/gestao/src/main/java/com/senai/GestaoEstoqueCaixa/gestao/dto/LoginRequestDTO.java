/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 *
 * @author Aluno
 */
public record LoginRequestDTO(

    @Email(message = "O email é obrigatório e deve ser válido.")
    @NotBlank(message = "O email é obrigatório.")
    String email,

    @NotBlank(message = "A senha é obrigatória.")
    String senha

) {}