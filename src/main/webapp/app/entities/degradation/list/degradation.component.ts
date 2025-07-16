import { AfterViewInit, Component, OnDestroy, OnInit, inject } from '@angular/core';
import { DegradationService } from 'app/entities/degradation/service/degradation.service';
import { IDegradation } from 'app/entities/degradation/degradation.model';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AlertComponent } from 'app/shared/alert/alert.component';
import { AlertErrorComponent } from 'app/shared/alert/alert-error.component';

@Component({
  selector: 'jhi-degradation',
  templateUrl: './degradation.component.html',
  styleUrls: [],
  standalone: true,
  imports: [CommonModule, RouterModule, FontAwesomeModule, AlertComponent, AlertErrorComponent],
})
export class DegradationComponent implements OnInit, AfterViewInit, OnDestroy {
  private degradationService = inject(DegradationService);
  private router = inject(Router);

  allDegradations: IDegradation[] = [];
  loading = false;
  errorLoading = false;

  sortState: any = { predicate: 'id', ascending: true };

  ngOnInit(): void {
    this.load();
  }

  ngAfterViewInit(): void {
    // Rien à faire ici sans carte
  }

  ngOnDestroy(): void {
    // Rien à faire ici sans carte
  }

  loadDegradations(): void {
    this.loading = true;
    this.errorLoading = false;

    this.degradationService.query().subscribe({
      next: response => {
        this.allDegradations = response.body ?? [];
        this.loading = false;
      },
      error: err => {
        console.error('Erreur chargement dégradations', err);
        this.errorLoading = true;
        this.loading = false;
      },
    });
  }

  get isLoading(): boolean {
    return this.loading;
  }

  get degradations(): IDegradation[] {
    return this.allDegradations;
  }

  load(): void {
    this.loadDegradations();
  }

  trackId(index: number, item: IDegradation): number {
    return item.id!;
  }

  delete(degradation: IDegradation): void {
    if (confirm(`Voulez-vous vraiment supprimer la dégradation #${degradation.id} ?`)) {
      this.degradationService.delete(degradation.id!).subscribe({
        next: () => {
          alert(`Dégradation #${degradation.id} supprimée avec succès.`);
          this.loadDegradations();
        },
        error: err => {
          console.error('Erreur lors de la suppression', err);
          alert('Erreur lors de la suppression.');
        },
      });
    }
  }

  ouvrirFormulaireDelai(degradationId: number): void {
    this.router.navigate(['/delai-intervention/new'], {
      queryParams: { degradationId },
    });
  }
}
