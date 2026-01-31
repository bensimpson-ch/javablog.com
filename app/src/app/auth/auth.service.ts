import { Injectable, inject } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { authConfig } from './auth.config';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private oauthService = inject(OAuthService);

  constructor() {
    this.oauthService.configure(authConfig);
  }

  async tryLogin(): Promise<boolean> {
    await this.oauthService.loadDiscoveryDocumentAndTryLogin();
    return this.oauthService.hasValidAccessToken();
  }

  login(): void {
    this.oauthService.initCodeFlow();
  }

  logout(): void {
    this.oauthService.logOut();
  }

  get isAuthenticated(): boolean {
    return this.oauthService.hasValidAccessToken();
  }

  get accessToken(): string {
    return this.oauthService.getAccessToken();
  }

  get userName(): string | null {
    const claims = this.oauthService.getIdentityClaims();
    return claims ? (claims as any)['preferred_username'] || (claims as any)['name'] : null;
  }
}
