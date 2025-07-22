import { AfterViewInit, Component, ElementRef, ViewChild, ChangeDetectorRef, OnDestroy, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpResponse } from '@angular/common/http';
import { getActiveDelai } from 'app/entities/degradation/degradation.model';

import * as L from 'leaflet';
import { Subscription } from 'rxjs';
import { NotificationExtendedService } from 'app/entities/notification/service/notification-extended.service';
import { DegradationService } from 'app/entities/degradation/service/degradation.service';
import { IDegradation, IDelaiIntervention } from 'app/entities/degradation/degradation.model';
import { MapService, MapLocation } from 'app/services/map.service';
import * as d3 from 'd3';
import 'leaflet.fullscreen';
import { ToastrModule } from 'ngx-toastr';
import dayjs, { Dayjs } from 'dayjs';

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

  priorities: string[] = ['P1', 'P2', 'P3'];
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
    const el = this.mapContainer.nativeElement;
    if (!el.offsetHeight) {
      el.style.height = '70vh';
      el.style.minHeight = '400px';
    }
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
    this.map = L.map(this.mapContainer.nativeElement, { center: [5.35, -4.02], zoom: 7 } as any);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors',
      maxZoom: 18,
    }).addTo(this.map);
    (L.control as any).fullscreen({ position: 'topleft' }).addTo(this.map);
    this.markerGroup.addTo(this.map);
    this.addLegend();
    setTimeout(() => this.map.invalidateSize(), 200);
  }

  private addLegend(): void {
    const legend = new L.Control({ position: 'bottomright' });
    legend.onAdd = () => {
      const div = L.DomUtil.create('div', 'info legend');
      const items = [
        { label: 'Sans délai (P1)', color: '#dc2626' },
        { label: 'Sans délai (P2)', color: '#eab308' },
        { label: 'Sans délai (P3)', color: '#16a34a' },
        { label: 'Terminé', color: '#6b7280' },
      ];
      div.innerHTML =
        '<strong>Légende priorité / délai</strong><br/>' +
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

  private loadDegradations(): void {
    this.degradationSub = this.degradationService.query().subscribe({
      next: (res: HttpResponse<IDegradation[]>) => {
        // Conversion des dates string en Dayjs pour éviter erreurs
        this.allDegradations = (res.body ?? []).map(d => ({
          ...d,
          dateDetection: d.dateDetection ? dayjs(d.dateDetection) : null,
          dateLimite: d.dateLimite ? dayjs(d.dateLimite) : null,
          delais:
            d.delais?.map(delai => ({
              ...delai,
              dateDebut: delai.dateDebut ? dayjs(delai.dateDebut) : null,
              dateLimite: delai.dateLimite ? dayjs(delai.dateLimite) : null,
            })) ?? null,
        }));
        this.extractFilterOptions();
        this.applyFilter();
        this.notifService.notifyDelaiAlertes(this.allDegradations);
        this.lastUpdate = new Date();
      },
      error: err => console.error('[HomeComponent] Erreur chargement dégradations :', err),
    });
  }

  applyFilter(): void {
    this.markerGroup.clearLayers();
    const list = this.allDegradations.filter(d => {
      const matchPriority = this.selectedPriority ? d.priorite === this.selectedPriority : true;
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

  private extractFilterOptions(): void {
    const unique = <T>(arr: (T | null | undefined)[]) => [...new Set(arr.filter(v => v != null))].sort() as T[];
    this.statuts = unique(this.allDegradations.map(d => d.statut));
    this.localites = unique(this.allDegradations.map(d => d.localite));
    this.acteurs = unique(this.allDegradations.map(d => d.porteur));
  }

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
      `<strong>ID #${d.id ?? '?'}</strong><br/>
       Localité : ${d.localite ?? '-'}<br/>
       Type : ${d.typeAnomalie ?? '-'}<br/>
       Priorité : <b>${d.priorite ?? '-'}</b><br/>
       Statut : ${d.statut ?? '-'}<br/>
       Date limite : ${this.formatDateLimite(d)}<br/>
       Responsable : ${d.porteur ?? '-'}<br/>
       <a href="/degradation/${d.id}" target="_blank">🔎 Voir fiche</a>`,
    );

    this.markerGroup.addLayer(marker);
  }

  private formatDateLimite(d: IDegradation): string {
    const delai = getActiveDelai(d);
    return delai?.dateLimite ? delai.dateLimite.format('DD/MM/YYYY') : 'Sans délai';
  }

  private getColorByDelayStatus(degradation: IDegradation): string {
    const now = new Date();
    const statut = degradation.statut?.toLowerCase();
    const priorite = degradation.priorite?.toUpperCase();

    if (statut === 'terminée' || statut === 'clôturée') return '#6b7280';

    let dateLimite: Date | null = null;

    const delai = getActiveDelai(degradation);
    if (delai?.dateLimite) {
      dateLimite = delai.dateLimite.toDate();
    } else {
      const jours = priorite === 'P1' ? 5 : priorite === 'P2' ? 10 : priorite === 'P3' ? 20 : null;
      if (jours !== null && degradation.dateDetection) {
        // conversion sécurisée avec dayjs
        const detectionDayjs: Dayjs = dayjs(degradation.dateDetection);
        dateLimite = detectionDayjs.add(jours, 'day').toDate();
      }
    }

    if (!dateLimite) return '#6b7280';

    const diffDays = Math.ceil((dateLimite.getTime() - now.getTime()) / (1000 * 60 * 60 * 24));
    if (diffDays < 0) return '#dc2626'; // rouge
    if (diffDays <= 2) return '#eab308'; // jaune
    return '#16a34a'; // vert
  }

  private parseCoord(val?: string | number | null): number | null {
    if (val == null) return null;
    const n = parseFloat(String(val).replace(',', '.'));
    return isFinite(n) ? n : null;
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

  private checkDelaiNotifications(): void {
    this.notificationIntervalId = setInterval(() => {
      this.notifService.notifyDelaiAlertes(this.allDegradations);
    }, 300000);
  }

  setPriority(p: string): void {
    this.selectedPriority = p;
    this.applyFilter();
  }

  setStatut(s: string): void {
    this.selectedStatut = s;
    this.applyFilter();
  }

  setLocalite(l: string): void {
    this.selectedLocalite = l;
    this.applyFilter();
  }

  setActeur(a: string): void {
    this.selectedActeur = a;
    this.applyFilter();
  }

  private addDynamicMarker(loc: MapLocation): void {
    const color = this.getColorByPriority(loc.priorite);
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
    switch (priority) {
      case 'P1':
        return '#dc2626';
      case 'P2':
        return '#ea580c';
      case 'P3':
        return '#ca8a04';
      default:
        return '#3b82f6';
    }
  }

  get countVert(): number {
    return this.allDegradations.filter(d => this.getColorByDelayStatus(d) === '#16a34a').length;
  }

  get countJaune(): number {
    return this.allDegradations.filter(d => this.getColorByDelayStatus(d) === '#eab308').length;
  }

  get countRouge(): number {
    return this.allDegradations.filter(d => this.getColorByDelayStatus(d) === '#dc2626').length;
  }

  get countSansDateLimite(): number {
    return this.allDegradations.filter(d => !d.dateLimite).length;
  }

  get countGris(): number {
    return this.allDegradations.filter(d => this.getColorByDelayStatus(d) === '#6b7280').length;
  }
}
