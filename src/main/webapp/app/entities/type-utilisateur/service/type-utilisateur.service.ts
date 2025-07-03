import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypeUtilisateur, NewTypeUtilisateur } from '../type-utilisateur.model';

export type PartialUpdateTypeUtilisateur = Partial<ITypeUtilisateur> & Pick<ITypeUtilisateur, 'id'>;

export type EntityResponseType = HttpResponse<ITypeUtilisateur>;
export type EntityArrayResponseType = HttpResponse<ITypeUtilisateur[]>;

@Injectable({ providedIn: 'root' })
export class TypeUtilisateurService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-utilisateurs');

  create(typeUtilisateur: NewTypeUtilisateur): Observable<EntityResponseType> {
    return this.http.post<ITypeUtilisateur>(this.resourceUrl, typeUtilisateur, { observe: 'response' });
  }

  update(typeUtilisateur: ITypeUtilisateur): Observable<EntityResponseType> {
    return this.http.put<ITypeUtilisateur>(`${this.resourceUrl}/${this.getTypeUtilisateurIdentifier(typeUtilisateur)}`, typeUtilisateur, {
      observe: 'response',
    });
  }

  partialUpdate(typeUtilisateur: PartialUpdateTypeUtilisateur): Observable<EntityResponseType> {
    return this.http.patch<ITypeUtilisateur>(`${this.resourceUrl}/${this.getTypeUtilisateurIdentifier(typeUtilisateur)}`, typeUtilisateur, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeUtilisateur>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeUtilisateur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTypeUtilisateurIdentifier(typeUtilisateur: Pick<ITypeUtilisateur, 'id'>): number {
    return typeUtilisateur.id;
  }

  compareTypeUtilisateur(o1: Pick<ITypeUtilisateur, 'id'> | null, o2: Pick<ITypeUtilisateur, 'id'> | null): boolean {
    return o1 && o2 ? this.getTypeUtilisateurIdentifier(o1) === this.getTypeUtilisateurIdentifier(o2) : o1 === o2;
  }

  addTypeUtilisateurToCollectionIfMissing<Type extends Pick<ITypeUtilisateur, 'id'>>(
    typeUtilisateurCollection: Type[],
    ...typeUtilisateursToCheck: (Type | null | undefined)[]
  ): Type[] {
    const typeUtilisateurs: Type[] = typeUtilisateursToCheck.filter(isPresent);
    if (typeUtilisateurs.length > 0) {
      const typeUtilisateurCollectionIdentifiers = typeUtilisateurCollection.map(typeUtilisateurItem =>
        this.getTypeUtilisateurIdentifier(typeUtilisateurItem),
      );
      const typeUtilisateursToAdd = typeUtilisateurs.filter(typeUtilisateurItem => {
        const typeUtilisateurIdentifier = this.getTypeUtilisateurIdentifier(typeUtilisateurItem);
        if (typeUtilisateurCollectionIdentifiers.includes(typeUtilisateurIdentifier)) {
          return false;
        }
        typeUtilisateurCollectionIdentifiers.push(typeUtilisateurIdentifier);
        return true;
      });
      return [...typeUtilisateursToAdd, ...typeUtilisateurCollection];
    }
    return typeUtilisateurCollection;
  }
}
