import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import dayjs from 'dayjs';
import { IDelaiIntervention, NewDelaiIntervention } from './delai-intervention.model';

type RestOf<T> = Omit<T, 'dateDebut' | 'dateLimite'> & {
  dateDebut?: string | null;
  dateLimite?: string | null;
};

type RestDelaiIntervention = RestOf<IDelaiIntervention>;
type NewRestDelaiIntervention = RestOf<NewDelaiIntervention>;

@Injectable({ providedIn: 'root' })
export class DelaiInterventionService {
  protected resourceUrl = 'api/delai-interventions';

  constructor(protected http: HttpClient) {}

  /* -------------------------------------------------- */
  /*  Conversion sécurisée : accepte tout type de date  */
  /* -------------------------------------------------- */
  protected convertDateFromClient(delai: IDelaiIntervention | NewDelaiIntervention): RestDelaiIntervention | NewRestDelaiIntervention {
    return {
      ...delai,
      dateDebut: dayjs(delai.dateDebut ?? null).isValid() ? dayjs(delai.dateDebut).toISOString() : null,
      dateLimite: dayjs(delai.dateLimite ?? null).isValid() ? dayjs(delai.dateLimite).toISOString() : null,
    };
  }

  protected convertDateFromServer(restDelai: RestDelaiIntervention): IDelaiIntervention {
    return {
      ...restDelai,
      dateDebut: restDelai.dateDebut ? dayjs(restDelai.dateDebut) : undefined,
      dateLimite: restDelai.dateLimite ? dayjs(restDelai.dateLimite) : undefined,
    };
  }

  protected convertDateArrayFromServer(restDelais: RestDelaiIntervention[]): IDelaiIntervention[] {
    return restDelais.map(d => this.convertDateFromServer(d));
  }

  /* ----------  CRUD  ---------- */
  query(): Observable<IDelaiIntervention[]> {
    return this.http.get<RestDelaiIntervention[]>(this.resourceUrl).pipe(map(res => this.convertDateArrayFromServer(res)));
  }

  findByDegradationId(degradationId: number): Observable<IDelaiIntervention[]> {
    return this.http
      .get<RestDelaiIntervention[]>(`${this.resourceUrl}/by-degradation/${degradationId}`)
      .pipe(map(res => this.convertDateArrayFromServer(res)));
  }

  find(id: number): Observable<IDelaiIntervention> {
    return this.http.get<RestDelaiIntervention>(`${this.resourceUrl}/${id}`).pipe(map(res => this.convertDateFromServer(res)));
  }

  create(delai: NewDelaiIntervention): Observable<IDelaiIntervention> {
    const copy = this.convertDateFromClient(delai);
    return this.http.post<RestDelaiIntervention>(this.resourceUrl, copy).pipe(map(res => this.convertDateFromServer(res)));
  }

  update(delai: IDelaiIntervention): Observable<IDelaiIntervention> {
    const copy = this.convertDateFromClient(delai);
    return this.http.put<RestDelaiIntervention>(`${this.resourceUrl}/${delai.id}`, copy).pipe(map(res => this.convertDateFromServer(res)));
  }

  delete(id: number): Observable<{}> {
    return this.http.delete(`${this.resourceUrl}/${id}`);
  }
}
