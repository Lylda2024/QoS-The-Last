import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'codeStageApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'type-utilisateur',
    data: { pageTitle: 'codeStageApp.typeUtilisateur.home.title' },
    loadChildren: () => import('./type-utilisateur/type-utilisateur.routes'),
  },
  {
    path: 'utilisateur',
    data: { pageTitle: 'codeStageApp.utilisateur.home.title' },
    loadChildren: () => import('./utilisateur/utilisateur.routes'),
  },
  {
    path: 'role',
    data: { pageTitle: 'codeStageApp.role.home.title' },
    loadChildren: () => import('./role/role.routes'),
  },
  {
    path: 'site',
    data: { pageTitle: 'codeStageApp.site.home.title' },
    loadChildren: () => import('./site/site.routes'),
  },
  {
    path: 'degradation',
    data: { pageTitle: 'codeStageApp.degradation.home.title' },
    loadChildren: () => import('./degradation/degradation.routes'),
  },
  {
    path: 'historique',
    data: { pageTitle: 'codeStageApp.historique.home.title' },
    loadChildren: () => import('./historique/historique.routes'),
  },
  {
    path: 'notification',
    data: { pageTitle: 'codeStageApp.notification.home.title' },
    loadChildren: () => import('./notification/notification.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
