import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { APP_INITIALIZER } from '@angular/core';

export function appInitializer() {
  return () => new Promise<void>(resolve => resolve());
}

export const config = {
  providers: [
    { provide: APP_INITIALIZER, useFactory: appInitializer, multi: true },
  ],
};

export default bootstrapApplication(AppComponent, config).catch(err => console.error(err));
