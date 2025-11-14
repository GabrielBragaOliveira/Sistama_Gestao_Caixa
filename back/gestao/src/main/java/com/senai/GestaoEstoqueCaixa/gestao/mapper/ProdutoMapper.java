/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.mapper;

import com.senai.GestaoEstoqueCaixa.gestao.dto.ProdutoRequestDTO;
import com.senai.GestaoEstoqueCaixa.gestao.dto.ProdutoResponseDTO;
import com.senai.GestaoEstoqueCaixa.gestao.entity.Produto;
import org.springframework.stereotype.Component;

/**
 *
 * @author Aluno
 */
@Component
public class ProdutoMapper {

    public static Produto toEntity(ProdutoRequestDTO dto) {
        Produto produto = new Produto();
        produto.setNome(dto.nome());
        produto.setCodigo(dto.codigo());
        produto.setCategoria(dto.categoria());
        produto.setQuantidadeEstoque(0);
        produto.setPreco(dto.preco());
        return produto;
    }

    public static ProdutoResponseDTO toResponseDTO(Produto produto) {
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getCodigo(),
                produto.getCategoria(),
                produto.getQuantidadeEstoque(),
                produto.getPreco(),
                produto.getAtivo()
        );
    }
}
