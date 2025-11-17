import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { PanelMenuModule } from 'primeng/panelmenu';
import { Perfils } from '../../enum/Perfil';
import { InputSwitchModule } from 'primeng/inputswitch';
import { FormsModule } from '@angular/forms';
import { TemaService } from '../../service/temaService';

@Component({
  selector: 'app-side-bar',
  standalone: true,
  imports: [
    CommonModule,
    PanelMenuModule,
    ButtonModule,
    InputSwitchModule,
    FormsModule
  ],
  templateUrl: './side-bar.component.html',
  styleUrl: './side-bar.component.css'
})
export class SideBarComponent implements OnInit {
  @Input() role: Perfils | '' = '';
  @Input() username: string = 'Usuário';
  @Output() logout = new EventEmitter<void>();
  @Output() stateChanged = new EventEmitter<boolean>();

  sidebarAberta = false;
  items: MenuItem[] = [];

  constructor(public temaService: TemaService) { }

  ngOnInit(): void {
    this.montarMenu();
    this.stateChanged.emit(this.sidebarAberta);
  }

  private montarMenu(): void {
    switch (this.role) {
      case 'ADMIN':
        this.items = [
          {
            label: 'Administrador',
            icon: 'pi pi-shield',
            items: [
              { label: 'Gestão de Usuários', icon: 'pi pi-users', routerLink: ['/usuario'] },
              { label: 'Gestão de Estoque', icon: 'pi pi-box', routerLink: ['/estoque'] },
              { label: 'Relatórios', icon: 'pi pi-chart-line', routerLink: ['/relatorio'] }
            ]
          }
        ];
        this.toggleSidebar();
        break;

      case 'OPERADOR':
        this.items = [
          {
            label: 'Operador',
            icon: 'pi pi-briefcase',
            items: [
              { label: 'Vendas', icon: 'pi pi-shopping-cart', routerLink: ['/venda'] },
              { label: 'Relatórios', icon: 'pi pi-chart-line', routerLink: ['/relatorio'] }
            ]
          }
        ];
        this.toggleSidebar();
        break;

      default:
        this.items = [];
    }
  }

  onLogout(): void {
    this.logout.emit();
  }

  toggleSidebar(): void {
    this.sidebarAberta = !this.sidebarAberta;
    this.stateChanged.emit(this.sidebarAberta);
  }

  sair(): void {
    this.logout.emit();
  }

  onThemeToggle(): void {
    this.temaService.toggleTheme();
  }
}

