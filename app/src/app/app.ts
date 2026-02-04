import { Component, computed, ElementRef, HostListener, inject, OnInit, PLATFORM_ID, LOCALE_ID, signal } from '@angular/core';
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
  private elementRef = inject(ElementRef);

  readonly currentLocale = inject(LOCALE_ID).split('-')[0];
  protected currentPath = signal('/');
  protected menuOpen = signal(false);
  protected langOpen = signal(false);

  protected themeToggleTitle = computed(() =>
    this.themeService.theme() === 'light'
      ? $localize`:@@theme.darkMode:Dark mode`
      : $localize`:@@theme.lightMode:Light mode`
  );

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    if (!this.menuOpen()) return;
    if (!this.elementRef.nativeElement.querySelector('.nav-actions')?.contains(event.target as Node)) {
      this.closeMenu();
    }
  }

  toggleMenu(event: MouseEvent): void {
    event.stopPropagation();
    this.menuOpen.update(v => !v);
    if (!this.menuOpen()) {
      this.langOpen.set(false);
    }
  }

  toggleLang(event: MouseEvent): void {
    event.stopPropagation();
    this.langOpen.update(v => !v);
  }

  closeMenu(): void {
    this.menuOpen.set(false);
    this.langOpen.set(false);
  }

  login(): void {
    this.closeMenu();
    this.authService.login();
  }

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
    this.currentPath.set(url.replace(/^\/(en|de)/, '') || '/');
  }

  private injectHreflangTags(): void {
    const path = this.router.url.replace(/^\/(en|de)/, '') || '/';
    const base = 'https://www.javablog.com';

    this.setHreflang('en', `${base}${path}`);
    this.setHreflang('de', `${base}/de${path}`);
    this.setHreflang('x-default', `${base}${path}`);
  }

  private updateHreflangTags(): void {
    const path = this.currentPath();
    const base = 'https://www.javablog.com';

    this.setHreflang('en', `${base}${path}`);
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
