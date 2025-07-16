import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDelaiIntervention, NewDelaiIntervention, StatutDelaiIntervention } from '../delai-intervention.model';

export type PartialUpdateDelaiIntervention = Partial<IDelaiIntervention> & Pick<IDelaiIntervention, 'id'>;

type RestOf<T extends IDelaiIntervention | NewDelaiIntervention> = Omit<T, 'dateDebut' | 'dateLimite'> & {
  dateDebut?: string | null;
  dateLimite?: string | null;
};

export type RestDelaiIntervention = RestOf<IDelaiIntervention>;
export type NewRestDelaiIntervention = RestOf<NewDelaiIntervention>;
export type PartialUpdateRestDelaiIntervention = RestOf<PartialUpdateDelaiIntervention>;

export type EntityResponseType = HttpResponse<IDelaiIntervention>;
export type EntityArrayResponseType = HttpResponse<IDelaiIntervention[]>;

@Injectable({ providedIn: 'root' })
export class DelaiInterventionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/delai-interventions');

  create(delaiIntervention: NewDelaiIntervention): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(delaiIntervention);
    return this.http
      .post<RestDelaiIntervention>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(delaiIntervention: IDelaiIntervention): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(delaiIntervention);
    return this.http
      .put<RestDelaiIntervention>(`${this.resourceUrl}/${this.getDelaiInterventionIdentifier(delaiIntervention)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(delaiIntervention: PartialUpdateDelaiIntervention): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(delaiIntervention);
    return this.http
      .patch<RestDelaiIntervention>(`${this.resourceUrl}/${this.getDelaiInterventionIdentifier(delaiIntervention)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDelaiIntervention>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDelaiIntervention[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDelaiInterventionIdentifier(delai: Pick<IDelaiIntervention, 'id'>): number {
    if (delai.id === undefined) {
      throw new Error('ID DelaiIntervention is undefined');
    }
    return delai.id;
  }

  compareDelaiIntervention(o1: Pick<IDelaiIntervention, 'id'> | null, o2: Pick<IDelaiIntervention, 'id'> | null): boolean {
    return o1 && o2 ? this.getDelaiInterventionIdentifier(o1) === this.getDelaiInterventionIdentifier(o2) : o1 === o2;
  }

  addDelaiInterventionToCollectionIfMissing<Type extends Pick<IDelaiIntervention, 'id'>>(
    collection: Type[],
    ...toCheck: (Type | null | undefined)[]
  ): Type[] {
    const items: Type[] = toCheck.filter(isPresent);
    if (items.length > 0) {
      const collectionIdentifiers = collection.map(item => this.getDelaiInterventionIdentifier(item));
      const itemsToAdd = items.filter(item => {
        const id = this.getDelaiInterventionIdentifier(item);
        if (collectionIdentifiers.includes(id)) {
          return false;
        }
        collectionIdentifiers.push(id);
        return true;
      });
      return [...itemsToAdd, ...collection];
    }
    return collection;
  }

  protected convertDateFromClient<T extends IDelaiIntervention | NewDelaiIntervention | PartialUpdateDelaiIntervention>(
    entity: T,
  ): RestOf<T> {
    return {
      ...entity,
      dateDebut: entity.dateDebut?.toJSON() ?? null,
      dateLimite: entity.dateLimite?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(rest: RestDelaiIntervention): IDelaiIntervention {
    return {
      ...rest,
      dateDebut: rest.dateDebut ? dayjs(rest.dateDebut) : undefined,
      dateLimite: rest.dateLimite ? dayjs(rest.dateLimite) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDelaiIntervention>): HttpResponse<IDelaiIntervention> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDelaiIntervention[]>): HttpResponse<IDelaiIntervention[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
  getDelaisByDegradationId(degradationId: number): Observable<IDelaiIntervention[]> {
    return this.http
      .get<RestDelaiIntervention[]>(`${this.resourceUrl}/by-degradation/${degradationId}`)
      .pipe(map((res: RestDelaiIntervention[]) => res.map(r => this.convertDateFromServer(r))));
  }
  queryActeurs(req?: any): Observable<EntityArrayResponseType> {
    return this.http.get<IUtilisateur[]>(this.resourceUrl + '/acteurs', { params: req, observe: 'response' });
  }
}
