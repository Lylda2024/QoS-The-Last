import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { HttpClient } from '@angular/common/http';
import { NotificationService } from './notification.service';
import { IDegradation } from '../../degradation/degradation.model';
import dayjs from 'dayjs';

@Injectable({
  providedIn: 'root',
})
export class NotificationExtendedService extends NotificationService {
  private notified = false;

  constructor(
    http: HttpClient,
    private toastr: ToastrService,
  ) {
    super(http);
  }

  notifyDelaiAlertes(degradations: IDegradation[]): void {
    if (this.notified) return;

    const rouges = degradations.filter(d => this.getColorByDelayStatus(d) === '#dc2626').length;
    const jaunes = degradations.filter(d => this.getColorByDelayStatus(d) === '#eab308').length;

    if (rouges > 0 || jaunes > 0) {
      const msg = `⚠️ ${jaunes} proche(s) du délai et ${rouges} dépassée(s)`;
      this.toastr.warning(msg, 'Alerte Délais');
      this.playSound();
      this.notified = true;
    }
  }

  resetNotified(): void {
    this.notified = false;
  }

  private playSound(): void {
    const audio = new Audio('assets/notification.mp3');
    audio.play().catch(() => {});
  }

  private getColorByDelayStatus(d: IDegradation): string {
    const now = new Date();
    // Ici on convertit dayjs en Date natif avec toDate()
    const dateLimite = d.dateLimite ? d.dateLimite.toDate() : null;
    const statut = d.statut?.toLowerCase();

    if (statut === 'terminée' || statut === 'clôturée') return '#6b7280';
    if (!dateLimite) return '#f9fafb';

    const diffDays = Math.ceil((dateLimite.getTime() - now.getTime()) / (1000 * 60 * 60 * 24));

    if (diffDays < 0) return '#dc2626';
    if (diffDays <= 2) return '#eab308';
    return '#16a34a';
  }
}
