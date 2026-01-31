import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const oauthService = inject(OAuthService);

  if (oauthService.hasValidAccessToken() && req.url.includes('/v1/')) {
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${oauthService.getAccessToken()}`
      }
    });
    return next(authReq);
  }

  return next(req);
};
