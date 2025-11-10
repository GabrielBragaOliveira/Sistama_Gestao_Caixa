import { Injectable } from '@angular/core';
import {Router} from "@angular/router";
import { UsuarioService } from './UsuarioService';
import { MessageService } from 'primeng/api';
import { UsuarioLogin, UsuarioResponse } from '../modelos/DTOs/UsuarioDTOs';
import { finalize } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly USER_KEY = 'usuario_logado';

  constructor(
    private router: Router,
    private service: UsuarioService,
    private msg: MessageService
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
      error: (err) => this.tratarErroHttp(err)
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

  private tratarErroHttp(err: any) {
    this.service.loading.set(false);
    const status = err?.status;
    if (status === 404) {
      this.msg.add({ severity: 'error', summary: 'Erro ', detail: 'E-mail ou Senha não encontrado' });
    } else {
      this.msg.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao tentar fazer login. Verifique a sua conexão ou tente novamente mais tarde.' });
    }
  }
}
