import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { AlertErrorComponent } from 'app/shared/alert/alert-error.component';

import { IDegradation } from 'app/entities/degradation/degradation.model';
import { DegradationService } from 'app/entities/degradation/service/degradation.service';

import { INotification } from '../notification.model';
import { NotificationService } from '../service/notification.service';

import { NotificationFormGroup, NotificationFormService } from './notification-form.service';

@Component({
  selector: 'jhi-notification-update',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    FontAwesomeModule,
    AlertErrorComponent, // CORRECTION ICI
  ],
  templateUrl: './notification-update.component.html',
})
export class NotificationUpdateComponent implements OnInit {
  isSaving = false;
  notification: INotification | null = null;

  degradationsSharedCollection: IDegradation[] = [];

  protected notificationService = inject(NotificationService);
  protected notificationFormService = inject(NotificationFormService);
  protected degradationService = inject(DegradationService);
  protected activatedRoute = inject(ActivatedRoute);

  editForm: NotificationFormGroup = this.notificationFormService.createNotificationFormGroup();

  compareDegradation = (o1: IDegradation | null, o2: IDegradation | null): boolean => this.degradationService.compareDegradation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notification }) => {
      this.notification = notification;
      if (notification) {
        this.updateForm(notification);
      }
      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const notification = this.notificationFormService.getNotification(this.editForm);

    let saveObservable: Observable<HttpResponse<INotification>>;
    if (notification.id !== null) {
      saveObservable = this.notificationService.update(notification);
    } else {
      saveObservable = this.notificationService.create(notification);
    }

    this.subscribeToSaveResponse(saveObservable);
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INotification>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // méthode à overrider si besoin
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(notification: INotification): void {
    this.notification = notification;
    this.notificationFormService.resetForm(this.editForm, notification);

    this.degradationsSharedCollection = this.degradationService.addDegradationToCollectionIfMissing(
      this.degradationsSharedCollection,
      notification.degradation,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.degradationService
      .query()
      .pipe(map((res: HttpResponse<IDegradation[]>) => res.body ?? []))
      .pipe(
        map((degradations: IDegradation[]) =>
          this.degradationService.addDegradationToCollectionIfMissing(degradations, this.notification?.degradation),
        ),
      )
      .subscribe((degradations: IDegradation[]) => (this.degradationsSharedCollection = degradations));
  }
}
