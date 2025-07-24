import { Component, OnInit, inject, signal } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { Observable, Subscription, combineLatest, filter, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { SearchBarComponent } from 'app/shared/search-bar/search-bar.component';

import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { FormatMediumDatePipe } from 'app/shared/date';
import { ItemCountComponent } from 'app/shared/pagination';
import { FormsModule } from '@angular/forms';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { ISite } from '../site.model';
import { EntityArrayResponseType, SiteService } from '../service/site.service';
import { SiteDeleteDialogComponent } from '../delete/site-delete-dialog.component';

@Component({
  selector: 'jhi-site',
  standalone: true,
  templateUrl: './site.component.html',
  imports: [
    FormsModule,
    SharedModule,
    SortDirective,
    SortByDirective,
    FormatMediumDatePipe,
    ItemCountComponent,
    SearchBarComponent,
    RouterModule,
  ],
})
export class SiteComponent implements OnInit {
  subscription: Subscription | null = null;
  sites = signal<ISite[]>([]);
  selectedSites: Set<number> = new Set();
  isLoading = false;
  searchQuery: string = '';

  sortState = sortStateSignal({});

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  public readonly router = inject(Router);
  protected readonly siteService = inject(SiteService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);

  trackId = (item: ISite): number => this.siteService.getSiteIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();
  }

  delete(site: ISite): void {
    const modalRef = this.modalService.open(SiteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.site = site;
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  deleteSelected(): void {
    if (confirm(`Voulez-vous vraiment supprimer les ${this.selectedSites.size} sites sélectionnés ?`)) {
      this.selectedSites.forEach(id => {
        this.siteService.delete(id).subscribe({
          next: () => {
            alert(`Site #${id} supprimé avec succès.`);
          },
          error: err => {
            console.error('Erreur lors de la suppression', err);
            alert('Erreur lors de la suppression.');
          },
        });
      });
      this.selectedSites.clear();
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
    this.sites.set(dataFromBody);
  }

  protected fillComponentAttributesFromResponseBody(data: ISite[] | null): ISite[] {
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
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    if (this.searchQuery) {
      queryObject['search'] = this.searchQuery;
    }
    return this.siteService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page: number, sortState: SortState): void {
    const queryParamsObj = {
      page,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(sortState),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }

  onSearch(query: string): void {
    this.searchQuery = query;
    this.page = 1;
    this.load();
  }

  toggleSelection(siteId: number): void {
    if (this.selectedSites.has(siteId)) {
      this.selectedSites.delete(siteId);
    } else {
      this.selectedSites.add(siteId);
    }
  }
}
