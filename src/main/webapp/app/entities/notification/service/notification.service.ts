import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { INotification } from '../notification.model';

export type EntityResponseType = HttpResponse<INotification>;
export type EntityArrayResponseType = HttpResponse<INotification[]>;

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  protected resourceUrl = 'api/notifications';

  constructor(protected http: HttpClient) {}

  create(notification: INotification): Observable<EntityResponseType> {
    return this.http.post<INotification>(this.resourceUrl, notification, { observe: 'response' });
  }

  update(notification: INotification): Observable<EntityResponseType> {
    return this.http.put<INotification>(`${this.resourceUrl}/${notification.id}`, notification, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INotification>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    return this.http.get<INotification[]>(this.resourceUrl, { params: req, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getNotificationIdentifier(notification: INotification): number {
    return notification.id!;
  }
}
