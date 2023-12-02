import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IValeur } from 'app/entities/valeur/valeur.model';
import { ValeurService } from 'app/entities/valeur/service/valeur.service';
import { IUniteAdministrative } from '../unite-administrative.model';
import { UniteAdministrativeService } from '../service/unite-administrative.service';
import { UniteAdministrativeFormService, UniteAdministrativeFormGroup } from './unite-administrative-form.service';

@Component({
  standalone: true,
  selector: 'jhi-unite-administrative-update',
  templateUrl: './unite-administrative-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UniteAdministrativeUpdateComponent implements OnInit {
  isSaving = false;
  uniteAdministrative: IUniteAdministrative | null = null;

  uniteAdministrativesSharedCollection: IUniteAdministrative[] = [];
  valeursSharedCollection: IValeur[] = [];

  editForm: UniteAdministrativeFormGroup = this.uniteAdministrativeFormService.createUniteAdministrativeFormGroup();

  constructor(
    protected uniteAdministrativeService: UniteAdministrativeService,
    protected uniteAdministrativeFormService: UniteAdministrativeFormService,
    protected valeurService: ValeurService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareUniteAdministrative = (o1: IUniteAdministrative | null, o2: IUniteAdministrative | null): boolean =>
    this.uniteAdministrativeService.compareUniteAdministrative(o1, o2);

  compareValeur = (o1: IValeur | null, o2: IValeur | null): boolean => this.valeurService.compareValeur(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ uniteAdministrative }) => {
      this.uniteAdministrative = uniteAdministrative;
      if (uniteAdministrative) {
        this.updateForm(uniteAdministrative);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const uniteAdministrative = this.uniteAdministrativeFormService.getUniteAdministrative(this.editForm);
    if (uniteAdministrative.id !== null) {
      this.subscribeToSaveResponse(this.uniteAdministrativeService.update(uniteAdministrative));
    } else {
      this.subscribeToSaveResponse(this.uniteAdministrativeService.create(uniteAdministrative));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUniteAdministrative>>): void {
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

  protected updateForm(uniteAdministrative: IUniteAdministrative): void {
    this.uniteAdministrative = uniteAdministrative;
    this.uniteAdministrativeFormService.resetForm(this.editForm, uniteAdministrative);

    this.uniteAdministrativesSharedCollection =
      this.uniteAdministrativeService.addUniteAdministrativeToCollectionIfMissing<IUniteAdministrative>(
        this.uniteAdministrativesSharedCollection,
        uniteAdministrative.uniteAdministrativeParent,
      );
    this.valeursSharedCollection = this.valeurService.addValeurToCollectionIfMissing<IValeur>(
      this.valeursSharedCollection,
      uniteAdministrative.typeUniteAdministrative,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.uniteAdministrativeService
      .query()
      .pipe(map((res: HttpResponse<IUniteAdministrative[]>) => res.body ?? []))
      .pipe(
        map((uniteAdministratives: IUniteAdministrative[]) =>
          this.uniteAdministrativeService.addUniteAdministrativeToCollectionIfMissing<IUniteAdministrative>(
            uniteAdministratives,
            this.uniteAdministrative?.uniteAdministrativeParent,
          ),
        ),
      )
      .subscribe((uniteAdministratives: IUniteAdministrative[]) => (this.uniteAdministrativesSharedCollection = uniteAdministratives));

    this.valeurService
      .query()
      .pipe(map((res: HttpResponse<IValeur[]>) => res.body ?? []))
      .pipe(
        map((valeurs: IValeur[]) =>
          this.valeurService.addValeurToCollectionIfMissing<IValeur>(valeurs, this.uniteAdministrative?.typeUniteAdministrative),
        ),
      )
      .subscribe((valeurs: IValeur[]) => (this.valeursSharedCollection = valeurs));
  }
}
