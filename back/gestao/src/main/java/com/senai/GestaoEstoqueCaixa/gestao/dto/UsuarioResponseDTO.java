/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.dto;

import com.senai.GestaoEstoqueCaixa.gestao.enums.UsuarioEnum;

/**
 *
 * @author Aluno
 */
public record UsuarioResponseDTO(
    Long id,
    String nome,
    String email,
    UsuarioEnum perfil,
    Boolean ativo
) {}