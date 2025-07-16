import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IDelaiIntervention } from '../delai-intervention.model';

@Injectable({ providedIn: 'root' })
export class DelaiInterventionService {
  public resourceUrl = 'api/delai-interventions';

  constructor(protected http: HttpClient) {}

  find(id: number): Observable<HttpResponse<IDelaiIntervention>> {
    return this.http.get<IDelaiIntervention>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
  getDelaisByDegradationId(degradationId: number): Observable<DelaiInterventionDTO[]> {
    return this.http.get<DelaiInterventionDTO[]>(`api/delais/by-degradation/${degradationId}`);
  }

  // autres méthodes CRUD si besoin
}
