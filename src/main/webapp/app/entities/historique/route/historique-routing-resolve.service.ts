import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHistorique } from '../historique.model';
import { HistoriqueService } from '../service/historique.service';

const historiqueResolve = (route: ActivatedRouteSnapshot): Observable<null | IHistorique> => {
  const id = route.params.id;
  if (id) {
    return inject(HistoriqueService)
      .find(id)
      .pipe(
        mergeMap((historique: HttpResponse<IHistorique>) => {
          if (historique.body) {
            return of(historique.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default historiqueResolve;
