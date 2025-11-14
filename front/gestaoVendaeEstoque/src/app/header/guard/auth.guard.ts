import { CanActivateFn, Router } from '@angular/router';
import { inject } from "@angular/core";
import { AuthService } from '../../service/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const usuario = authService.getUsuarioLogado();

  if (!usuario) {
    alert("Acesso Negado. Faça o Login")
    router.navigate(['/login']);
    return false;
  }

  const rolesPermitidas = route.data?.['roles'] as string[] | undefined;

  if (rolesPermitidas && !rolesPermitidas.includes(usuario.perfil)) {
    alert('Acesso negado. Você não tem permissão para acessar esta página.');
    router.navigate(['/acesso-negado']); 
    return false;
  }

  return true;
};
