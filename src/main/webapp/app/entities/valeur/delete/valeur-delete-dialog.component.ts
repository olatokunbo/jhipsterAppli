import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IValeur } from '../valeur.model';
import { ValeurService } from '../service/valeur.service';

@Component({
  standalone: true,
  templateUrl: './valeur-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ValeurDeleteDialogComponent {
  valeur?: IValeur;

  constructor(
    protected valeurService: ValeurService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.valeurService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
