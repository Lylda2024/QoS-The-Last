import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FontAwesomeModule, FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faEye, faPencilAlt, faTrash } from '@fortawesome/free-solid-svg-icons';

import { DelaiInterventionService } from '../service/delai-intervention.service';
import { IDelaiIntervention } from '../delai-intervention.model';
import dayjs from 'dayjs';

@Component({
  standalone: true,
  selector: 'jhi-delai-intervention-list',
  templateUrl: './delai-intervention-list.component.html',
  imports: [CommonModule, RouterModule, FontAwesomeModule],
})
export class DelaiInterventionListComponent implements OnInit {
  delais: IDelaiIntervention[] = [];
  isLoading = false;

  constructor(
    private delaiInterventionService: DelaiInterventionService,
    private router: Router,
    private iconLibrary: FaIconLibrary,
  ) {
    this.iconLibrary.addIcons(faEye, faPencilAlt, faTrash);
  }

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.isLoading = true;
    this.delaiInterventionService.query().subscribe({
      next: res => {
        this.delais = res.body ?? [];
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  trackId(index: number, item: IDelaiIntervention): number {
    if (item.id === undefined) {
      throw new Error('ID manquant pour DelaiIntervention');
    }
    return item.id;
  }

  getDateAsDate(dayjsDate: dayjs.Dayjs | null | undefined): Date | undefined {
    return dayjsDate?.toDate();
  }

  view(delai: IDelaiIntervention): void {
    this.router.navigate(['/delai-intervention', delai.id, 'view']);
  }

  edit(delai: IDelaiIntervention): void {
    this.router.navigate(['/delai-intervention', delai.id, 'edit']);
  }

  delete(delai: IDelaiIntervention): void {
    if (confirm('Voulez-vous vraiment supprimer cet élément ?')) {
      this.delaiInterventionService.delete(delai.id!).subscribe(() => {
        this.load(); // Recharge la liste
      });
    }
  }
}
