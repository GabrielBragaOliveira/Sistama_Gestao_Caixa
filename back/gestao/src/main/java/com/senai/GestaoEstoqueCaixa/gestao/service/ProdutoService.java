/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.service;

import com.senai.GestaoEstoqueCaixa.gestao.dto.ProdutoRequestDTO;
import com.senai.GestaoEstoqueCaixa.gestao.dto.ProdutoResponseDTO;
import com.senai.GestaoEstoqueCaixa.gestao.entity.Produto;
import com.senai.GestaoEstoqueCaixa.gestao.exceptions.RecursoNaoEncontradoException;
import com.senai.GestaoEstoqueCaixa.gestao.mapper.ProdutoMapper;
import com.senai.GestaoEstoqueCaixa.gestao.repository.ProdutoRepository;
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
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoMapper produtoMapper;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }
    
    public List<ProdutoResponseDTO> listarTodos() {
        return produtoRepository.findAll()
                .stream()
                .map(ProdutoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Produto não encontrado com ID: " + id));

        return produtoMapper.toResponseDTO(produto);
    }
    
    public ProdutoResponseDTO buscarPorCodigo(String codigo) {
        Produto produto = produtoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Produto não encontrado com ID: " + codigo));

        return produtoMapper.toResponseDTO(produto);
    }

    @Transactional
    public ProdutoResponseDTO criar(ProdutoRequestDTO dto) {
        Produto produto = ProdutoMapper.toEntity(dto);
        Produto produtoSalvo = produtoRepository.save(produto);
        return ProdutoMapper.toResponseDTO(produtoSalvo);
    }

    @Transactional
    public ProdutoResponseDTO atualizar(String codigo, ProdutoRequestDTO dto) {
        Produto produtoExistente = produtoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado com Codigo: " + codigo));

        produtoExistente.setNome(dto.nome());
        produtoExistente.setCategoria(dto.categoria());
        produtoExistente.setQuantidadeEstoque(dto.quantidadeEstoque());
        produtoExistente.setPreco(dto.preco());

        Produto produtoAtualizado = produtoRepository.save(produtoExistente);
        return ProdutoMapper.toResponseDTO(produtoAtualizado);
    }

    @Transactional
    public void deletar(String codigo) {
        Produto produtoExistente = produtoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado com Codigo: " + codigo));

        produtoExistente.setAtivo(false);
        produtoRepository.delete(produtoExistente);
    }
}
