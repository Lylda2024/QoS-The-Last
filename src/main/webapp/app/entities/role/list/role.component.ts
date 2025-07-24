import { Component, OnInit, inject, signal } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { Observable, Subscription, combineLatest, filter, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { SearchBarComponent } from 'app/shared/search-bar/search-bar.component';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { ItemCountComponent } from 'app/shared/pagination';
import { FormsModule } from '@angular/forms';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { IRole } from '../role.model';
import { EntityArrayResponseType, RoleService } from '../service/role.service';
import { RoleDeleteDialogComponent } from '../delete/role-delete-dialog.component';

@Component({
  selector: 'jhi-role',
  standalone: true,
  templateUrl: './role.component.html',
  imports: [FormsModule, RouterModule, SharedModule, SortDirective, SortByDirective, ItemCountComponent, SearchBarComponent],
})
export class RoleComponent implements OnInit {
  subscription: Subscription | null = null;
  roles = signal<IRole[]>([]);
  selectedRoles: Set<number> = new Set();
  isLoading = false;
  searchQuery: string = '';

  sortState = sortStateSignal({});

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  public readonly router = inject(Router);
  protected readonly roleService = inject(RoleService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);

  trackId = (item: IRole): number => this.roleService.getRoleIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();
  }

  delete(role: IRole): void {
    const modalRef = this.modalService.open(RoleDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.role = role;
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  deleteSelected(): void {
    if (confirm(`Voulez-vous vraiment supprimer les ${this.selectedRoles.size} rôles sélectionnés ?`)) {
      this.selectedRoles.forEach(id => {
        this.roleService.delete(id).subscribe({
          next: () => {
            alert(`Rôle #${id} supprimé avec succès.`);
          },
          error: err => {
            console.error('Erreur lors de la suppression', err);
            alert('Erreur lors de la suppression.');
          },
        });
      });
      this.selectedRoles.clear();
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
    this.roles.set(dataFromBody);
  }

  protected fillComponentAttributesFromResponseBody(data: IRole[] | null): IRole[] {
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
    return this.roleService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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

  toggleSelection(roleId: number): void {
    if (this.selectedRoles.has(roleId)) {
      this.selectedRoles.delete(roleId);
    } else {
      this.selectedRoles.add(roleId);
    }
  }
}
