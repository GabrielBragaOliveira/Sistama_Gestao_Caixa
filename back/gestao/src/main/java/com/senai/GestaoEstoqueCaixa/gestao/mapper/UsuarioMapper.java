/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.mapper;

import com.senai.GestaoEstoqueCaixa.gestao.dto.UsuarioRequestDTO;
import com.senai.GestaoEstoqueCaixa.gestao.dto.UsuarioResponseDTO;
import com.senai.GestaoEstoqueCaixa.gestao.entity.Usuario;
import org.springframework.stereotype.Component;

/**
 *
 * @author Aluno
 */
@Component
public class UsuarioMapper {

    public static Usuario toEntity(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(dto.senha());
        usuario.setPerfil(dto.perfil());
        return usuario;
    }

    public static UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getPerfil(),
            usuario.getAtivo()
        );
    }
}
