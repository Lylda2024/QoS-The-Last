import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface MapLocation {
  latitude: number;
  longitude: number;
  priorite?: string;
}

@Injectable({ providedIn: 'root' })
export class MapService {
  private locationSubject = new BehaviorSubject<MapLocation | null>(null);
  location$ = this.locationSubject.asObservable();

  setLocation(loc: MapLocation): void {
    this.locationSubject.next(loc);
  }

  clearLocation(): void {
    this.locationSubject.next(null);
  }
}
