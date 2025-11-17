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
import com.senai.GestaoEstoqueCaixa.gestao.exceptions.ConflitoException;
import com.senai.GestaoEstoqueCaixa.gestao.exceptions.ErroValidacaoException;
import com.senai.GestaoEstoqueCaixa.gestao.exceptions.NaoAutorizadoException;
import com.senai.GestaoEstoqueCaixa.gestao.exceptions.RecursoNaoEncontradoException;
import com.senai.GestaoEstoqueCaixa.gestao.exceptions.RequisicaoInvalidaException;
import com.senai.GestaoEstoqueCaixa.gestao.mapper.UsuarioMapper;
import com.senai.GestaoEstoqueCaixa.gestao.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<UsuarioResponseDTO> listarTodos(String filtro, Boolean ativo, UsuarioEnum perfil) {

        List<Usuario> usuarios = usuarioRepository.filtrarUsuarios(
                (filtro != null && !filtro.isBlank()) ? filtro : null,
                ativo,
                perfil
        );

        return usuarios.stream()
                .map(UsuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com ID: " + id));
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO criar(UsuarioRequestDTO dto) {

        if (dto.nome() == null || dto.nome().isBlank()) {
            throw new ErroValidacaoException("O nome do usuário é obrigatório.");
        }
        if (dto.email() == null || dto.email().isBlank()) {
            throw new ErroValidacaoException("O email do usuário é obrigatório.");
        }
        if (dto.senha() == null || dto.senha().isBlank()) {
            throw new ErroValidacaoException("A senha do usuário é obrigatória.");
        }
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(dto.email());

        if (usuarioExistente.isPresent()) {
            Usuario usuario = usuarioExistente.get();
            if (!usuario.isAtivo()) {
                usuario.setNome(dto.nome());
                usuario.setSenha(dto.senha());
                usuario.setPerfil(dto.perfil());
                usuario.setAtivo(true);

                usuarioRepository.save(usuario);
                return usuarioMapper.toResponseDTO(usuario);
            } else {
                throw new ConflitoException("Já existe um usuário ativo com este e-mail.");
            }
        }

        Usuario usuario = usuarioMapper.toEntity(dto);
        usuario.setAtivo(true);
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return usuarioMapper.toResponseDTO(usuarioSalvo);
    }

    @Transactional
    public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO dto) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        if (!usuarioExistente.getEmail().equals(dto.email())) {
            throw new RequisicaoInvalidaException("O email do usuário não pode ser alterado.");
        }

        if (!usuarioExistente.isAtivo()) {
            throw new RequisicaoInvalidaException("Somente usuários ativos podem ser editados.");
        }

        if (dto.nome().isBlank() || dto.nome() == null) {
            throw new ErroValidacaoException("O nome é obrigatório.");
        }
        if (dto.email().isBlank() || dto.email() == null) {
            throw new ErroValidacaoException("O email é obrigatório.");
        }
        if (dto.senha().isBlank() || dto.senha() == null) {
            throw new ErroValidacaoException("A senha é obrigatória.");
        }

        if (!usuarioExistente.getEmail().equalsIgnoreCase(dto.email())
                && usuarioRepository.existsByEmail(dto.email())) {
            throw new ConflitoException("O email informado já está cadastrado para outro usuário.");
        }

        if (!usuarioExistente.getEmail().equalsIgnoreCase(dto.email())
                && usuarioRepository.existsByEmail(dto.email())) {
            throw new ConflitoException("O email informado já está cadastrado para outro usuário.");
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
    public void inativar(Long id, String emailUsuarioLogado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));

        if (usuarioExistente.getEmail().equalsIgnoreCase(emailUsuarioLogado)) {
            throw new RequisicaoInvalidaException("Você não pode inativar seu próprio usuário.");
        }

        usuarioExistente.setAtivo(false);
        usuarioRepository.save(usuarioExistente);
    }

    @Transactional
    public UsuarioResponseDTO login(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new NaoAutorizadoException("Email ou senha incorretos."));

        if (!usuario.isAtivo()) {
            throw new NaoAutorizadoException("Usuário inativo.");
        }

        if (!usuario.getSenha().equals(dto.senha())) {
            throw new NaoAutorizadoException("Email ou senha incorretos.");
        }

        return usuarioMapper.toResponseDTO(usuario);
    }
}
