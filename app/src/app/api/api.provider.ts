/**
 * API Configuration Provider
 *
 * After running `npm run generate-client`, add this provider to app.config.ts:
 *
 *   import { provideApi } from './api/api.provider';
 *
 *   export const appConfig: ApplicationConfig = {
 *     providers: [
 *       ...
 *       ...provideApi()
 *     ]
 *   };
 */
import { Provider } from '@angular/core';
import { Configuration } from './v1';
import { environment } from '../../environments/environment';

export function provideApi(): Provider[] {
  return [
    {
      provide: Configuration,
      useFactory: () => new Configuration({
        basePath: environment.apiBasePath
      })
    }
  ];
}
