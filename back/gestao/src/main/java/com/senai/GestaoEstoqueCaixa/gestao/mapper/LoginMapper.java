/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.mapper;

import com.senai.GestaoEstoqueCaixa.gestao.dto.LoginResponseDTO;
import com.senai.GestaoEstoqueCaixa.gestao.entity.Usuario;
import org.springframework.stereotype.Component;

/**
 *
 * @author CacatsViado
 */
@Component
public class LoginMapper {
    
    public static LoginResponseDTO toResponseDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return new LoginResponseDTO(
            usuario.getNome(),
            usuario.getPerfil() // enum → String
        );
    }
}
