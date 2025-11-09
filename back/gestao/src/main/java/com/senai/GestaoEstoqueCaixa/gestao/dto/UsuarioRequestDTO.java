/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.dto;

import com.senai.GestaoEstoqueCaixa.gestao.enums.UsuarioEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 *
 * @author Aluno
 */
public record UsuarioRequestDTO (
        
    @NotBlank(message = "O nome é obrigatório") 
    @jakarta.validation.constraints.Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres.") 
    String nome,
        
    @Email 
    @NotBlank(message = "O Email é obrigatório") 
    String email,
    
    @NotBlank(message = "O Senha é obrigatório") 
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$",
        message = "A senha deve ter ao menos 8 caracteres, uma letra maiúscula e um número."
    )
    String senha,
    
    @NotNull(message = "O perfil é obrigatório.")
    UsuarioEnum perfil
        
) {}