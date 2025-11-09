import { CanDeactivateFn } from '@angular/router';
import { RelatorioComponent } from '../../relatorio/relatorio.component';

export const deactivateGuard: CanDeactivateFn<RelatorioComponent> = (component, currentRoute, currentState, nextState) => {
  //if (!component.isSalvo){
    //return confirm("Seus dados não foram salvos. Deseja continuar?")
  //}
  return true;
};
