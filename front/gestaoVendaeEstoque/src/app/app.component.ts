import { Component, OnInit } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { SideBarComponent } from "./header/side-bar/side-bar.component";
import { AuthService } from './service/auth.service';
import { Perfils } from './enum/Perfil';
import { CommonModule } from '@angular/common';
import { UsuarioResponse } from './modelos/DTOs/UsuarioDTOs';
import { Nullable } from 'primeng/ts-helpers';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet, 
    SideBarComponent,
    CommonModule
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent{
  userName: string = '';
  userRole: Perfils | '' = '';

  constructor(
    private auth: AuthService,
    private router: Router
  ) {}

  private atualizarUsuario(): void {
    const usuario: UsuarioResponse | null = this.auth.getUsuarioLogado();
    if (usuario) {
      this.userName = usuario.nome;
      this.userRole = usuario.perfil as Perfils;
    }
  }

  onLogout(): void {
    this.auth.sair();
    this.userName = '';
    this.userRole = '';
  }

  isLayoutVisible(): boolean {
    const hiddenRoutes = ['/login']; 
    this.atualizarUsuario();
    return !hiddenRoutes.includes(this.router.url);
  }
}
