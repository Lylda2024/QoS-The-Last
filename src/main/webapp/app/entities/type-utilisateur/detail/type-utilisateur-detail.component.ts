import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ITypeUtilisateur } from '../type-utilisateur.model';

@Component({
  selector: 'jhi-type-utilisateur-detail',
  templateUrl: './type-utilisateur-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class TypeUtilisateurDetailComponent {
  typeUtilisateur = input<ITypeUtilisateur | null>(null);

  previousState(): void {
    window.history.back();
  }
}
