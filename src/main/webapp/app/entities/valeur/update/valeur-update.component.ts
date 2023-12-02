import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IValeur } from '../valeur.model';
import { ValeurService } from '../service/valeur.service';
import { ValeurFormService, ValeurFormGroup } from './valeur-form.service';

@Component({
  standalone: true,
  selector: 'jhi-valeur-update',
  templateUrl: './valeur-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ValeurUpdateComponent implements OnInit {
  isSaving = false;
  valeur: IValeur | null = null;

  valeursSharedCollection: IValeur[] = [];

  editForm: ValeurFormGroup = this.valeurFormService.createValeurFormGroup();

  constructor(
    protected valeurService: ValeurService,
    protected valeurFormService: ValeurFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareValeur = (o1: IValeur | null, o2: IValeur | null): boolean => this.valeurService.compareValeur(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ valeur }) => {
      this.valeur = valeur;
      if (valeur) {
        this.updateForm(valeur);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const valeur = this.valeurFormService.getValeur(this.editForm);
    if (valeur.id !== null) {
      this.subscribeToSaveResponse(this.valeurService.update(valeur));
    } else {
      this.subscribeToSaveResponse(this.valeurService.create(valeur));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IValeur>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(valeur: IValeur): void {
    this.valeur = valeur;
    this.valeurFormService.resetForm(this.editForm, valeur);

    this.valeursSharedCollection = this.valeurService.addValeurToCollectionIfMissing<IValeur>(
      this.valeursSharedCollection,
      valeur.valeurParent,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.valeurService
      .query()
      .pipe(map((res: HttpResponse<IValeur[]>) => res.body ?? []))
      .pipe(map((valeurs: IValeur[]) => this.valeurService.addValeurToCollectionIfMissing<IValeur>(valeurs, this.valeur?.valeurParent)))
      .subscribe((valeurs: IValeur[]) => (this.valeursSharedCollection = valeurs));
  }
}
