/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.service;

import com.senai.GestaoEstoqueCaixa.gestao.dto.ItemVendaRequestDTO;
import com.senai.GestaoEstoqueCaixa.gestao.dto.ItemVendaResponseDTO;
import com.senai.GestaoEstoqueCaixa.gestao.entity.ItemVenda;
import com.senai.GestaoEstoqueCaixa.gestao.entity.Produto;
import com.senai.GestaoEstoqueCaixa.gestao.mapper.ItemVendaMapper;
import com.senai.GestaoEstoqueCaixa.gestao.repository.ItemVendaRepository;
import com.senai.GestaoEstoqueCaixa.gestao.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 *
 * @author Aluno
 */
@Service
public class ItemVendaService {

    private final ItemVendaRepository itemVendaRepository;
    private final ProdutoRepository produtoRepository;
    private final MovimentacaoEstoqueService movimentacaoEstoqueService;

    public ItemVendaService(ItemVendaRepository itemVendaRepository,
                            ProdutoRepository produtoRepository,
                            MovimentacaoEstoqueService movimentacaoEstoqueService) {
        this.itemVendaRepository = itemVendaRepository;
        this.produtoRepository = produtoRepository;
        this.movimentacaoEstoqueService = movimentacaoEstoqueService;
    }
    
    @Transactional
    public ItemVendaResponseDTO criarItemVenda(ItemVendaRequestDTO dto) {
        Produto produto = produtoRepository.findById(dto.produtoId())
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: ID " + dto.produtoId()));

        if (dto.quantidade() > produto.getQuantidadeEstoque()) {
            throw new IllegalArgumentException("Estoque insuficiente para o produto: " + produto.getNome());
        }

        ItemVenda item = ItemVendaMapper.toEntity(dto, produto);

        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - item.getQuantidade());
        produtoRepository.save(produto);

        movimentacaoEstoqueService.registrarSaida(produto, item.getQuantidade(), "Venda direta");

        ItemVenda itemSalvo = itemVendaRepository.save(item);

        return ItemVendaMapper.toResponseDTO(itemSalvo);
    }

    public List<ItemVendaResponseDTO> listarTodos() {
        return itemVendaRepository.findAll()
                .stream()
                .map(ItemVendaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ItemVendaResponseDTO buscarPorId(Long id) {
        ItemVenda item = itemVendaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item de venda não encontrado"));
        return ItemVendaMapper.toResponseDTO(item);
    }
    
    @Transactional
    public void deletarItem(Long id) {
        if (!itemVendaRepository.existsById(id)) {
            throw new EntityNotFoundException("Item de venda não encontrado para exclusão");
        }
        itemVendaRepository.deleteById(id);
    }
}