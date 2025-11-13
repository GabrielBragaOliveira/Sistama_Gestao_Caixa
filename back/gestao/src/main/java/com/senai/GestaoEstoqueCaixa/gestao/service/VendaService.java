/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.GestaoEstoqueCaixa.gestao.service;

import com.senai.GestaoEstoqueCaixa.gestao.dto.ItemVendaRequestDTO;
import com.senai.GestaoEstoqueCaixa.gestao.dto.VendaRequestDTO;
import com.senai.GestaoEstoqueCaixa.gestao.dto.VendaResponseDTO;
import com.senai.GestaoEstoqueCaixa.gestao.entity.ItemVenda;
import com.senai.GestaoEstoqueCaixa.gestao.entity.MovimentacaoEstoque;
import com.senai.GestaoEstoqueCaixa.gestao.entity.Produto;
import com.senai.GestaoEstoqueCaixa.gestao.entity.Usuario;
import com.senai.GestaoEstoqueCaixa.gestao.entity.Venda;
import com.senai.GestaoEstoqueCaixa.gestao.enums.MovimentoEnum;
import com.senai.GestaoEstoqueCaixa.gestao.mapper.ItemVendaMapper;
import com.senai.GestaoEstoqueCaixa.gestao.mapper.VendaMapper;
import com.senai.GestaoEstoqueCaixa.gestao.repository.ItemVendaRepository;
import com.senai.GestaoEstoqueCaixa.gestao.repository.ProdutoRepository;
import com.senai.GestaoEstoqueCaixa.gestao.repository.UsuarioRepository;
import com.senai.GestaoEstoqueCaixa.gestao.repository.VendaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Aluno
 */
@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;
    @Autowired
    private ItemVendaRepository itemVendaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MovimentacaoEstoqueService movimentacaoEstoqueService;

    public VendaService(
            VendaRepository vendaRepository,
            ItemVendaRepository itemVendaRepository,
            ProdutoRepository produtoRepository,
            UsuarioRepository usuarioRepository,
            MovimentacaoEstoqueService movimentacaoEstoqueService) {
        this.vendaRepository = vendaRepository;
        this.itemVendaRepository = itemVendaRepository;
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
        this.movimentacaoEstoqueService = movimentacaoEstoqueService;
    }

    @Transactional
    public VendaResponseDTO registrarVenda(VendaRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        List<ItemVenda> itens = new ArrayList<>();

        for (ItemVendaRequestDTO itemDTO : dto.itens()) {
            Produto produto = produtoRepository.findByIdForUpdate(itemDTO.produtoId())
                    .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: ID " + itemDTO.produtoId()));

            if (itemDTO.precoUnitario() == null
                    || produto.getPreco() == null
                    || itemDTO.precoUnitario().compareTo(produto.getPreco()) != 0) {
                throw new IllegalArgumentException("Preço unitário inválido para o produto: " + produto.getNome());
            }

            if (itemDTO.quantidade() > produto.getQuantidadeEstoque()) {
                throw new IllegalArgumentException("Estoque insuficiente para o produto: " + produto.getNome());
            }

            ItemVenda item = ItemVendaMapper.toEntity(itemDTO, produto);
            itens.add(item);

            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - item.getQuantidade());
            produtoRepository.save(produto);
        }

        Venda venda = VendaMapper.toEntity(dto, usuario, itens);
        venda.setDataVenda(LocalDateTime.now());
        venda.getItens().forEach(item -> item.setVenda(venda));

        if (venda.getValorRecebido() == null
                || venda.getValorRecebido().compareTo(venda.getValorTotal()) < 0) {
            throw new IllegalArgumentException(
                    "Valor recebido R$" + venda.getValorRecebido()
                    + " é menor que o valor total da venda R$" + venda.getValorTotal() 
            );
        }

        Venda vendaSalva = vendaRepository.save(venda);

        for (ItemVenda item : vendaSalva.getItens()) {
            MovimentacaoEstoque mov = new MovimentacaoEstoque();
            mov.setProduto(item.getProduto());
            mov.setTipo(MovimentoEnum.SAIDA);
            mov.setQuantidade(item.getQuantidade());
            mov.setMotivo("Venda ID " + vendaSalva.getId());
            mov.setData(LocalDateTime.now());
            movimentacaoEstoqueService.registrarMovimentacao(mov);
        }

        return VendaMapper.toResponseDTO(vendaSalva);
    }

    public List<VendaResponseDTO> listarTodas() {
        return vendaRepository.findAll()
                .stream()
                .map(VendaMapper::toResponseDTO)
                .toList();
    }

    public VendaResponseDTO buscarPorId(Long id) {
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venda não encontrada"));
        return VendaMapper.toResponseDTO(venda);
    }
}
