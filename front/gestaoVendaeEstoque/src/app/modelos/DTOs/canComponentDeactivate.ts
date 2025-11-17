import { Observable } from 'rxjs';

export interface CanComponentDeactivate {
  podeDesativar: () => boolean;
  aoDesativar?: () => void;
}