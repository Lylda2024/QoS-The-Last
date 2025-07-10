import { Injectable } from '@angular/core';
import * as XLSX from 'xlsx';

@Injectable({ providedIn: 'root' })
export class ExcelLoaderService {
  constructor() {}

  async loadCommunesFromExcel(): Promise<string[]> {
    try {
      const response = await fetch('assets/communes.xlsx');
      if (!response.ok) throw new Error('Fichier non trouvé');

      const arrayBuffer = await response.arrayBuffer();
      const workbook = XLSX.read(arrayBuffer, { type: 'array' });

      const sheetName = workbook.SheetNames[0];
      const worksheet = workbook.Sheets[sheetName];
      const json = XLSX.utils.sheet_to_json(worksheet);

      const communes = json
        .map((row: any) => row['commune']) // adapte ce nom à ta colonne Excel exacte
        .filter((commune: any) => !!commune);

      return Array.from(new Set(communes)).sort();
    } catch (error) {
      console.error('Erreur lors du chargement du fichier Excel', error);
      return [];
    }
  }
}
