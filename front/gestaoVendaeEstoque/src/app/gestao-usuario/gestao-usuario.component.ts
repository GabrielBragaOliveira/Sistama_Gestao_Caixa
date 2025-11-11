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
import { UsuarioService } from '../service/UsuarioService';
import { MessageService, ConfirmationService } from 'primeng/api';
import { UsuarioResponse } from '../modelos/DTOs/UsuarioDTOs';
import { CadastroUsuarioComponent } from "./cadastro-usuario/cadastro-usuario.component";
import { finalize } from 'rxjs';
import { DialogModule } from 'primeng/dialog';

@Component({
  selector: 'app-gestao-usuario',
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
    CadastroUsuarioComponent,
    DialogModule
],
  templateUrl: './gestao-usuario.component.html',
  styleUrl: './gestao-usuario.component.css'
})
export class GestaoUsuarioComponent implements OnInit{

  filtroString: string = '';
  filtroAtivo: boolean | null = null;
  filtroPerfil: Perfils | null = null;
  usuarios: UsuarioResponse[] = [];
  abrirCadastro = false;
  idEditando: number | null = null;
  isEdicao: boolean = false;
  private filtroTimeout: any;

  statusOptionsAtivo = [
    { label: 'Todos', value: null },
    { label: 'Ativos', value: true },
    { label: 'Inativos', value: false }
  ];

  statusOptionsPerfil = [
    { label: 'Todos', value: null },
    { label: 'Operador', value: 'OPERADOR' },
    { label: 'Administrador', value: 'ADMIN' }
  ];

  constructor(
    protected service: UsuarioService,
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
    if (this.filtroPerfil !== null) params.perfil = this.filtroPerfil;

    this.service.listar(params).pipe(finalize(() => this.service.loading.set(false))).subscribe({
      next: (lista) =>{
        this.usuarios = lista;
      },
      error: () => {
        this.msg.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao carregar os Usuarios' });
      }
    })
  }

  limparFiltros(): void {
    this.filtroString = '';
    this.filtroAtivo = null;
    this.filtroPerfil = null;
    this.carregar();
  }

  confirmarInativacao(usuario: UsuarioResponse): void {
    this.confirm.confirm({
      message: `Confirma inativar ${usuario.nome}?`,
      header: 'Confirmar',
      acceptLabel: 'Sim',
      rejectLabel: 'Não',
      accept: () => this.inativar(usuario.id)
    });
  }

  private inativar(id: number): void {
    this.service.loading.set(true);
    this.service.inativar(id).pipe(finalize(() => this.service.loading.set(false))).subscribe({
      next: () => {
        this.msg.add({ severity: 'success', summary: 'Sucesso', detail: 'Usuario inativado' });
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
