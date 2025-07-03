import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDegradation } from 'app/entities/degradation/degradation.model';
import { DegradationService } from 'app/entities/degradation/service/degradation.service';
import { IHistorique } from '../historique.model';
import { HistoriqueService } from '../service/historique.service';
import { HistoriqueFormGroup, HistoriqueFormService } from './historique-form.service';

@Component({
  selector: 'jhi-historique-update',
  templateUrl: './historique-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class HistoriqueUpdateComponent implements OnInit {
  isSaving = false;
  historique: IHistorique | null = null;

  degradationsSharedCollection: IDegradation[] = [];

  protected historiqueService = inject(HistoriqueService);
  protected historiqueFormService = inject(HistoriqueFormService);
  protected degradationService = inject(DegradationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: HistoriqueFormGroup = this.historiqueFormService.createHistoriqueFormGroup();

  compareDegradation = (o1: IDegradation | null, o2: IDegradation | null): boolean => this.degradationService.compareDegradation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ historique }) => {
      this.historique = historique;
      if (historique) {
        this.updateForm(historique);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const historique = this.historiqueFormService.getHistorique(this.editForm);
    if (historique.id !== null) {
      this.subscribeToSaveResponse(this.historiqueService.update(historique));
    } else {
      this.subscribeToSaveResponse(this.historiqueService.create(historique));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHistorique>>): void {
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

  protected updateForm(historique: IHistorique): void {
    this.historique = historique;
    this.historiqueFormService.resetForm(this.editForm, historique);

    this.degradationsSharedCollection = this.degradationService.addDegradationToCollectionIfMissing<IDegradation>(
      this.degradationsSharedCollection,
      historique.degradation,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.degradationService
      .query()
      .pipe(map((res: HttpResponse<IDegradation[]>) => res.body ?? []))
      .pipe(
        map((degradations: IDegradation[]) =>
          this.degradationService.addDegradationToCollectionIfMissing<IDegradation>(degradations, this.historique?.degradation),
        ),
      )
      .subscribe((degradations: IDegradation[]) => (this.degradationsSharedCollection = degradations));
  }
}
