import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { ISite, NewSite } from 'app/entities/site/site.model';

import * as XLSX from 'xlsx';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { SiteService } from '../service/site.service';
import { SiteFormGroup, SiteFormService } from './site-form.service';

@Component({
  selector: 'jhi-site-update',
  templateUrl: './site-update.component.html',
  standalone: true,
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SiteUpdateComponent implements OnInit {
  isSaving = false;
  isLoading = false;
  site: ISite | null = null;

  fileName: string | null = null;
  importedSites: Partial<ISite>[] = [];

  protected siteService = inject(SiteService);
  protected siteFormService = inject(SiteFormService);
  protected activatedRoute = inject(ActivatedRoute);

  editForm: SiteFormGroup = this.siteFormService.createSiteFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ site }) => {
      this.site = site;
      if (site) {
        this.updateForm(site);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const site = this.siteFormService.getSite(this.editForm);
    if (site.id !== null) {
      this.subscribeToSaveResponse(this.siteService.update(site));
    } else {
      const { id, ...newSite } = site;
      this.subscribeToSaveResponse(this.siteService.create({ ...newSite, id: null } as NewSite));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISite>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    console.error('❌ Erreur lors de la sauvegarde');
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(site: ISite): void {
    this.site = site;
    this.siteFormService.resetForm(this.editForm, site);
  }

  onFileChange(event: any): void {
    const target: DataTransfer = event.target as DataTransfer;
    if (target.files?.length !== 1) {
      console.error('⚠️ Un seul fichier à la fois.');
      return;
    }
    this.fileName = target.files[0].name;
    const reader: FileReader = new FileReader();
    reader.onload = (e: ProgressEvent<FileReader>) => {
      const bstr: string = e.target?.result as string;
      const wb: XLSX.WorkBook = XLSX.read(bstr, { type: 'binary' });
      const sheetName = wb.SheetNames[0];
      const ws: XLSX.WorkSheet = wb.Sheets[sheetName];
      const data = XLSX.utils.sheet_to_json<any>(ws);
      this.importedSites = data
        .map(row => ({
          nomSite: row['Nom du site']?.toString().trim() ?? '',
          codeOCI: row['Code']?.toString().trim() ?? '',
          latitude: parseFloat(row['Latitude']) || null,
          longitude: parseFloat(row['Longitude']) || null,
          ville: row['Ville']?.toString().trim() ?? '',
          typeSite: row['Type']?.toString().trim() ?? '',
        }))
        .filter(site => site.nomSite && site.codeOCI);
    };
    reader.readAsBinaryString(target.files[0]);
  }

  saveImportedSites(): void {
    this.isLoading = true;
    const sitesToCreate = this.importedSites.map(site => ({ ...site, id: null }));
    this.siteService.createMany?.(sitesToCreate)?.subscribe({
      next: () => {
        this.isLoading = false;
        this.importedSites = [];
        this.fileName = null;
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  cancelImport(): void {
    this.fileName = null;
    this.importedSites = [];
  }
}
