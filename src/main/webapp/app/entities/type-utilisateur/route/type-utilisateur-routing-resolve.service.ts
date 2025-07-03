import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypeUtilisateur } from '../type-utilisateur.model';
import { TypeUtilisateurService } from '../service/type-utilisateur.service';

const typeUtilisateurResolve = (route: ActivatedRouteSnapshot): Observable<null | ITypeUtilisateur> => {
  const id = route.params.id;
  if (id) {
    return inject(TypeUtilisateurService)
      .find(id)
      .pipe(
        mergeMap((typeUtilisateur: HttpResponse<ITypeUtilisateur>) => {
          if (typeUtilisateur.body) {
            return of(typeUtilisateur.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default typeUtilisateurResolve;
