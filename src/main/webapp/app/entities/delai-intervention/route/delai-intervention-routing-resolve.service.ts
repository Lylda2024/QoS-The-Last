import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, ResolveFn, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDelaiIntervention } from '../delai-intervention.model';
import { DelaiInterventionService } from '../service/delai-intervention.service';

export const delaiInterventionResolve: ResolveFn<IDelaiIntervention | null> = (
  route: ActivatedRouteSnapshot,
): Observable<IDelaiIntervention | null> => {
  const id = route.params['id'];
  if (id) {
    return inject(DelaiInterventionService)
      .find(id)
      .pipe(
        mergeMap((res: HttpResponse<IDelaiIntervention>) => {
          if (res.body) {
            return of(res.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};
