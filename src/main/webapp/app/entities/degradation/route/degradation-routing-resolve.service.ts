import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDegradation } from '../degradation.model';
import { DegradationService } from '../service/degradation.service';

const degradationResolve = (route: ActivatedRouteSnapshot): Observable<null | IDegradation> => {
  const id = route.params.id;
  if (id) {
    return inject(DegradationService)
      .find(id)
      .pipe(
        mergeMap((degradation: HttpResponse<IDegradation>) => {
          if (degradation.body) {
            return of(degradation.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default degradationResolve;
