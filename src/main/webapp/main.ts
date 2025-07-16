import { bootstrapApplication } from '@angular/platform-browser';
import AppComponent from './app/app.component';
import { appConfig } from './app/app.config';

bootstrapApplication(AppComponent, appConfig)
  .then(ref => {
    const iconInit = ref.injector.get('fa-icon-init', null);
    if (iconInit) iconInit();
  })
  .catch(err => console.error(err));
