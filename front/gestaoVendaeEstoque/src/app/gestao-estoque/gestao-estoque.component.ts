import { Component, OnInit } from '@angular/core';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TagModule } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { SelectButtonModule } from 'primeng/selectbutton';
import { Perfils } from '../enum/Perfil';
import { MessageService, ConfirmationService } from 'primeng/api';
import { finalize } from 'rxjs';
import { CadastroEstoqueComponent } from "./cadastro-estoque/cadastro-estoque.component";
import { DialogModule } from 'primeng/dialog';
import { ProdutoService } from '../service/Produto.Service';
import { ProdutoResponse } from '../modelos/DTOs/ProdutoDTO';

@Component({
  selector: 'app-gestao-estoque',
  standalone: true,
  imports: [
    ToastModule,
    ConfirmDialogModule,
    CommonModule,
    FormsModule,
    TagModule,
    TableModule,
    InputTextModule,
    SelectButtonModule,
    ButtonModule,
    CadastroEstoqueComponent,
    DialogModule
  ],
  templateUrl: './gestao-estoque.component.html',
  styleUrl: './gestao-estoque.component.css'
})
export class GestaoEstoqueComponent implements OnInit{

  filtroString: string = '';
  filtroAtivo: boolean | null = null;
  produtos: ProdutoResponse[] = [];
  abrirCadastro = false;
  idEditando: number | null = null;
  isEdicao: boolean = false;
  private filtroTimeout: any;

  statusOptionsAtivo = [
    { label: 'Todos', value: null },
    { label: 'Ativos', value: true },
    { label: 'Inativos', value: false }
  ];

  constructor(
    protected service: ProdutoService,
    private msg: MessageService,
    private confirm: ConfirmationService,
  ) {}

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void{
    this.service.loading.set(true);

    const params : any = {};
    
    if (this.filtroString.trim()) params.filtro = this.filtroString.trim();
    if (this.filtroAtivo !== null) params.ativo = this.filtroAtivo;

    this.service.listar(params).pipe(finalize(() => this.service.loading.set(false))).subscribe({
      next: (lista) =>{
        this.produtos = lista;
      },
      error: () => {
        this.msg.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao carregar os Produtos' });
      }
    })
  }

  limparFiltros(): void {
    this.filtroString = '';
    this.filtroAtivo = null;
    this.carregar();
  }

  confirmarInativacao(produto: ProdutoResponse): void {
    this.confirm.confirm({
      message: `Confirma inativar ${produto.nome}?`,
      header: 'Confirmar',
      acceptLabel: 'Sim',
      rejectLabel: 'Não',
      accept: () => this.inativar(produto.id)
    });
  }

  private inativar(id: number): void {
    this.service.loading.set(true);
    this.service.inativar(id).pipe(finalize(() => this.service.loading.set(false))).subscribe({
      next: () => {
        this.msg.add({ severity: 'success', summary: 'Sucesso', detail: 'Produto inativado' });
        this.carregar();
      },
      error: () => {
        this.msg.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível inativar' });
        this.service.loading.set(false);
      }
    });
  }

  onFiltroStringChange(): void {
    clearTimeout(this.filtroTimeout);
    this.filtroTimeout = setTimeout(() => {
      this.carregar();
    }, 500);
  }

  novo() : void{
    this.abrirCadastro = true
    this.idEditando = null
    this.isEdicao = false
  }
  editar(id: number) : void {
    this.abrirCadastro = true
    this.idEditando = id
    this.isEdicao = true
  }

  onFormularioFechado(recarregar: boolean): void {
    this.abrirCadastro = false;
    this.idEditando = null;

    if (recarregar) {
      this.carregar();
    }
  }
}

