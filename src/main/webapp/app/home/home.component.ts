import { AfterViewInit, Component, ElementRef, OnDestroy, ViewChild, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import * as L from 'leaflet';
import { Subscription } from 'rxjs';

import { DegradationService } from 'app/entities/degradation/service/degradation.service';
import { IDegradation } from 'app/entities/degradation/degradation.model';
import { MapService, MapLocation } from 'app/services/map.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  standalone: true,
  imports: [FormsModule, CommonModule],
})
export default class HomeComponent implements AfterViewInit, OnDestroy {
  @ViewChild('mapContainer', { static: false }) mapContainer!: ElementRef<HTMLDivElement>;

  private map!: L.Map;
  private degradationService = inject(DegradationService);
  private mapService = inject(MapService);
  private cdRef = inject(ChangeDetectorRef);

  priorities: string[] = ['Critique', 'Élevée', 'Moyenne', 'Faible'];
  selectedPriority: string = '';
  allDegradations: IDegradation[] = [];
  markerGroup: L.LayerGroup = L.layerGroup();

  private degradationSubscription?: Subscription;
  private locationSubscription?: Subscription;

  ngAfterViewInit(): void {
    console.log('[HomeComponent] ngAfterViewInit called');

    if (!this.mapContainer) {
      console.error('❌ Élément mapContainer non trouvé');
      return;
    }

    this.fixLeafletIcons();
    this.initMap();
    this.loadDegradations();

    this.locationSubscription = this.mapService.location$.subscribe(loc => {
      if (loc) {
        this.addDynamicMarker(loc);
        this.mapService.clearLocation();
      }
    });

    // Empêche l'erreur NG0100
    setTimeout(() => this.cdRef.detectChanges(), 0);
  }

  private initMap(): void {
    console.log('[HomeComponent] Initialisation de la carte...');
    this.map = L.map(this.mapContainer.nativeElement).setView([5.35, -4.02], 7);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors',
      maxZoom: 18,
    }).addTo(this.map);

    this.markerGroup.addTo(this.map);

    setTimeout(() => {
      this.map.invalidateSize();
    }, 100);
  }

  fixLeafletIcons(): void {
    delete (L.Icon.Default.prototype as any)._getIconUrl;
    L.Icon.Default.mergeOptions({
      iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png',
      iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
      shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png',
    });
  }

  loadDegradations(): void {
    this.degradationSubscription = this.degradationService.query().subscribe({
      next: response => {
        this.allDegradations = response.body ?? [];
        this.applyFilter();
      },
      error: error => {
        console.error('[HomeComponent] Erreur chargement dégradations:', error);
      },
    });
  }

  applyFilter(): void {
    this.markerGroup.clearLayers();

    const filtered = this.selectedPriority
      ? this.allDegradations.filter(d => d.priorite?.toLowerCase() === this.selectedPriority.toLowerCase())
      : this.allDegradations;

    filtered.forEach(deg => {
      const lat = this.parseCoord(deg.site?.latitude);
      const lng = this.parseCoord(deg.site?.longitude);

      if (!isNaN(lat) && !isNaN(lng)) {
        const color = this.getColorByPriority(deg.priorite?.toLowerCase());
        const marker = L.circleMarker([lat, lng], {
          radius: 8,
          color,
          fillColor: color,
          fillOpacity: 0.8,
        });

        const popup = `
          <strong>ID #${deg.id ?? '?'}</strong><br/>
          ${deg.localite ?? '-'}<br/>
          ${deg.typeAnomalie ?? '-'}<br/>
          Priorité : <b>${deg.priorite ?? '-'}</b><br/>
          <small>Lat: ${lat}, Lng: ${lng}</small>
        `;

        marker.bindPopup(popup);
        this.markerGroup.addLayer(marker);
      } else {
        console.warn(`[HomeComponent] Coordonnées invalides pour la dégradation ${deg.id}`);
      }
    });

    const layers = this.markerGroup.getLayers();
    if (layers.length > 0) {
      const group = L.featureGroup(layers as L.Layer[]);
      this.map.fitBounds(group.getBounds().pad(0.2));
    }
  }

  parseCoord(coord: string | number | undefined | null): number {
    if (typeof coord === 'string') return parseFloat(coord);
    if (typeof coord === 'number') return coord;
    return NaN;
  }

  addDynamicMarker(loc: MapLocation): void {
    const { latitude, longitude, priorite } = loc;
    const color = this.getColorByPriority(priorite?.toLowerCase());

    const marker = L.circleMarker([latitude, longitude], {
      radius: 10,
      color,
      fillColor: color,
      fillOpacity: 0.9,
    });

    const popup = `
      <strong>Nouvelle dégradation</strong><br/>
      Priorité : <b>${priorite ?? '-'}</b><br/>
      <small>Lat: ${latitude}, Lng: ${longitude}</small>
    `;

    marker.bindPopup(popup);
    this.markerGroup.addLayer(marker);
    this.map.flyTo([latitude, longitude], 15);
  }

  getColorByPriority(priority: string | null | undefined): string {
    const colors = {
      critique: '#dc2626',
      élevée: '#ea580c',
      moyenne: '#ca8a04',
      faible: '#16a34a',
    };
    return colors[priority as keyof typeof colors] || '#3b82f6';
  }

  clearFilter(): void {
    this.selectedPriority = '';
    this.applyFilter();
  }

  ngOnDestroy(): void {
    console.log('[HomeComponent] Nettoyage...');
    this.map.remove();
    this.degradationSubscription?.unsubscribe();
    this.locationSubscription?.unsubscribe();
  }
}
