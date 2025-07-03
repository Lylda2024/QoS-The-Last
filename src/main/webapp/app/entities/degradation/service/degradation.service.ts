import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IDegradation, NewDegradation } from '../degradation.model';

export type PartialUpdateDegradation = Partial<IDegradation> & { id: number };

export type EntityResponseType = HttpResponse<IDegradation>;
export type EntityArrayResponseType = HttpResponse<IDegradation[]>;

@Injectable({ providedIn: 'root' })
export class DegradationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/degradations');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(degradation: NewDegradation): Observable<EntityResponseType> {
    return this.http.post<IDegradation>(this.resourceUrl, degradation, { observe: 'response' });
  }

  update(degradation: IDegradation): Observable<EntityResponseType> {
    return this.http.put<IDegradation>(`${this.resourceUrl}/${degradation.id}`, degradation, { observe: 'response' });
  }

  partialUpdate(degradation: PartialUpdateDegradation): Observable<EntityResponseType> {
    return this.http.patch<IDegradation>(`${this.resourceUrl}/${degradation.id}`, degradation, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDegradation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    return this.http.get<IDegradation[]>(this.resourceUrl, { params: req, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDegradationIdentifier(degradation: Pick<IDegradation, 'id'>): number {
    return degradation.id;
  }

  compareDegradation(o1: Pick<IDegradation, 'id'> | null, o2: Pick<IDegradation, 'id'> | null): boolean {
    return o1 && o2 ? o1.id === o2.id : o1 === o2;
  }

  addDegradationToCollectionIfMissing<Type extends Pick<IDegradation, 'id'>>(
    degradationCollection: Type[],
    ...degradationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const degradations: Type[] = degradationsToCheck.filter((item): item is Type => item != null);
    const degradationCollectionIdentifiers = degradationCollection.map(item => item.id);
    const additions = degradations.filter(item => {
      const id = item.id;
      if (degradationCollectionIdentifiers.includes(id)) {
        return false;
      }
      degradationCollectionIdentifiers.push(id);
      return true;
    });
    return [...additions, ...degradationCollection];
  }
}
