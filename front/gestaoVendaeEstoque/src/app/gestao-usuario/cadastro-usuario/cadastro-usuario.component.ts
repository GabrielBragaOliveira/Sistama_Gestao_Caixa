import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { UsuarioService } from '../../service/UsuarioService';
import { FormGroup, FormControl, FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { Perfils } from '../../enum/Perfil';
import { UsuarioRequest } from '../../modelos/DTOs/UsuarioDTOs';
import { AuthService } from '../../service/auth.service';
import { ButtonModule } from 'primeng/button';
import { SelectButtonModule } from 'primeng/selectbutton';
import { InputTextModule } from 'primeng/inputtext';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { NgIf, CommonModule } from '@angular/common';

@Component({
  selector: 'app-cadastro-usuario',
  standalone: true,
  imports: [
    NgIf,
    CommonModule,
    ReactiveFormsModule,
    SelectButtonModule,
    InputTextModule,
    ButtonModule,
    ToastModule
    ],
  templateUrl: './cadastro-usuario.component.html',
  styleUrl: './cadastro-usuario.component.css'
})
export class CadastroUsuarioComponent implements OnInit {
  @Input() id: number | null = null;
  @Input() isEdicao: boolean = false;
  @Output() fechar = new EventEmitter<boolean>();

  formUsuario: FormGroup<{
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
    private service: UsuarioService,
    private msg: MessageService
  ) {
    this.formUsuario = this.fb.group({
      nome: this.fb.control('', { validators: [Validators.required], nonNullable: true }),
      email: this.fb.control('', { validators: [Validators.required, Validators.email], nonNullable: true }),
      perfil: this.fb.control<Perfils | ''>('', { validators: [Validators.required], nonNullable: true }),
      senha: this.fb.control('', { validators: [Validators.required], nonNullable: true }),
    });
  }

  ngOnInit(): void {
    if (this.isEdicao && this.id) {
      this.carregarUsuarioParaEdicao();
      this.formUsuario.get('senha')?.clearValidators();
      this.formUsuario.get('senha')?.updateValueAndValidity();
    }
  }

  private carregarUsuarioParaEdicao(): void {
    this.service.loading.set(true);
    this.service.buscarPorId(this.id!).subscribe({
      next: (usuario) => {
        this.formUsuario.patchValue({
          nome: usuario.nome,
          email: usuario.email,
          perfil: usuario.perfil,
          senha: usuario.senha,
        });
        this.service.loading.set(false);
      },
      error: () => {
        this.msg.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao carregar dados para edição' });
        this.service.loading.set(false);
        this.cancelar();
      }
    });
  }
  
  salvar(): void{
    if (this.formUsuario.valid) {
      const raw = this.formUsuario.getRawValue();
      const usuario: UsuarioRequest = {
        ...raw,
        perfil: raw.perfil as Perfils 
      };
      if (this.isEdicao && !raw.senha) {
        delete (usuario as any).senha;  
      }
      if (this.isEdicao && this.id) {
        this.service.atualizar(this.id, usuario).subscribe({
          next: () => {
            this.msg.add({ severity: 'success', summary: 'Sucesso', detail: 'Funcionário atualizado' }); 
            this.fechar.emit(true);
          },
          error: (err) => this.tratarErroHttp(err)
        });
      } else {
        this.service.criar(usuario).subscribe({
          next: () => {
            this.msg.add({ severity: 'success', summary: 'Sucesso', detail: 'Funcionário cadastrado' });
            this.cancelar()
          },
          error: (err) => this.tratarErroHttp(err)
        });
      }
    }
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