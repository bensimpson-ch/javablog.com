import { Component, computed, ElementRef, HostListener, inject, OnInit, PLATFORM_ID, LOCALE_ID, signal } from '@angular/core';
import { DOCUMENT, isPlatformBrowser } from '@angular/common';
import { Router, RouterLink, RouterOutlet, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { ThemeService } from './services/theme.service';
import { AuthService } from './auth';
// @ts-ignore
import { LanguageCode } from './api';

interface Language {
  code: LanguageCode;
  label: string;
}

const LANGUAGE_LABELS: Record<LanguageCode, string> = {
  [LanguageCode.Bg]: 'Български',
  [LanguageCode.Hr]: 'Hrvatski',
  [LanguageCode.Cs]: 'Čeština',
  [LanguageCode.Da]: 'Dansk',
  [LanguageCode.Nl]: 'Nederlands',
  [LanguageCode.En]: 'English',
  [LanguageCode.Et]: 'Eesti',
  [LanguageCode.Fi]: 'Suomi',
  [LanguageCode.Fr]: 'Français',
  [LanguageCode.De]: 'Deutsch',
  [LanguageCode.El]: 'Ελληνικά',
  [LanguageCode.Hu]: 'Magyar',
  [LanguageCode.Ga]: 'Gaeilge',
  [LanguageCode.It]: 'Italiano',
  [LanguageCode.Lv]: 'Latviešu',
  [LanguageCode.Lt]: 'Lietuvių',
  [LanguageCode.Mt]: 'Malti',
  [LanguageCode.Pl]: 'Polski',
  [LanguageCode.Pt]: 'Português',
  [LanguageCode.Ro]: 'Română',
  [LanguageCode.Sk]: 'Slovenčina',
  [LanguageCode.Sl]: 'Slovenščina',
  [LanguageCode.Es]: 'Español',
  [LanguageCode.Sv]: 'Svenska',
  [LanguageCode.Ru]: 'Русский',
  [LanguageCode.Uk]: 'Українська',
  [LanguageCode.Be]: 'Беларуская',
  [LanguageCode.Sq]: 'Shqip',
  [LanguageCode.No]: 'Norsk',
  [LanguageCode.Is]: 'Íslenska',
  [LanguageCode.Ca]: 'Català',
  [LanguageCode.Eu]: 'Euskara',
  [LanguageCode.Gd]: 'Gàidhlig',
  [LanguageCode.Cy]: 'Cymraeg',
  [LanguageCode.Zh]: '简体中文',
  [LanguageCode.Tw]: '臺灣華語',
  [LanguageCode.Hi]: 'हिन्दी',
  [LanguageCode.Ar]: 'العربية',
  [LanguageCode.Id]: 'Bahasa Indonesia',
  [LanguageCode.Bn]: 'বাংলা',
  [LanguageCode.Ja]: '日本語',
  [LanguageCode.Ko]: '한국어',
  [LanguageCode.Tr]: 'Türkçe',
  [LanguageCode.He]: 'עברית',
};

const DISPLAYED_CODES: LanguageCode[] = [
  LanguageCode.En,
  LanguageCode.De,
  LanguageCode.Es,
  LanguageCode.Fr,
  LanguageCode.Id,
  LanguageCode.It,
  LanguageCode.Nl,
  LanguageCode.Pt,
  LanguageCode.Ro,
  LanguageCode.Sv,
  LanguageCode.Ru,
  LanguageCode.El,
  LanguageCode.Ar,
  LanguageCode.Ja,
  LanguageCode.Ko,
  LanguageCode.Zh,
  LanguageCode.Tw,
  LanguageCode.Tr,
  LanguageCode.He,
];

const ALL_LANGUAGES: Language[] = DISPLAYED_CODES
  .map(code => ({code, label: LANGUAGE_LABELS[code]}));

const LANGUAGE_MAP = new Map(ALL_LANGUAGES.map(l => [l.code as string, l]));

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
  protected allLanguages = ALL_LANGUAGES;
  protected pinnedLanguages = signal<Language[]>([LANGUAGE_MAP.get(LanguageCode.En)!]);

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

    this.initPinnedLanguages();

    const hasCode = window.location.search.includes('code=');
    const loggedIn = await this.authService.tryLogin();

    if (loggedIn && hasCode) {
      this.router.navigate(['/'], { replaceUrl: true });
    }
  }

  private updateCurrentPath(url: string): void {
    this.currentPath.set(url.replace(/^\/[a-z]{2}(?=\/|$)/, '') || '/');
  }

  langHref(code: string): string {
    return code === 'en' ? this.currentPath() : `/${code}${this.currentPath()}`;
  }

  private initPinnedLanguages(): void {
    const browserLangs = navigator.languages
      .map(l => l.split('-')[0].toLowerCase())
      .filter(code => LANGUAGE_MAP.has(code));
    const seen = new Set<string>();
    const pinned: Language[] = [];
    pinned.push(LANGUAGE_MAP.get(LanguageCode.En)!);
    seen.add(LanguageCode.En);
    for (const code of browserLangs) {
      if (!seen.has(code)) {
        pinned.push(LANGUAGE_MAP.get(code)!);
        seen.add(code);
      }
    }
    this.pinnedLanguages.set(pinned);
  }

  private injectHreflangTags(): void {
    const path = this.router.url.replace(/^\/[a-z]{2}(?=\/|$)/, '') || '/';
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
