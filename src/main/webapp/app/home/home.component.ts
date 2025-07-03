import { AfterViewInit, Component, ElementRef, OnDestroy, ViewChild, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import * as L from 'leaflet';
import { Subscription } from 'rxjs';

import { DegradationService } from 'app/entities/degradation/service/degradation.service';
import { IDegradation } from 'app/entities/degradation/degradation.model';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  standalone: true,
  imports: [FormsModule],
})
export default class HomeComponent implements AfterViewInit, OnDestroy {
  @ViewChild('mapContainer', { static: false }) mapContainer!: ElementRef<HTMLDivElement>;

  private map!: L.Map;
  private degradationService = inject(DegradationService);

  selectedPriority: string = '';
  allDegradations: IDegradation[] = [];
  markerGroup: L.LayerGroup = L.layerGroup();

  private subscription?: Subscription;

  ngAfterViewInit(): void {
    console.log('[HomeComponent] ngAfterViewInit called');
    console.log('[HomeComponent] mapContainer:', this.mapContainer);

    this.fixLeafletIcons();

    if (!this.mapContainer) {
      console.error('❌ Élément mapContainer non trouvé');
      return;
    }

    this.initMap();
    this.loadDegradations();
  }

  private initMap(): void {
    console.log('[HomeComponent] Initialisation de la carte Leaflet...');
    this.map = L.map(this.mapContainer.nativeElement).setView([5.35, -4.02], 7);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors',
      maxZoom: 18,
    }).addTo(this.map);

    this.markerGroup.addTo(this.map);

    setTimeout(() => {
      console.log('[HomeComponent] invalidateSize appelée');
      this.map.invalidateSize();
    }, 100);
  }

  fixLeafletIcons(): void {
    console.log('[HomeComponent] Correction des icônes Leaflet par défaut');
    delete (L.Icon.Default.prototype as any)._getIconUrl;
    L.Icon.Default.mergeOptions({
      iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png',
      iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
      shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png',
    });
  }

  loadDegradations(): void {
    console.log('[HomeComponent] Chargement des dégradations depuis service');
    this.subscription = this.degradationService.query().subscribe({
      next: response => {
        this.allDegradations = response.body ?? [];
        console.log('[HomeComponent] Dégradations reçues:', this.allDegradations.length, 'éléments');
        this.applyFilter();
      },
      error: error => {
        console.error('[HomeComponent] Erreur lors du chargement des dégradations:', error);
      },
    });
  }

  applyFilter(): void {
    console.log(`[HomeComponent] Application du filtre: priorité = "${this.selectedPriority}"`);
    this.markerGroup.clearLayers();

    const filtered = this.selectedPriority
      ? this.allDegradations.filter(d => d.priorite?.toLowerCase() === this.selectedPriority.toLowerCase())
      : this.allDegradations;

    console.log('[HomeComponent] Nombre de dégradations après filtrage:', filtered.length);

    filtered.forEach(deg => {
      const latRaw = deg.site?.latitude;
      const lngRaw = deg.site?.longitude;

      let lat: number = NaN;
      let lng: number = NaN;

      if (typeof latRaw === 'string') lat = parseFloat(latRaw);
      else if (typeof latRaw === 'number') lat = latRaw;

      if (typeof lngRaw === 'string') lng = parseFloat(lngRaw);
      else if (typeof lngRaw === 'number') lng = lngRaw;

      const priority = deg.priorite?.toLowerCase();

      if (!isNaN(lat) && !isNaN(lng)) {
        const color = this.getColorByPriority(priority);
        console.log(`[HomeComponent] Ajout du marqueur: ${deg.numero} à (${lat}, ${lng}) avec couleur ${color}`);

        const marker = L.circleMarker([lat, lng], {
          radius: 8,
          color,
          fillColor: color,
          fillOpacity: 0.8,
        });

        const popup = `
          <strong>${deg.numero ?? 'N° ?'}</strong><br/>
          ${deg.localite ?? '-'}<br/>
          ${deg.typeAnomalie ?? '-'}<br/>
          Priorité : <b>${deg.priorite ?? '-'}</b><br/>
          <small>Lat: ${lat}, Lng: ${lng}</small>
        `;

        marker.bindPopup(popup);
        this.markerGroup.addLayer(marker);
      } else {
        console.warn(`[HomeComponent] Coordonnées invalides pour dégradation ${deg.numero}`);
      }
    });

    const layers = this.markerGroup.getLayers();
    if (layers.length > 0) {
      const group = L.featureGroup(layers as L.Layer[]);
      this.map.fitBounds(group.getBounds().pad(0.2));
      console.log('[HomeComponent] Zoom ajusté sur les marqueurs');
    } else {
      console.log('[HomeComponent] Aucun marqueur à afficher');
    }
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

  ngOnDestroy(): void {
    console.log('[HomeComponent] Nettoyage avant destruction');
    if (this.map) {
      this.map.remove();
    }
    this.subscription?.unsubscribe();
  }
}
