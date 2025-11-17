/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.service;

import com.senai.GestaoEstoqueCaixa.gestao.dto.ProdutoRequestDTO;
import com.senai.GestaoEstoqueCaixa.gestao.dto.ProdutoResponseDTO;
import com.senai.GestaoEstoqueCaixa.gestao.entity.Produto;
import com.senai.GestaoEstoqueCaixa.gestao.exceptions.ConflitoException;
import com.senai.GestaoEstoqueCaixa.gestao.exceptions.ErroValidacaoException;
import com.senai.GestaoEstoqueCaixa.gestao.exceptions.RecursoNaoEncontradoException;
import com.senai.GestaoEstoqueCaixa.gestao.exceptions.RequisicaoInvalidaException;
import com.senai.GestaoEstoqueCaixa.gestao.mapper.ProdutoMapper;
import com.senai.GestaoEstoqueCaixa.gestao.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
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
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoMapper produtoMapper;

    public List<ProdutoResponseDTO> listarTodos() {
        return produtoRepository.findAll()
                .stream()
                .map(ProdutoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado com ID: " + id));

        return produtoMapper.toResponseDTO(produto);
    }

    public ProdutoResponseDTO buscarPorCodigo(Integer codigo) {
        Produto produto = produtoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Produto não encontrado com ID: " + codigo));

        return produtoMapper.toResponseDTO(produto);
    }

    @Transactional
    public ProdutoResponseDTO criar(ProdutoRequestDTO dto) {
        Optional<Produto> produtoExistente = produtoRepository.findByCodigo(dto.codigo());

        if (produtoExistente.isPresent()) {
            Produto produto = produtoExistente.get();
            if (!produto.isAtivo()) {
                produto.setCodigo(dto.codigo());
                produto.setNome(dto.nome());
                produto.setCategoria(dto.categoria());
                produto.setQuantidadeEstoque(0);
                produto.setPreco(dto.preco());
                produto.setAtivo(true);

                produtoRepository.save(produto);

                return produtoMapper.toResponseDTO(produto);
            } else {
                throw new ConflitoException("Já existe um produto ativo com esse código");
            }
        }

        Produto produto = produtoMapper.toEntity(dto);
        produto.setAtivo(true);

        if (produto.getQuantidadeEstoque() == null) {
            produto.setQuantidadeEstoque(0);
        }

        Produto produtosSalvo = produtoRepository.save(produto);

        return produtoMapper.toResponseDTO(produtosSalvo);
    }

    @Transactional
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {
        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));

        if (!produtoExistente.getAtivo()) {
            throw new RequisicaoInvalidaException("Somente produtos ativos podem ser editados.");
        }

        if (dto.preco().compareTo(BigDecimal.ZERO) < 0) {
            throw new ErroValidacaoException("O preço não pode ser negativo.");
        }
        produtoExistente.setNome(dto.nome());
        produtoExistente.setCategoria(dto.categoria());
        produtoExistente.setPreco(dto.preco());

        Produto produtoAtualizado = produtoRepository.save(produtoExistente);
        return produtoMapper.toResponseDTO(produtoAtualizado);
    }

    @Transactional
    public void inativar(Long id) {
        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));

        produtoExistente.setAtivo(false);
        produtoRepository.save(produtoExistente);
    }
}
