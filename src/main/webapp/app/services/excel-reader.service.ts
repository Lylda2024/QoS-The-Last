import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable, switchMap } from 'rxjs';
import * as XLSX from 'xlsx';

@Injectable({ providedIn: 'root' })
export class ExcelReaderService {
  constructor(private http: HttpClient) {}

  /**
   * Charge un fichier Excel depuis /assets et extrait une liste de communes.
   * Le fichier doit contenir une colonne "Commune" ou être une liste simple.
   */
  loadCommunes(): Observable<string[]> {
    return this.http.get('/assets/communes.xlsx', { responseType: 'blob' }).pipe(
      switchMap(blob => this.parseBlob(blob)),
      map(rows => {
        const communes = new Set<string>();
        for (const row of rows) {
          const value =
            row['Localité'] || // ou autre nom possible
            Object.values(row)[0]; // ou première colonne

          if (typeof value === 'string' && value.trim().length > 0) {
            communes.add(value.trim());
          }
        }
        return Array.from(communes).sort((a, b) => a.localeCompare(b));
      }),
    );
  }

  /**
   * Analyse un fichier Excel (Blob) et le convertit en tableau JSON.
   */
  private parseBlob(blob: Blob): Promise<Record<string, any>[]> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();

      reader.onload = (e: ProgressEvent<FileReader>) => {
        try {
          const data = new Uint8Array(e.target!.result as ArrayBuffer);
          const workbook = XLSX.read(data, { type: 'array' });

          const firstSheet = workbook.Sheets[workbook.SheetNames[0]];
          const jsonData = XLSX.utils.sheet_to_json(firstSheet, { defval: '' });

          resolve(jsonData as Record<string, any>[]);
        } catch (error) {
          reject(`Erreur lors de la lecture du fichier Excel : ${error}`);
        }
      };

      reader.onerror = err => {
        reject(`Erreur de lecture du fichier : ${err}`);
      };

      reader.readAsArrayBuffer(blob);
    });
  }
}
