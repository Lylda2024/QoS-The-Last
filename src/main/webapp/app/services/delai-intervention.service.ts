import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IDelaiIntervention, NewDelaiIntervention } from './delai-intervention.model';

@Injectable({ providedIn: 'root' })
export class DelaiInterventionService {
  protected resourceUrl = 'api/delai-interventions'; // URL REST côté Spring Boot

  constructor(protected http: HttpClient) {}

  /**
   * Obtenir tous les délais d'intervention
   */
  query(): Observable<IDelaiIntervention[]> {
    return this.http.get<IDelaiIntervention[]>(this.resourceUrl);
  }

  /**
   * Obtenir les délais d’une dégradation donnée (relation avec entité Degradation)
   */
  findByDegradationId(degradationId: number): Observable<IDelaiIntervention[]> {
    return this.http.get<IDelaiIntervention[]>(`${this.resourceUrl}/by-degradation/${degradationId}`);
  }

  /**
   * Obtenir un délai spécifique
   */
  find(id: number): Observable<IDelaiIntervention> {
    return this.http.get<IDelaiIntervention>(`${this.resourceUrl}/${id}`);
  }

  /**
   * Créer un nouveau délai
   */
  create(delai: NewDelaiIntervention): Observable<IDelaiIntervention> {
    return this.http.post<IDelaiIntervention>(this.resourceUrl, delai);
  }

  /**
   * Modifier un délai existant
   */
  update(delai: IDelaiIntervention): Observable<IDelaiIntervention> {
    return this.http.put<IDelaiIntervention>(`${this.resourceUrl}/${delai.id}`, delai);
  }

  /**
   * Supprimer un délai
   */
  delete(id: number): Observable<{}> {
    return this.http.delete(`${this.resourceUrl}/${id}`);
  }
}
