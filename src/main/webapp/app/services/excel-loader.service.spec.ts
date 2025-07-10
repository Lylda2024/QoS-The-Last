import { TestBed } from '@angular/core/testing';

import { ExcelLoaderService } from './excel-loader.service';

describe('ExcelLoaderService', () => {
  let service: ExcelLoaderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExcelLoaderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
