import { Component, AfterViewInit, OnDestroy, inject } from '@angular/core';
import * as L from 'leaflet';

import { DegradationService } from 'app/entities/degradation/service/degradation.service';
import { IDegradation } from 'app/entities/degradation/degradation.model';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AlertComponent } from 'app/shared/alert/alert.component';
import { AlertErrorComponent } from 'app/shared/alert/alert-error.component';

@Component({
  selector: 'jhi-degradation',
  templateUrl: './degradation.component.html',
  styleUrls: [],
  standalone: true,
  imports: [CommonModule, RouterModule, FontAwesomeModule, AlertComponent, AlertErrorComponent],
})
export class DegradationComponent implements AfterViewInit, OnDestroy {
  private map!: L.Map;
  private mapInitialized = false;
  private degradationService = inject(DegradationService);
  private router = inject(Router); // Injection du Router

  allDegradations: IDegradation[] = [];
  markerGroup: L.LayerGroup = L.layerGroup();

  loading = false;
  errorLoading = false;
  sortState: any = { predicate: 'id', ascending: true };

  ngAfterViewInit(): void {
    if (!this.mapInitialized) {
      this.initMap();
      this.loadDegradations();
      this.mapInitialized = true;
    }
  }

  initMap(): void {
    const mapElement = document.getElementById('map');
    if (!mapElement) {
      console.error('❌ Élément #map non trouvé dans le DOM');
      return;
    }

    this.map = L.map(mapElement).setView([5.35, -4.02], 7);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors',
      maxZoom: 18,
    }).addTo(this.map);

    this.markerGroup = L.layerGroup().addTo(this.map);
  }

  loadDegradations(): void {
    this.loading = true;
    this.errorLoading = false;

    this.degradationService.query().subscribe({
      next: response => {
        this.allDegradations = response.body ?? [];
        this.displayMarkers();
        this.loading = false;
      },
      error: err => {
        console.error('Erreur chargement dégradations', err);
        this.errorLoading = true;
        this.loading = false;
      },
    });
  }

  displayMarkers(): void {
    this.markerGroup.clearLayers();

    this.allDegradations.forEach(deg => {
      const lat = Number(deg.site?.latitude);
      const lng = Number(deg.site?.longitude);
      const priority = deg.priorite
        ?.toLowerCase()
        .normalize('NFD')
        .replace(/[\u0300-\u036f]/g, '');

      if (!isNaN(lat) && !isNaN(lng)) {
        const color = this.getColorByPriority(priority);

        const marker = L.circleMarker([lat, lng], {
          radius: 8,
          color,
          fillColor: color,
          fillOpacity: 0.8,
        });

        const popupContent = `
          <strong>${deg.numero ?? 'N° ?'}</strong><br/>
          ${deg.localite ?? '-'}<br/>
          ${deg.typeAnomalie ?? '-'}<br/>
          Priorité : <b>${deg.priorite ?? '-'}</b>
        `;

        marker.bindPopup(popupContent);
        this.markerGroup.addLayer(marker);
      }
    });
  }

  getColorByPriority(priority: string | undefined): string {
    if (!priority) return 'blue';

    switch (priority) {
      case 'critique':
        return 'red';
      case 'elevee':
        return 'orange';
      case 'moyenne':
        return 'gold';
      case 'faible':
        return 'green';
      default:
        return 'blue';
    }
  }

  get isLoading(): boolean {
    return this.loading;
  }

  get degradations(): IDegradation[] {
    return this.allDegradations;
  }

  load(): void {
    this.loadDegradations();
  }

  trackId(index: number, item: IDegradation): number {
    return item.id!;
  }

  navigateToWithComponentValues(event: any): void {
    this.sortState = event;
    this.loadDegradations();
  }

  ngOnDestroy(): void {
    if (this.map) {
      this.map.remove();
    }
  }

  delete(degradation: IDegradation): void {
    if (confirm(`Voulez-vous vraiment supprimer la dégradation #${degradation.id} ?`)) {
      this.degradationService.delete(degradation.id!).subscribe({
        next: () => {
          alert(`Dégradation #${degradation.id} supprimée avec succès.`);
          this.loadDegradations();
        },
        error: err => {
          console.error('Erreur lors de la suppression', err);
          alert('Erreur lors de la suppression.');
        },
      });
    }
  }

  showOnMap(degradation: IDegradation): void {
    const lat = Number(degradation.site?.latitude);
    const lng = Number(degradation.site?.longitude);
    if (!isNaN(lat) && !isNaN(lng)) {
      this.map.setView([lat, lng], 12);
    }
    // Navigation vers la page d'accueil
    this.router.navigate(['/home']);
  }
}
