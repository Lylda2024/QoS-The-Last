import { Component, NgZone, OnInit, inject, signal } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { Observable, Subscription, combineLatest, filter, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { SearchBarComponent } from 'app/shared/search-bar/search-bar.component';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { ItemCountComponent } from 'app/shared/pagination';
import { FormsModule } from '@angular/forms';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { IUtilisateur } from '../utilisateur.model';
import { EntityArrayResponseType, UtilisateurService } from '../service/utilisateur.service';
import { UtilisateurDeleteDialogComponent } from '../delete/utilisateur-delete-dialog.component';

@Component({
  selector: 'jhi-utilisateur',
  standalone: true,
  templateUrl: './utilisateur.component.html',
  imports: [RouterModule, FormsModule, SharedModule, SortDirective, SortByDirective, ItemCountComponent, SearchBarComponent],
})
export class UtilisateurComponent implements OnInit {
  subscription: Subscription | null = null;
  utilisateurs = signal<IUtilisateur[]>([]);
  selectedUtilisateurs: Set<number> = new Set();
  isLoading = false;
  searchQuery: string = '';

  sortState = sortStateSignal({});

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  public readonly router = inject(Router);
  protected readonly utilisateurService = inject(UtilisateurService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);

  trackId = (item: IUtilisateur): number => this.utilisateurService.getUtilisateurIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();
  }

  delete(utilisateur: IUtilisateur): void {
    const modalRef = this.modalService.open(UtilisateurDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.utilisateur = utilisateur;
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  deleteSelected(): void {
    if (confirm(`Voulez-vous vraiment supprimer les ${this.selectedUtilisateurs.size} utilisateurs sélectionnés ?`)) {
      this.selectedUtilisateurs.forEach(id => {
        this.utilisateurService.delete(id).subscribe({
          next: () => {
            alert(`Utilisateur #${id} supprimé avec succès.`);
          },
          error: err => {
            console.error('Erreur lors de la suppression', err);
            alert('Erreur lors de la suppression.');
          },
        });
      });
      this.selectedUtilisateurs.clear();
      this.load();
    }
  }

  load(): void {
    this.queryBackend().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(this.page, event);
  }

  navigateToPage(page: number): void {
    this.handleNavigation(page, this.sortState());
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.utilisateurs.set(dataFromBody);
  }

  protected fillComponentAttributesFromResponseBody(data: IUtilisateur[] | null): IUtilisateur[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    const { page } = this;

    this.isLoading = true;
    const pageToLoad: number = page;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      eagerload: true,
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    return this.utilisateurService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page: number, sortState: SortState): void {
    const queryParamsObj = {
      page,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(sortState),
    };

    this.ngZone.run(() => {
      this.router.navigate(['./'], {
        relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    });
  }

  onSearch(query: string): void {
    this.searchQuery = query;
    this.load();
  }

  toggleSelection(utilisateurId: number): void {
    if (this.selectedUtilisateurs.has(utilisateurId)) {
      this.selectedUtilisateurs.delete(utilisateurId);
    } else {
      this.selectedUtilisateurs.add(utilisateurId);
    }
  }
}
