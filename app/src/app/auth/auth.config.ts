import { AuthConfig } from 'angular-oauth2-oidc';
import { environment } from '../../environments/environment';

export function getAuthConfig(): AuthConfig {
  return {
    issuer: environment.keycloak.issuer,
    clientId: environment.keycloak.clientId,
    redirectUri: typeof window !== 'undefined' ? window.location.origin : '',
    responseType: 'code',
    scope: 'openid profile email',
    showDebugInformation: !environment.production
  };
}
