import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { ProdutoService } from '../../service/Produto.Service';
import { FormGroup, FormControl, FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { Perfils } from '../../enum/Perfil';
import { ProdutoRequest } from '../../modelos/DTOs/ProdutoDTO';
import { ButtonModule } from 'primeng/button';
import { SelectButtonModule } from 'primeng/selectbutton';
import { InputTextModule } from 'primeng/inputtext';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { NgIf, CommonModule } from '@angular/common';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-cadastro-estoque',
  standalone: true,
  imports: [
    NgIf,
    CommonModule,
    ReactiveFormsModule,
    SelectButtonModule,
    InputTextModule,
    ButtonModule,
    ToastModule,
  ],
  templateUrl: './cadastro-estoque.component.html',
  styleUrl: './cadastro-estoque.component.css'
})
export class CadastroEstoqueComponent implements OnInit, OnChanges {
  @Input() id: number | null = null;
  @Input() isEdicao: boolean = false;
  @Output() fechar = new EventEmitter<boolean>();

  formProduto: FormGroup<{
    nome: FormControl<string>; 
    email: FormControl<string>;
    perfil: FormControl<Perfils|''>;
    senha: FormControl<string>;
  }>;

  perfis = [
    { label: 'Administrador', value: Perfils.ADMIN },
    { label: 'Operador', value: Perfils.OPERADOR },
  ];

  constructor(
    private fb: FormBuilder,
    private service: ProdutoService,
    private msg: MessageService,
    private confirm: ConfirmationService
  ) {
    this.formProduto = this.fb.group({
      nome: this.fb.control('', { validators: [Validators.required], nonNullable: true }),
      email: this.fb.control('', { validators: [Validators.required, Validators.email], nonNullable: true }),
      perfil: this.fb.control<Perfils | ''>('', { validators: [Validators.required], nonNullable: true }),
      senha: this.fb.control('', { validators: [Validators.required], nonNullable: true }),
    });
  }

  ngOnInit(): void {
    if (this.isEdicao && this.id) {
      this.carregarProdutoParaEdicao();
      this.formProduto.get('senha')?.clearValidators();
      this.formProduto.get('senha')?.updateValueAndValidity();
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['id'] || changes['isEdicao']) {
      this.configurarFormulario();
    }
  }

  private configurarFormulario(): void {
    this.formProduto.reset();

    this.formProduto.get('senha')?.setValidators([Validators.required]);
    this.formProduto.get('senha')?.updateValueAndValidity();

    if (this.isEdicao && this.id) {
      this.carregarProdutoParaEdicao();
      
      this.formProduto.get('senha')?.clearValidators();
      this.formProduto.get('senha')?.updateValueAndValidity();
    }
  }

  private carregarProdutoParaEdicao(): void {
    this.service.loading.set(true);/*
    this.service.buscarPorId(this.id!).subscribe({
      next: (Produto) => {
        this.formProduto.patchValue({
          nome: Produto.nome,
          email: Produto.email,
          perfil: Produto.perfil,
          senha: Produto.senha, 
        });*/
        this.service.loading.set(false);/*
      },
      error: () => {
        this.msg.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao carregar dados para edição' });
        this.service.loading.set(false);
        this.cancelar();
      }
    });*/
  }
  
  salvar(): void{
    /*
    if (this.formProduto.valid) {
      const raw = this.formProduto.getRawValue();
      const Produto: ProdutoRequest = {
        ...raw,
        perfil: raw.perfil as Perfils 
      };

      if (this.isEdicao && !raw.senha) {
        delete (Produto as any).senha; 
      }
      const acao = this.isEdicao && this.id 
        ? this.service.atualizar(this.id!, Produto) 
        : this.service.criar(Produto);

      this.service.loading.set(true);

      const msgSucesso = this.isEdicao ? 'Funcionário atualizado' : 'Funcionário cadastrado';
      acao.pipe(finalize(() => this.service.loading.set(false))).subscribe({
        next: () => {
          this.msg.add({ severity: 'success', summary: 'Sucesso', detail: msgSucesso });
          if (this.isEdicao) {
            this.fechar.emit(true); 
          } else {
            this.cancelar(); 
          }
        },
        error: (err) => this.tratarErroHttp(err)
      });
    }*/
  }

  cancelar(): void {
    this.fechar.emit(false);
  }

  private tratarErroHttp(err: any) {
    const status = err?.status;
    if (status === 409) {
      this.msg.add({ severity: 'warn', summary: 'Conflito', detail: 'E-mail já cadastrado' });
    } else if (status === 400) {
      this.msg.add({ severity: 'warn', summary: 'Regras', detail: err?.error?.message || 'Violação de regra de negócio' });
    } else {
      this.msg.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao salvar' });
    }
  }
}