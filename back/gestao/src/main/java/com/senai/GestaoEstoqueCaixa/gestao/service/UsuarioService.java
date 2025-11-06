/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.service;

import com.senai.GestaoEstoqueCaixa.gestao.dto.UsuarioRequestDTO;
import com.senai.GestaoEstoqueCaixa.gestao.dto.UsuarioResponseDTO;
import com.senai.GestaoEstoqueCaixa.gestao.entity.Usuario;
import com.senai.GestaoEstoqueCaixa.gestao.exceptions.RecursoNaoEncontradoException;
import com.senai.GestaoEstoqueCaixa.gestao.mapper.UsuarioMapper;
import com.senai.GestaoEstoqueCaixa.gestao.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author Aluno
 */
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuário não encontrado com ID: " + id));

        return usuarioMapper.toResponseDTO(usuario);
    }
    
    public UsuarioResponseDTO buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuário não encontrado com ID: " + email));

        return usuarioMapper.toResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO criar(UsuarioRequestDTO dto) {
        Usuario usuario = UsuarioMapper.toEntity(dto);
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return UsuarioMapper.toResponseDTO(usuarioSalvo);
    }

    @Transactional
    public UsuarioResponseDTO atualizar(String email, UsuarioRequestDTO dto) {
        Usuario usuarioExistente = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com Email: " + email));

        usuarioExistente.setNome(dto.nome());
        usuarioExistente.setEmail(dto.email());
        usuarioExistente.setSenha(dto.senha());
        usuarioExistente.setPerfil(dto.perfil());
        usuarioExistente.setAtivo(dto.ativo());

        Usuario usuarioAtualizado = usuarioRepository.save(usuarioExistente);
        return UsuarioMapper.toResponseDTO(usuarioAtualizado);
    }

    @Transactional
    public void deletar(String email) {
        Usuario usuarioExistente = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com Email: " + email));

        usuarioExistente.setAtivo(false);
        usuarioRepository.save(usuarioExistente);
    }
}
