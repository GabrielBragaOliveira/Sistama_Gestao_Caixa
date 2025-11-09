/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.service;

import com.senai.GestaoEstoqueCaixa.gestao.dto.LoginRequestDTO;
import com.senai.GestaoEstoqueCaixa.gestao.dto.UsuarioRequestDTO;
import com.senai.GestaoEstoqueCaixa.gestao.dto.UsuarioResponseDTO;
import com.senai.GestaoEstoqueCaixa.gestao.entity.Usuario;
import com.senai.GestaoEstoqueCaixa.gestao.enums.UsuarioEnum;
import com.senai.GestaoEstoqueCaixa.gestao.mapper.UsuarioMapper;
import com.senai.GestaoEstoqueCaixa.gestao.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
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

    //@Autowired
    //private LoginMapper LoginMapper;
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<UsuarioResponseDTO> listarTodos(String filtro, Boolean ativo, UsuarioEnum perfil) {
        List<Usuario> usuarios = new ArrayList<>();

        if (filtro != null && !filtro.isBlank() && ativo != null) {
            usuarios = usuarioRepository.findByNomeIgnoreCaseContainingOrEmailIgnoreCaseContainingAndAtivoOrderByNomeAsc(filtro, filtro, ativo);
        } else if (filtro != null && !filtro.isBlank() && perfil != null) {
            usuarios = usuarioRepository.findByNomeIgnoreCaseContainingOrEmailIgnoreCaseContainingAndPerfilOrderByNomeAsc(filtro, filtro, perfil);
        } else if (ativo != null) {
            usuarios = usuarioRepository.findByAtivo(ativo);
        } else if (filtro != null && !filtro.isBlank()) {
            usuarios = usuarioRepository.findByNomeIgnoreCaseContainingOrEmailIgnoreCaseContainingOrderByNomeAsc(filtro, filtro);
        } else {
            usuarios = usuarioRepository.findAll();
        }

        return usuarios.stream()
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
                "Usuário não encontrado com email: " + email));
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO criar(UsuarioRequestDTO dto) {
        if (dto.nome() == null || dto.nome().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do usuário é obrigatório.");
        }
        if (dto.email() == null || dto.email().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O email do usuário é obrigatório.");
        }
        if (dto.senha() == null || dto.senha().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha do usuário é obrigatória.");
        }

        usuarioRepository.findByEmail(dto.email()).ifPresent(usuario -> {
            if (usuario.isAtivo()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário ativo com este email.");
            } else {

                usuario.setNome(dto.nome());
                usuario.setSenha(dto.senha());
                usuario.setPerfil(dto.perfil());
                usuario.setAtivo(true);
                usuarioRepository.save(usuario);
            }
        });

        Usuario usuario = usuarioMapper.toEntity(dto);
        usuario.setAtivo(true);
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return usuarioMapper.toResponseDTO(usuarioSalvo);
    }

    @Transactional
    public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO dto) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Usuário não encontrado com email: " + id));

        if (!usuarioExistente.isAtivo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Somente usuários ativos podem ser editados.");
        }

        if (dto.nome() == null || dto.nome().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do usuário é obrigatório.");
        }
        if (dto.email() == null || dto.email().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O email do usuário é obrigatório.");
        }
        if (dto.senha() == null || dto.senha().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha do usuário é obrigatória.");
        }

        if (!usuarioExistente.getEmail().equalsIgnoreCase(dto.email())
                && usuarioRepository.existsByEmail(dto.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O email informado já está cadastrado para outro usuário.");
        }

        usuarioExistente.setNome(dto.nome());
        usuarioExistente.setEmail(dto.email());
        usuarioExistente.setSenha(dto.senha());
        usuarioExistente.setPerfil(dto.perfil());
        usuarioExistente.setAtivo(true);

        Usuario usuarioAtualizado = usuarioRepository.save(usuarioExistente);
        return usuarioMapper.toResponseDTO(usuarioAtualizado);
    }

    @Transactional
    public void inativar(Long id) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Usuário não encontrado: "));

        usuarioExistente.setAtivo(false);
        usuarioRepository.save(usuarioExistente);
    }

    @Transactional
    public UsuarioResponseDTO login(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou senha incorretos."));

        if (!usuario.isAtivo()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário inativo.");
        }

        if (!usuario.getSenha().equals(dto.senha())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou senha incorretos.");
        }

        return usuarioMapper.toResponseDTO(usuario);
    }
}
