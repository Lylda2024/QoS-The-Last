import * as XLSX from 'xlsx';
import { Component } from '@angular/core';

import { SiteService } from '../service/site.service';
import { NewSite } from '../site.model';

@Component({
  selector: 'jhi-import',
  templateUrl: './import.component.html',
  styleUrls: ['./import.component.scss'],
})
export class ImportComponent {
  constructor(private siteService: SiteService) {}

  onFileChange(evt: any): void {
    const target: DataTransfer = <DataTransfer>evt.target;
    const reader: FileReader = new FileReader();

    reader.onload = (e: any) => {
      const data = new Uint8Array(e.target.result);
      const workbook = XLSX.read(data, { type: 'array' });
      const sheet = workbook.Sheets[workbook.SheetNames[0]];
      const rows = XLSX.utils.sheet_to_json(sheet);

      const sites: NewSite[] = (rows as any[]).map(row => ({
        id: null,
        nomSite: row['site bts'],
        codeOCI: row['code oci'],
        longitude: parseFloat(row['longitude']),
        latitude: parseFloat(row['latitude']),
        statut: 'Inconnu',
        technologie: row['equipementier telecom'] || '',
        enService: true,
      }));

      sites.forEach(site => {
        this.siteService.create(site).subscribe({
          next: () => console.log('Site inséré :', site.nomSite),
          error: err => console.error('Erreur :', err),
        });
      });
    };

    reader.readAsArrayBuffer(target.files[0]);
  }
}
