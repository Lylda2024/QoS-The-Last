import { AfterViewInit, Component, ElementRef, ViewChild, ChangeDetectorRef, OnDestroy, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpResponse } from '@angular/common/http';
import * as L from 'leaflet';
import { Subscription } from 'rxjs';
import { NotificationExtendedService } from 'app/entities/notification/service/notification-extended.service';
import { DegradationService } from 'app/entities/degradation/service/degradation.service';
import { IDegradation } from 'app/entities/degradation/degradation.model';
import { MapService, MapLocation } from 'app/services/map.service';
import * as d3 from 'd3';
import 'leaflet.fullscreen';
import dayjs from 'dayjs';
import { ToastrModule } from 'ngx-toastr';

(window as any).d3 = d3;

@Component({
  selector: 'jhi-home',
  standalone: true,
  imports: [CommonModule, FormsModule, ToastrModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export default class HomeComponent implements AfterViewInit, OnDestroy {
  @ViewChild('mapContainer', { static: false }) mapContainer!: ElementRef<HTMLDivElement>;

  private degradationService = inject(DegradationService);
  private mapService = inject(MapService);
  private cdRef = inject(ChangeDetectorRef);
  private notifService = inject(NotificationExtendedService);

  private map!: L.Map;
  markerGroup: L.LayerGroup = L.layerGroup();

  priorities: string[] = ['Critique', 'Élevée', 'Moyenne', 'Faible'];
  selectedPriority = '';
  allDegradations: IDegradation[] = [];

  statuts: string[] = [];
  localites: string[] = [];
  acteurs: string[] = [];

  selectedStatut = '';
  selectedLocalite = '';
  selectedActeur = '';

  private degradationSub?: Subscription;
  private locationSub?: Subscription;
  private notificationIntervalId?: any;

  lastUpdate: Date = new Date();

  ngAfterViewInit(): void {
    this.fixLeafletIcons();
    this.initMap();
    this.loadDegradations();

    this.locationSub = this.mapService.location$.subscribe(loc => {
      if (loc) {
        this.addDynamicMarker(loc);
        this.mapService.clearLocation();
      }
    });

    this.checkDelaiNotifications();
    setTimeout(() => this.cdRef.detectChanges(), 0);
  }

  ngOnDestroy(): void {
    this.map?.remove();
    this.degradationSub?.unsubscribe();
    this.locationSub?.unsubscribe();
    if (this.notificationIntervalId) clearInterval(this.notificationIntervalId);
  }

  private initMap(): void {
    this.map = L.map(this.mapContainer.nativeElement, {
      center: [5.35, -4.02],
      zoom: 7,
    } as any);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors',
      maxZoom: 18,
    }).addTo(this.map);

    (L.control as any).fullscreen({ position: 'topleft' }).addTo(this.map);
    this.markerGroup.addTo(this.map);
    this.addLegend();
    setTimeout(() => this.map.invalidateSize(), 100);
  }

  private addLegend(): void {
    const legend = new L.Control({ position: 'bottomright' });
    legend.onAdd = () => {
      const div = L.DomUtil.create('div', 'info legend');
      const items: { label: string; color: string }[] = [
        { label: 'Délai respecté', color: '#16a34a' },
        { label: 'Délai proche (≤ 2j)', color: '#eab308' },
        { label: 'Délai dépassé', color: '#dc2626' },
        { label: 'Pas de délai', color: '#f9fafb' },
        { label: 'Terminée', color: '#6b7280' },
      ];
      div.innerHTML =
        '<strong>Statut délai</strong><br/>' +
        items
          .map(i => `<i style="background:${i.color};width:12px;height:12px;display:inline-block;margin-right:6px;"></i>${i.label}`)
          .join('<br/>');
      return div;
    };
    legend.addTo(this.map);
  }

  private fixLeafletIcons(): void {
    delete (L.Icon.Default.prototype as any)._getIconUrl;
    L.Icon.Default.mergeOptions({
      iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png',
      iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
      shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png',
    });
  }

  /** 🔄 Charge les dégradations et construit la carte + heatmap */
  private loadDegradations(): void {
    this.degradationSub = this.degradationService.query().subscribe({
      next: (res: HttpResponse<IDegradation[]>) => {
        this.allDegradations = res.body ?? [];
        this.extractFilterOptions();
        this.applyFilter();
        this.notifService.notifyDelaiAlertes(this.allDegradations);
        this.lastUpdate = new Date();

        // 👉 Récupérer les dates d’apparition
        const dates = this.allDegradations.filter(d => !!d.dateDetection).map(d => dayjs(d.dateDetection).startOf('day').unix());

        const aggregated = dates.reduce((acc: Record<number, number>, ts) => {
          acc[ts] = (acc[ts] || 0) + 1;
          return acc;
        }, {});

        // Transforme en tableau [{date, value}]
        const heatmapData = Object.entries(aggregated).map(([ts, count]) => ({
          date: new Date(Number(ts) * 1000),
          value: count,
        }));

        // Nettoyer le conteneur
        const container = document.getElementById('calendar-heatmap');
        if (container) container.innerHTML = '';

        // 📦 Importer CalHeatMap dynamiquement
        import('cal-heatmap').then(({ default: CalHeatMap }) => {
          const cal = new CalHeatMap();
          cal.paint({
            itemSelector: '#calendar-heatmap',
            range: 3, // afficher 3 mois
            domain: {
              type: 'month',
              gutter: 10,
              label: {
                text: 'MMM',
                position: 'top',
                height: 20,
              },
            },
            subDomain: {
              type: 'day',
              width: 20,
              height: 20,
              gutter: 4,
              radius: 4,
            },
            data: heatmapData,
            scale: {
              color: {
                type: 'threshold',
                domain: [1, 5, 10],
                range: ['#16a34a', '#eab308', '#dc2626'], // vert → jaune → rouge
              },
            },
            theme: 'light',
            tooltip: {
              enabled: true,
              text: (date: Date, value: number) => `${date.toLocaleDateString()} : ${value || 0} dégradation(s)`,
            },
            legend: [1, 5, 10],
            legendOrientation: 'horizontal',
          });
        });
      },
      error: err => console.error('[HomeComponent] Erreur chargement dégradations :', err),
    });
  }

  /** 🔄 Filtrage des dégradations */
  applyFilter(): void {
    this.markerGroup.clearLayers();
    const list = this.allDegradations.filter(d => {
      const matchPriority = this.selectedPriority ? d.priorite?.toLowerCase() === this.selectedPriority.toLowerCase() : true;
      const matchStatut = this.selectedStatut ? d.statut?.toLowerCase() === this.selectedStatut.toLowerCase() : true;
      const matchLocalite = this.selectedLocalite ? d.localite === this.selectedLocalite : true;
      const matchActeur = this.selectedActeur ? d.porteur === this.selectedActeur : true;
      return matchPriority && matchStatut && matchLocalite && matchActeur;
    });

    list.forEach(d => this.addMarkerIfValid(d));

    const layers = this.markerGroup.getLayers();
    if (layers.length) {
      const bounds = (L.featureGroup(layers as L.Layer[]) as any).getBounds().pad(0.2);
      this.map.fitBounds(bounds);
    }
    this.notifService.notifyDelaiAlertes(list);
  }

  /** Ajoute un marqueur sur la carte */
  private addMarkerIfValid(d: IDegradation): void {
    const lat = this.parseCoord(d.site?.latitude);
    const lng = this.parseCoord(d.site?.longitude);
    if (lat === null || lng === null) return;

    const color = this.getColorByDelayStatus(d);
    const marker = L.circleMarker([lat, lng], {
      radius: 8,
      color,
      fillColor: color,
      fillOpacity: 0.85,
    });
    marker.bindPopup(
      `<strong>ID #${d.id ?? '?'}</strong><br/>Localité : ${d.localite ?? '-'}<br/>Type : ${d.typeAnomalie ?? '-'}<br/>Priorité : <b>${d.priorite ?? '-'}</b><br/>Statut : ${d.statut ?? '-'}<br/>Date limite : ${d.dateLimite ? new Date(d.dateLimite).toLocaleDateString() : '-'}<br/>Responsable : ${d.porteur ?? '-'}<br/><a href="/degradation/${d.id}" target="_blank">🔎 Voir fiche</a>`,
    );
    this.markerGroup.addLayer(marker);
  }

  /** Ajoute un marqueur dynamique */
  private addDynamicMarker(loc: MapLocation): void {
    const color = this.getColorByPriority(loc.priorite?.toLowerCase());
    const marker = L.circleMarker([loc.latitude, loc.longitude], {
      radius: 10,
      color,
      fillColor: color,
      fillOpacity: 0.9,
    }).bindPopup(
      `<strong>Nouvelle dégradation</strong><br/>Priorité : <b>${loc.priorite ?? '-'}</b><br/><small>Lat : ${loc.latitude}, Lng : ${loc.longitude}</small>`,
    );
    this.markerGroup.addLayer(marker);
    this.map.flyTo([loc.latitude, loc.longitude], 15);
  }

  private getColorByPriority(priority?: string | null): string {
    const colors: Record<string, string> = {
      critique: '#dc2626',
      élevée: '#ea580c',
      moyenne: '#ca8a04',
      faible: '#16a34a',
    };
    return priority ? (colors[priority] ?? '#3b82f6') : '#3b82f6';
  }

  private getColorByDelayStatus(degradation: IDegradation): string {
    const now = new Date();
    const dateLimite = degradation.dateLimite ? new Date(degradation.dateLimite) : null;
    const statut = degradation.statut?.toLowerCase();

    if (statut === 'terminée' || statut === 'clôturée') return '#6b7280';
    if (!dateLimite) return '#f9fafb';

    const diffInMs = dateLimite.getTime() - now.getTime();
    const diffInDays = Math.ceil(diffInMs / (1000 * 60 * 60 * 24));
    if (diffInDays < 0) return '#dc2626';
    if (diffInDays <= 2) return '#eab308';
    return '#16a34a';
  }

  private parseCoord(val?: string | number | null): number | null {
    if (val === null || val === undefined) return null;
    let str = typeof val === 'number' ? val.toString() : val;
    str = str.replace(',', '.').trim();
    const n = parseFloat(str);
    return !isFinite(n) ? null : n;
  }

  clearFilter(): void {
    this.selectedPriority = '';
    this.selectedStatut = '';
    this.selectedLocalite = '';
    this.selectedActeur = '';
    this.applyFilter();
    this.notifService.resetNotified();
  }

  goToUrgences(): void {
    const urgentes = this.allDegradations.filter(d => this.getColorByDelayStatus(d) === '#dc2626');
    if (urgentes.length) {
      const first = urgentes[0];
      const lat = this.parseCoord(first.site?.latitude);
      const lng = this.parseCoord(first.site?.longitude);
      if (lat !== null && lng !== null) this.map.flyTo([lat, lng], 15);
    }
  }

  private extractFilterOptions(): void {
    const unique = <T>(arr: (T | null | undefined)[]) => [...new Set(arr.filter((v): v is T => !!v))].sort();
    this.statuts = unique(this.allDegradations.map(d => d.statut));
    this.localites = unique(this.allDegradations.map(d => d.localite));
    this.acteurs = unique(this.allDegradations.map(d => d.porteur));
  }

  // ✅ Getters stats
  get countVert(): number {
    return this.allDegradations.filter(d => this.getColorByDelayStatus(d) === '#16a34a').length;
  }
  get countJaune(): number {
    return this.allDegradations.filter(d => this.getColorByDelayStatus(d) === '#eab308').length;
  }
  get countRouge(): number {
    return this.allDegradations.filter(d => this.getColorByDelayStatus(d) === '#dc2626').length;
  }
  get countBlanc(): number {
    return this.allDegradations.filter(d => this.getColorByDelayStatus(d) === '#f9fafb').length;
  }
  get countGris(): number {
    return this.allDegradations.filter(d => this.getColorByDelayStatus(d) === '#6b7280').length;
  }

  private checkDelaiNotifications(): void {
    this.notificationIntervalId = setInterval(() => {
      this.notifService.notifyDelaiAlertes(this.allDegradations);
    }, 300000);
  }

  // ✅ Actions filtres
  setPriority(priority: string): void {
    this.selectedPriority = priority;
    this.applyFilter();
  }
  setStatut(statut: string): void {
    this.selectedStatut = statut;
    this.applyFilter();
  }
  setLocalite(localite: string): void {
    this.selectedLocalite = localite;
    this.applyFilter();
  }
  setActeur(acteur: string): void {
    this.selectedActeur = acteur;
    this.applyFilter();
  }
}
