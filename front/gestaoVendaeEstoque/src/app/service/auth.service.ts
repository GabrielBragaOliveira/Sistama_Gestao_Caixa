import { Injectable } from '@angular/core';
import { Router } from "@angular/router";
import { UsuarioService } from './UsuarioService';
import { MessageService } from 'primeng/api';
import { UsuarioLogin, UsuarioResponse } from '../modelos/DTOs/UsuarioDTOs';
import { finalize } from 'rxjs';
import { ErrorHandlingService } from './ErrorHandlingService';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly USER_KEY = 'usuario_logado';

  constructor(
    private router: Router,
    private service: UsuarioService,
    private msg: MessageService,
    private errorHandler: ErrorHandlingService
  ) { }

  login(usuarioLogin: UsuarioLogin): void {
    this.service.loading.set(true);
    console.log(usuarioLogin)
    this.service.login(usuarioLogin).pipe(finalize(() => this.service.loading.set(false))).subscribe({
      next: (usuario: UsuarioResponse) => {
        localStorage.setItem(this.USER_KEY, JSON.stringify(usuario));
        this.msg.add({ severity: 'success', summary: 'Sucesso', detail: `Bem-vindo, ${usuario.nome}!` });
        this.router.navigate(['/relatorio']);
      },
      error: (err) => this.errorHandler.tratarErroHttp(err)
    });
  }

  getUsuarioLogado(): UsuarioResponse | null {
    const usuario = localStorage.getItem(this.USER_KEY);
    return usuario ? JSON.parse(usuario) : null;
  }

  getIsAuthenticated(): boolean {
    return !!localStorage.getItem(this.USER_KEY);
  }

  sair(): void {
    localStorage.removeItem(this.USER_KEY);
    this.router.navigate(['/login']);
  }
}
