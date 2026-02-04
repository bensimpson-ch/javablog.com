import { Component, computed, inject, OnInit, PLATFORM_ID, LOCALE_ID, signal } from '@angular/core';
import { DOCUMENT, isPlatformBrowser } from '@angular/common';
import { Router, RouterLink, RouterOutlet, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { ThemeService } from './services/theme.service';
import { AuthService } from './auth';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit {
  protected themeService = inject(ThemeService);
  protected authService = inject(AuthService);
  private router = inject(Router);
  private platformId = inject(PLATFORM_ID);
  private document = inject(DOCUMENT);

  readonly currentLocale = inject(LOCALE_ID).split('-')[0];
  protected currentPath = signal('/');

  protected themeToggleAriaLabel = computed(() =>
    this.themeService.theme() === 'light'
      ? $localize`:@@theme.switchToDark:Switch to dark mode`
      : $localize`:@@theme.switchToLight:Switch to light mode`
  );

  protected themeToggleTitle = computed(() =>
    this.themeService.theme() === 'light'
      ? $localize`:@@theme.darkMode:Dark mode`
      : $localize`:@@theme.lightMode:Light mode`
  );

  async ngOnInit(): Promise<void> {
    this.injectHreflangTags();
    this.updateCurrentPath(this.router.url);

    this.router.events.pipe(
      filter((e): e is NavigationEnd => e instanceof NavigationEnd)
    ).subscribe(e => {
      this.updateCurrentPath(e.urlAfterRedirects);
      this.updateHreflangTags();
    });

    if (!isPlatformBrowser(this.platformId)) return;

    const hasCode = window.location.search.includes('code=');
    const loggedIn = await this.authService.tryLogin();

    if (loggedIn && hasCode) {
      this.router.navigate(['/'], { replaceUrl: true });
    }
  }

  private updateCurrentPath(url: string): void {
    // Strip locale prefix if present, keep the app-relative path
    this.currentPath.set(url.replace(/^\/(en|de)/, '') || '/');
  }

  private injectHreflangTags(): void {
    const path = this.router.url.replace(/^\/(en|de)/, '') || '/';
    const base = 'https://www.javablog.com';

    this.setHreflang('en', `${base}/en${path}`);
    this.setHreflang('de', `${base}/de${path}`);
    this.setHreflang('x-default', `${base}${path}`);
  }

  private updateHreflangTags(): void {
    const path = this.currentPath();
    const base = 'https://www.javablog.com';

    this.setHreflang('en', `${base}/en${path}`);
    this.setHreflang('de', `${base}/de${path}`);
    this.setHreflang('x-default', `${base}${path}`);
  }

  private setHreflang(lang: string, href: string): void {
    const head = this.document.head;
    let link = head.querySelector(`link[rel="alternate"][hreflang="${lang}"]`) as HTMLLinkElement;
    if (!link) {
      link = this.document.createElement('link');
      link.rel = 'alternate';
      link.setAttribute('hreflang', lang);
      head.appendChild(link);
    }
    link.href = href;
  }
}
