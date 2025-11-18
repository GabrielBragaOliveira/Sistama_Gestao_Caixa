import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { ProdutoService } from '../../service/Produto.Service';
import { FormGroup, FormControl, FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { Perfils } from '../../enum/Perfil';
import { ProdutoRequest } from '../../modelos/DTOs/ProdutoDTO';
import { ButtonModule } from 'primeng/button';
import { SelectButtonModule } from 'primeng/selectbutton';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { ConfirmationService, MessageService } from 'primeng/api';
import { NgIf, CommonModule } from '@angular/common';
import { finalize } from 'rxjs';
import { ErrorHandlingService } from '../../service/ErrorHandlingService';

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
    InputNumberModule,
  ],
  templateUrl: './cadastro-estoque.component.html',
  styleUrl: './cadastro-estoque.component.css'
})
export class CadastroEstoqueComponent implements OnInit, OnChanges {
  @Input() id: number | null = null;
  @Input() isEdicao: boolean = false;
  @Output() fechar = new EventEmitter();

  formProduto: FormGroup<{
    codigo: FormControl<number | null>;
    nome: FormControl<string>;
    categoria: FormControl<string>;
    preco: FormControl<number | null>;
  }>;

  perfis = [
    { label: 'Administrador', value: Perfils.ADMIN },
    { label: 'Operador', value: Perfils.OPERADOR },
  ];

  constructor(
    private fb: FormBuilder,
    private service: ProdutoService,
    private errorHandler: ErrorHandlingService,
    private msg: MessageService,
    private confirm: ConfirmationService
  ) {
    this.formProduto = this.fb.group({
      codigo: this.fb.control<number | null>(null, { validators: [Validators.required, Validators.min(1)], nonNullable: true }),
      nome: this.fb.control('', { validators: [Validators.required], nonNullable: true }),
      categoria: this.fb.control('', { validators: [Validators.required], nonNullable: true }),
      preco: this.fb.control<number | null>(null, { validators: [Validators.required, Validators.min(0.01)], nonNullable: true })
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['id'] || changes['isEdicao']) {
      this.configurarFormulario();
    }
  }

  ngOnInit(): void {
    this.configurarFormulario();
  }

  private limparFormulario(): void {
    this.formProduto.reset({
      codigo: 0,
      nome: '',
      categoria: '',
      preco: 0
    });
  }

  private configurarFormulario(): void {
    this.limparFormulario();
    if (this.isEdicao && this.id) {
      this.carregarProdutoParaEdicao();
    }
  }

  private carregarProdutoParaEdicao(): void {
    this.service.loading.set(true);
    this.service.buscarPorId(this.id!).pipe(finalize(() => this.service.loading.set(false))).subscribe({
      next: (Produto) => {
        this.formProduto.patchValue({
          codigo: Produto.codigo,
          nome: Produto.nome,
          categoria: Produto.categoria,
          preco: Produto.preco,
        });
      },
      error: (err) => {
        this.errorHandler.tratarErroHttp(err)
        this.fechar.emit();
      }
    });
  }

  salvar(): void {
    if (this.formProduto.valid) {
      const rawValue = this.formProduto.getRawValue();

      const Produto: ProdutoRequest = {
        codigo: rawValue.codigo!,
        nome: rawValue.nome,
        categoria: rawValue.categoria,
        preco: rawValue.preco!
      };

      const acao = this.isEdicao && this.id
        ? this.service.atualizar(this.id!, Produto)
        : this.service.criar(Produto);

      this.service.loading.set(true);

      const msgSucesso = this.isEdicao ? 'Produto atualizado' : 'Produto cadastrado';
      acao.pipe(finalize(() => this.service.loading.set(false))).subscribe({
        next: () => {
          this.msg.add({ severity: 'success', summary: 'Sucesso', detail: msgSucesso });
          if (this.isEdicao) {
            this.fechar.emit();
          } else {
            this.cancelar();
          }
        },
        error: (err) => {
          this.errorHandler.tratarErroHttp(err)
          this.fechar.emit();
        }
      });
    }
  }

  cancelar(): void {

    if (this.formProduto.dirty) {

      this.confirm.confirm({
        message: 'Você tem alterações não salvas. Deseja realmente fechar?',
        header: 'Confirmação',
        icon: 'pi pi-exclamation-triangle',
        acceptLabel: 'Sim, fechar',
        rejectLabel: 'Não, ficar',
        accept: () => {
          this.fechar.emit();
        }
      });

    } else {
      this.fechar.emit();
    }
  }

}