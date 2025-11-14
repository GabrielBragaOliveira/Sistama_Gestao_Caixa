import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RelatorioComponent } from './relatorio/relatorio.component';
import { VendaComponent } from './venda/venda.component';
import { GestaoUsuarioComponent } from './gestao-usuario/gestao-usuario.component';
import { GestaoEstoqueComponent } from './gestao-estoque/gestao-estoque.component';
import { authGuard } from './header/guard/auth.guard';
import { deactivateGuard } from './header/guard/deactivate.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  {
    path: 'relatorio', component: RelatorioComponent,
    canActivate: [authGuard],
    canDeactivate: [deactivateGuard],
    data: { roles: ['ADMIN', 'OPERADOR'] }
  },
  {
    path: 'venda', component: VendaComponent,
    canActivate: [authGuard],
    canDeactivate: [deactivateGuard],
    data: { roles: ['OPERADOR'] }
  },
  {
    path: 'usuario', component: GestaoUsuarioComponent,
    canActivate: [authGuard],
    canDeactivate: [deactivateGuard],
    data: { roles: ['ADMIN'] }
  },
  {
    path: 'estoque', component: GestaoEstoqueComponent,
    canActivate: [authGuard],
    canDeactivate: [deactivateGuard],
    data: { roles: ['ADMIN'] }
  },
];
