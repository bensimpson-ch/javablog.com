import { Injectable, inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { OAuthService } from 'angular-oauth2-oidc';
import { getAuthConfig } from './auth.config';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private oauthService = inject(OAuthService);
  private isBrowser = isPlatformBrowser(inject(PLATFORM_ID));

  constructor() {
    if (this.isBrowser) {
      this.oauthService.configure(getAuthConfig());
    }
  }

  async tryLogin(): Promise<boolean> {
    if (!this.isBrowser) return false;
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
    if (!this.isBrowser) return false;
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
