import * as XLSX from 'xlsx';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ExcelReaderService {
  parseFile(file: File): Promise<any[]> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        const data = new Uint8Array(e.target.result);
        const workbook = XLSX.read(data, { type: 'array' });
        const firstSheet = workbook.Sheets[workbook.SheetNames[0]];
        const jsonData = XLSX.utils.sheet_to_json(firstSheet, { defval: '' });
        resolve(jsonData);
      };
      reader.onerror = err => reject(err);
      reader.readAsArrayBuffer(file);
    });
  }
}
