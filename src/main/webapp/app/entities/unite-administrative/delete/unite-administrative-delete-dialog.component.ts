import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUniteAdministrative } from '../unite-administrative.model';
import { UniteAdministrativeService } from '../service/unite-administrative.service';

@Component({
  standalone: true,
  templateUrl: './unite-administrative-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UniteAdministrativeDeleteDialogComponent {
  uniteAdministrative?: IUniteAdministrative;

  constructor(
    protected uniteAdministrativeService: UniteAdministrativeService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.uniteAdministrativeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
