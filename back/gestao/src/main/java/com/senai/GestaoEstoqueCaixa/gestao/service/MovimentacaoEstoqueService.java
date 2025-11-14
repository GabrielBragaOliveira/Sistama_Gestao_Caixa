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
import com.senai.GestaoEstoqueCaixa.gestao.exceptions.ErroValidacaoException;
import com.senai.GestaoEstoqueCaixa.gestao.exceptions.RecursoNaoEncontradoException;
import com.senai.GestaoEstoqueCaixa.gestao.mapper.MovimentoMapper;
import com.senai.GestaoEstoqueCaixa.gestao.repository.MovimentacaoEstoqueRepository;
import com.senai.GestaoEstoqueCaixa.gestao.repository.ProdutoRepository;
import com.senai.GestaoEstoqueCaixa.gestao.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
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
public class MovimentacaoEstoqueService {

    @Autowired
    private MovimentacaoEstoqueRepository repository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Transactional
    public MovimentoEstoqueResponseDTO criarMovimentacao(MovimentoEstoqueRequestDTO dto, String emailUsuario) {

        Produto produto = produtoRepository.findById(dto.produtoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));

        if (produto.getQuantidadeEstoque() == null) {
            produto.setQuantidadeEstoque(0);
        }

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        MovimentoEnum tipo = MovimentoEnum.valueOf(dto.tipo().trim().toUpperCase());
        Integer quantidade = dto.quantidade();

        switch (tipo) {
            case ENTRADA:
                produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + quantidade);
                break;
            case SAIDA:
                if (tipo == MovimentoEnum.SAIDA && produto.getQuantidadeEstoque() < quantidade) {
                    throw new ErroValidacaoException("Estoque insuficiente para saída.");
                }
                produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidade);
                break;
            case AJUSTE:
                produto.setQuantidadeEstoque(quantidade);
                break;
        }

        produtoRepository.save(produto);

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
