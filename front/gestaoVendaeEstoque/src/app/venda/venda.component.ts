import { CardModule } from 'primeng/card';
import { InputNumberModule } from 'primeng/inputnumber';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { MessageService } from 'primeng/api';
import { ProdutoService } from '../service/Produto.Service';
import { VendaService } from '../service/Venda.Service';
import { ProdutoResponse } from '../modelos/DTOs/ProdutoDTO';
import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { itemVenda, itemvendaRequest , VendaRequest } from '../modelos/DTOs/VendaDTOs';
import { AuthService } from '../service/auth.service';
import { InputTextModule } from 'primeng/inputtext'; 
import { AutoCompleteModule, AutoCompleteCompleteEvent } from 'primeng/autocomplete';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-venda',
  standalone: true,
  imports:[
    CommonModule,
    InputTextModule,
    FormsModule,
    CardModule,
    InputNumberModule,
    ButtonModule,
    TableModule,
    ToastModule,
    ConfirmDialogModule,
    AutoCompleteModule
  ],
  templateUrl: './venda.component.html',
  styleUrl: './venda.component.css'
})
export class VendaComponent implements OnInit{

  @ViewChild('campoBuscaAC') campoBuscaAC!: any; 
  @ViewChild('campoQtd') campoQtd!: ElementRef; 
  
  codigoOuNome: string = '';
  sugestoesProdutos: ProdutoResponse[] = [];
  produtoEncontrado: ProdutoResponse | null = null; 
  quantidadeManual: number = 1;

  itensDaVenda: itemVenda[] = [];
  valorTotal: number = 0;
  valorRecebido: number | null = null;
  troco: number = 0;

  private todosOsProdutos: ProdutoResponse[] = [];

  constructor(
    private produtoService: ProdutoService,
    private vendaService: VendaService,
    private authService: AuthService,
    private msg: MessageService
  ) { }

  ngOnInit(): void {
    this.produtoService.loading.set(true);
    
    const params : any = {};
        
    params.ativo = true;
    
    this.produtoService.listar(params).pipe(finalize(() => this.produtoService.loading.set(false))).subscribe({
      next: (lista) =>{
        this.todosOsProdutos = lista;
      },
      error: () => {
        this.msg.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao carregar os Produtos' });
      }
    })
  }

  ngAfterViewInit(): void {
    this.focarNoInputBusca();
  }

  buscarProdutos(event: AutoCompleteCompleteEvent): void {
    const query = event.query.toLowerCase();
    this.sugestoesProdutos = this.todosOsProdutos.filter(p =>
      p.nome.toLowerCase().includes(query) ||
      p.codigo.toString().includes(query)
    );
  }

  private focarNoInputBusca(): void {
    setTimeout(() => {
      this.campoBuscaAC.focusInput();
    }, 0);
  }

  private focarNoInputQtd(): void {
    setTimeout(() => {
      this.campoQtd.nativeElement.querySelector('input').focus();
    }, 0);
  }

  private limparFormularioBusca(): void {
    this.codigoOuNome = '';
    this.produtoEncontrado = null;
    this.quantidadeManual = 1;
  }

  onProdutoSelect(event: any): void {
    this.produtoEncontrado = event.value;
    this.quantidadeManual = 1;
    this.focarNoInputQtd(); 
  }

  onEnterBusca(): void {
    const query = this.codigoOuNome.trim();
    if (!query) return;

    const found = this.todosOsProdutos.find(p => p.codigo.toString() === query);

    if (found) {
      this.adicionarItem(found, 1);
      this.limparFormularioBusca();
      this.focarNoInputBusca();
    } else {
    }
  }
  
  adicionarItemManual(): void {
    if (!this.produtoEncontrado || this.quantidadeManual <= 0) {
      this.msg.add({ severity: 'warn', summary: 'Inválido', detail: 'Selecione um produto e quantidade.' });
      return;
    }
    
    this.adicionarItem(this.produtoEncontrado, this.quantidadeManual);
    this.limparFormularioBusca();
    this.focarNoInputBusca();
  }
  
  adicionarItem(produto: ProdutoResponse, quantidade: number): void {
    const itemExistente = this.itensDaVenda.find(
      item => item.produto.id === produto.id
    );

    const qtdTotalNoCarrinho = (itemExistente?.quantidade || 0) + quantidade;

    if (qtdTotalNoCarrinho > produto.quantidadeEstoque) {
      this.msg.add({ severity: 'error', summary: 'Estoque Insuficiente', detail: `Estoque máximo (${produto.quantidadeEstoque}) atingido.` });
      return;
    }

    if (itemExistente) {
      itemExistente.quantidade += quantidade;
    } else {
      this.itensDaVenda.push({
        produto: produto,
        quantidade: quantidade
      });
    }

    this.recalcularTotal();
  }

  removerItem(itemParaRemover: itemVenda): void { 
    this.itensDaVenda = this.itensDaVenda.filter(item => item !== itemParaRemover);
    this.recalcularTotal();
  }
  

  private recalcularTotal(): void {
    this.valorTotal = this.itensDaVenda.reduce((acc, item) => {
      return acc + (item.produto.preco * item.quantidade);
    }, 0);
    this.calcularTroco();
  }

  calcularTroco(): void {
    if (this.valorRecebido && this.valorRecebido >= this.valorTotal) {
      this.troco = this.valorRecebido - this.valorTotal;
    } else {
      this.troco = 0; 
    }
  }

  cancelarVenda(): void {
    this.itensDaVenda = [];
    this.valorRecebido = null;
    this.recalcularTotal();
    this.produtoEncontrado = null;
    this.codigoOuNome = '';
    this.limparFormularioBusca();
    this.focarNoInputBusca();
  }

  finalizarVenda(): void {
    const usuarioLogado = this.authService.getUsuarioLogado();

    if (!usuarioLogado) {
      this.msg.add({ severity: 'error', summary: 'Erro', detail: 'Usuário não autenticado. Faça login novamente.' });
      return;
    }
    
    if (this.itensDaVenda.length === 0 || !this.valorRecebido || this.valorRecebido < this.valorTotal) {
       this.msg.add({ severity: 'warn', summary: 'Atenção', detail: 'Verifique os itens ou o valor recebido.' });
      return;
    }

    const listaItensDTO: itemvendaRequest[] = this.itensDaVenda.map(item => ({
      produtoId: item.produto.id,
      quantidade: item.quantidade,
      precoUnitario: item.produto.preco
    }));

    const requisicaoVenda: VendaRequest = {
      valorRecebido: this.valorRecebido, 
      usuarioId: usuarioLogado.id,
      itens: listaItensDTO
    };

    console.log(requisicaoVenda)
    this.vendaService.criar(requisicaoVenda).subscribe({
      next: (vendaSalva) => {
        this.msg.add({ severity: 'success', summary: 'Sucesso', detail: 'Venda registrada com sucesso!' });
        this.cancelarVenda(); 
      },
      error: (err) => {
        this.msg.add({ severity: 'error', summary: 'Erro', detail: err.error.message || 'Falha ao registrar a venda.' });
      }
    });

  }
}