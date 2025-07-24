import { AfterViewInit, Component, OnDestroy, OnInit, inject, TemplateRef, ViewChild } from '@angular/core';
import { DegradationService } from 'app/entities/degradation/service/degradation.service';
import { IDegradation } from 'app/entities/degradation/degradation.model';
import { IDelaiIntervention } from 'app/entities/delai-intervention/delai-intervention.model';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AlertComponent } from 'app/shared/alert/alert.component';
import { AlertErrorComponent } from 'app/shared/alert/alert-error.component';
import { SearchBarComponent } from 'app/shared/search-bar/search-bar.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-degradation',
  templateUrl: './degradation.component.html',
  styleUrls: [],
  standalone: true,
  imports: [CommonModule, RouterModule, FontAwesomeModule, AlertComponent, AlertErrorComponent, SearchBarComponent],
})
export class DegradationComponent implements OnInit, AfterViewInit, OnDestroy {
  private degradationService = inject(DegradationService);
  private router = inject(Router);
  private modalService = inject(NgbModal);

  allDegradations: IDegradation[] = [];
  filteredDegradations: IDegradation[] = [];
  selectedDegradations: Set<number> = new Set();
  loading = false;
  errorLoading = false;
  searchQuery = '';

  historiqueDelais: IDelaiIntervention[] = [];
  @ViewChild('historiqueModal') historiqueModal!: TemplateRef<any>;

  /* -------------------------------------------------- */
  ngOnInit(): void {
    this.loadDegradations();
  }
  ngAfterViewInit(): void {}
  ngOnDestroy(): void {}

  /* -------------------------------------------------- */
  loadDegradations(): void {
    this.loading = true;
    this.errorLoading = false;
    this.degradationService.query().subscribe({
      next: res => {
        this.allDegradations = res.body ?? [];
        this.filteredDegradations = this.allDegradations;
        this.loading = false;
      },
      error: () => {
        this.errorLoading = true;
        this.loading = false;
      },
    });
  }

  /* -------------------------------------------------- */
  get isLoading(): boolean {
    return this.loading;
  }
  get degradations(): IDegradation[] {
    return this.filteredDegradations;
  }
  trackId(index: number, item: IDegradation): number {
    return item.id!;
  }

  /* -------------------------------------------------- */
  delete(degradation: IDegradation): void {
    if (confirm(`Supprimer #${degradation.id} ?`)) {
      this.degradationService.delete(degradation.id!).subscribe({
        next: () => this.loadDegradations(),
        error: () => alert('Erreur suppression.'),
      });
    }
  }

  ouvrirFormulaireDelai(degradationId: number): void {
    this.router.navigate(['/delai-intervention/new'], { queryParams: { degradationId } });
  }

  openHistoriqueModal(degradationId: number): void {
    this.degradationService.find(degradationId).subscribe(res => {
      this.historiqueDelais = res.body?.delais ?? [];
      this.modalService.open(this.historiqueModal, { size: 'lg' });
    });
  }

  /* -------------------------------------------------- */
  toggleSelection(id: number): void {
    this.selectedDegradations.has(id) ? this.selectedDegradations.delete(id) : this.selectedDegradations.add(id);
  }

  deleteSelected(): void {
    if (this.selectedDegradations.size === 0) return;
    if (!confirm(`Supprimer ${this.selectedDegradations.size} dégradations ?`)) return;

    Array.from(this.selectedDegradations).forEach(id => this.degradationService.delete(id).subscribe());
    this.selectedDegradations.clear();
    this.loadDegradations();
  }

  /* -------------------------------------------------- */
  onSearch(query: string): void {
    this.searchQuery = query.trim().toLowerCase();
    this.filteredDegradations = this.allDegradations.filter(
      d =>
        (d.localite?.toLowerCase().includes(this.searchQuery) ?? false) ||
        (d.typeAnomalie?.toLowerCase().includes(this.searchQuery) ?? false) ||
        (d.priorite?.toLowerCase().includes(this.searchQuery) ?? false) ||
        (d.contactTemoin?.toLowerCase().includes(this.searchQuery) ?? false) ||
        (d.porteur?.toLowerCase().includes(this.searchQuery) ?? false),
    );
  }
}
