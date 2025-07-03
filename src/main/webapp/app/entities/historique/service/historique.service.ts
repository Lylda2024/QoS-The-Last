import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHistorique, NewHistorique } from '../historique.model';

export type PartialUpdateHistorique = Partial<IHistorique> & Pick<IHistorique, 'id'>;

type RestOf<T extends IHistorique | NewHistorique> = Omit<T, 'horodatage'> & {
  horodatage?: string | null;
};

export type RestHistorique = RestOf<IHistorique>;

export type NewRestHistorique = RestOf<NewHistorique>;

export type PartialUpdateRestHistorique = RestOf<PartialUpdateHistorique>;

export type EntityResponseType = HttpResponse<IHistorique>;
export type EntityArrayResponseType = HttpResponse<IHistorique[]>;

@Injectable({ providedIn: 'root' })
export class HistoriqueService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/historiques');

  create(historique: NewHistorique): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(historique);
    return this.http
      .post<RestHistorique>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(historique: IHistorique): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(historique);
    return this.http
      .put<RestHistorique>(`${this.resourceUrl}/${this.getHistoriqueIdentifier(historique)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(historique: PartialUpdateHistorique): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(historique);
    return this.http
      .patch<RestHistorique>(`${this.resourceUrl}/${this.getHistoriqueIdentifier(historique)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestHistorique>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestHistorique[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getHistoriqueIdentifier(historique: Pick<IHistorique, 'id'>): number {
    return historique.id;
  }

  compareHistorique(o1: Pick<IHistorique, 'id'> | null, o2: Pick<IHistorique, 'id'> | null): boolean {
    return o1 && o2 ? this.getHistoriqueIdentifier(o1) === this.getHistoriqueIdentifier(o2) : o1 === o2;
  }

  addHistoriqueToCollectionIfMissing<Type extends Pick<IHistorique, 'id'>>(
    historiqueCollection: Type[],
    ...historiquesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const historiques: Type[] = historiquesToCheck.filter(isPresent);
    if (historiques.length > 0) {
      const historiqueCollectionIdentifiers = historiqueCollection.map(historiqueItem => this.getHistoriqueIdentifier(historiqueItem));
      const historiquesToAdd = historiques.filter(historiqueItem => {
        const historiqueIdentifier = this.getHistoriqueIdentifier(historiqueItem);
        if (historiqueCollectionIdentifiers.includes(historiqueIdentifier)) {
          return false;
        }
        historiqueCollectionIdentifiers.push(historiqueIdentifier);
        return true;
      });
      return [...historiquesToAdd, ...historiqueCollection];
    }
    return historiqueCollection;
  }

  protected convertDateFromClient<T extends IHistorique | NewHistorique | PartialUpdateHistorique>(historique: T): RestOf<T> {
    return {
      ...historique,
      horodatage: historique.horodatage?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restHistorique: RestHistorique): IHistorique {
    return {
      ...restHistorique,
      horodatage: restHistorique.horodatage ? dayjs(restHistorique.horodatage) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestHistorique>): HttpResponse<IHistorique> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestHistorique[]>): HttpResponse<IHistorique[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
