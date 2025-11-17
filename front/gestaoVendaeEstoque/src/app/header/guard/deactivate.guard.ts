import { CanDeactivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { ConfirmationService } from 'primeng/api';
import { Observable } from 'rxjs';
import { CanComponentDeactivate } from '../../modelos/DTOs/canComponentDeactivate';

export const deactivateGuard: CanDeactivateFn<CanComponentDeactivate> = (component) => {

  if (typeof component.podeDesativar !== 'function') {
    return true; 
  }

  if (component.podeDesativar()) {
    return true;
  }
  
  const confirmationService = inject(ConfirmationService);

  return new Observable<boolean>((observer) => {
    confirmationService.confirm({
      message: 'Você tem uma venda em andamento. Deseja realmente sair e cancelar a venda?',
      header: 'Venda não finalizada',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sair e Cancelar',
      rejectLabel: 'Ficar na página',
      accept: () => {

        if (component.aoDesativar) {
          component.aoDesativar();
        }

        observer.next(true);
        observer.complete();
      },
      reject: () => {
        observer.next(false);
        observer.complete();
      }
    });
  });
};