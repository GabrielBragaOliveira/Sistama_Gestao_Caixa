import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MessageService, ConfirmationService } from 'primeng/api';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { SelectButtonModule } from 'primeng/selectbutton';
import { DropdownModule } from 'primeng/dropdown';
import { TagModule } from 'primeng/tag';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DialogModule } from 'primeng/dialog';
import { RadioButtonModule } from 'primeng/radiobutton';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { TooltipModule } from 'primeng/tooltip';
import { ProdutoResponse } from '../modelos/DTOs/ProdutoDTO';
import { ProdutoService } from '../service/Produto.Service';
import { finalize } from 'rxjs';
import { CadastroEstoqueComponent } from "./cadastro-estoque/cadastro-estoque.component";
import { AjusteEstoqueComponent } from "./ajuste-estoque/ajuste-estoque.component";

@Component({
  selector: 'app-gestao-estoque',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    SelectButtonModule,
    DropdownModule,
    TagModule,
    ToastModule,
    ConfirmDialogModule,
    DialogModule,
    RadioButtonModule,
    InputNumberModule,
    InputTextareaModule,
    TooltipModule,
    CadastroEstoqueComponent,
    AjusteEstoqueComponent
  ],
  templateUrl: './gestao-estoque.component.html',
  styleUrl: './gestao-estoque.component.css'
})
export class GestaoEstoqueComponent implements OnInit {

  produtos: ProdutoResponse[] = [];

  filtroString: string = '';
  filtroAtivo: boolean | null = null;
  filtroOrdemQtd: 'asc' | 'desc' | null = null;

  private filtroTimeout: any;

  opcoesStatus = [
    { label: 'Todos', value: null },
    { label: 'Ativos', value: true },
    { label: 'Inativos', value: false }
  ];

  opcoesOrdem = [
    { label: 'Ordenar Quantidade', value: null },
    { label: 'Maior ao Menor', value: 'desc' },
    { label: 'Menor ao Maior', value: 'asc' }
  ];

  abrirCadastro: boolean = false;
  idEditando: number | null = null;
  isEdicao: boolean = false;

  modalAjusteVisivel: boolean = false;
  produtoEditando: ProdutoResponse | null = null;

  constructor(
    private produtoService: ProdutoService,
    private msg: MessageService,
    private confirm: ConfirmationService
  ) { }

  ngOnInit(): void {
    this.onFiltroChange();
  }

  onFiltroChange(): void {
    const params: any = {
      filtro: this.filtroString,
      ativo: this.filtroAtivo,
      ordemQtd: this.filtroOrdemQtd
    };

    Object.keys(params).forEach(key => {
      if (params[key] === null || params[key] === '') {
        delete params[key];
      }
    });

    console.log('Buscando produtos com params:', params);

    this.produtoService.listar(params).pipe(finalize(() => this.produtoService.loading.set(false))).subscribe({
      next: (lista) => {
        this.produtos = lista;
      },
      error: () => {
        this.msg.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao carregar os Produtos' });
      }
    })
  }

  onFiltroStringChange(): void {
    clearTimeout(this.filtroTimeout);
    this.filtroTimeout = setTimeout(() => {
      this.onFiltroChange();
    }, 500);
  }

  limparFiltros(): void {
    this.filtroString = '';
    this.filtroAtivo = null;
    this.filtroOrdemQtd = null;
    this.onFiltroChange();
  }

  novoProduto(): void {
    this.abrirCadastro = true;
    this.idEditando = null;
    this.isEdicao = false;
  }

  editar(id: number) {
    this.abrirCadastro = true;
    this.idEditando = id;
    this.isEdicao = true;
  }

  onFormularioFechado(): void {
    this.abrirCadastro = false;
    this.idEditando = null;
    this.onFiltroChange();
  }

  confirmarInativacao(produto: ProdutoResponse): void {
    this.confirm.confirm({
      message: `Tem certeza que deseja inativar o produto "${produto.nome}"?`,
      acceptLabel: 'Sim, inativar',
      rejectLabel: 'Cancelar',
      accept: () => {
        this.produtoService.inativar(produto.id).subscribe({
          next: () => {
            this.msg.add({ severity: 'success', summary: 'Sucesso', detail: 'Produto inativado.' });
            this.onFiltroChange();
          },
          error: () => this.msg.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao inativar produto.' })
        });
      }
    });
  }

  abrirModalAjuste(produto: ProdutoResponse): void {
    this.produtoEditando = produto;
    this.modalAjusteVisivel = true;
  }

  onModalAjusteFechado(): void {
    this.modalAjusteVisivel = false;
    this.produtoEditando = null;
    this.onFiltroChange();
  }

}