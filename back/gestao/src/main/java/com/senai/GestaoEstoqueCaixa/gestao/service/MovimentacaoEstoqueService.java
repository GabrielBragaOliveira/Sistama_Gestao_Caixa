/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.service;

import com.senai.GestaoEstoqueCaixa.gestao.dto.MovimentoEstoqueRequestDTO;
import com.senai.GestaoEstoqueCaixa.gestao.dto.MovimentoEstoqueResponseDTO;
import com.senai.GestaoEstoqueCaixa.gestao.entity.MovimentacaoEstoque;
import com.senai.GestaoEstoqueCaixa.gestao.entity.Produto;
import com.senai.GestaoEstoqueCaixa.gestao.entity.Usuario;
import com.senai.GestaoEstoqueCaixa.gestao.enums.MovimentoEnum;
import com.senai.GestaoEstoqueCaixa.gestao.mapper.MovimentoMapper;
import com.senai.GestaoEstoqueCaixa.gestao.repository.MovimentacaoEstoqueRepository;
import com.senai.GestaoEstoqueCaixa.gestao.repository.ProdutoRepository;
import com.senai.GestaoEstoqueCaixa.gestao.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author Aluno
 */
@Service
public class MovimentacaoEstoqueService {

    private final MovimentacaoEstoqueRepository repository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;

    public MovimentacaoEstoqueService(MovimentacaoEstoqueRepository repository,
            ProdutoRepository produtoRepository,
            UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public MovimentoEstoqueResponseDTO criarMovimentacao(MovimentoEstoqueRequestDTO dto) {

        Produto produto = produtoRepository.findById(dto.produtoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado."));

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        MovimentacaoEstoque movimentacao = MovimentoMapper.toEntity(dto, produto, usuario);

        repository.save(movimentacao);

        return MovimentoMapper.toResponseDTO(movimentacao);
    }

    public List<MovimentoEstoqueResponseDTO> listarMovimentacoes() {
        return repository.findAll()
                .stream()
                .map(MovimentoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public MovimentoEstoqueResponseDTO buscarPorId(Long id) {
        MovimentacaoEstoque movimentacao = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movimentação não encontrada."));
        return MovimentoMapper.toResponseDTO(movimentacao);
    }

    public List<MovimentoEstoqueResponseDTO> listarPorProduto(Long produtoId) {
        return repository.findByProdutoId(produtoId)
                .stream()
                .map(MovimentoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<MovimentoEstoqueResponseDTO> listarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioResponsavelId(usuarioId)
                .stream()
                .map(MovimentoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<MovimentoEstoqueResponseDTO> listarPorTipo(MovimentoEnum tipo) {
        return repository.findByTipo(tipo)
                .stream()
                .map(MovimentoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public void registrarMovimentacao(MovimentacaoEstoque movimentacao) {
        repository.save(movimentacao);
    }

    public void registrarSaida(Produto produto, Integer quantidade, String motivo) {
        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setProduto(produto);
        mov.setTipo(MovimentoEnum.SAIDA);
        mov.setQuantidade(quantidade);
        mov.setMotivo(motivo);
        mov.setData(LocalDateTime.now());
        repository.save(mov);
    }
}
