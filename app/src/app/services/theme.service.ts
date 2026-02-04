import { Injectable, signal, effect, inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

export type Theme = 'light' | 'dark';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  private readonly STORAGE_KEY = 'theme-preference';
  private isBrowser: boolean;

  readonly theme = signal<Theme>('light');

  constructor() {
    this.isBrowser = isPlatformBrowser(inject(PLATFORM_ID));
    this.theme.set(this.getInitialTheme());

    effect(() => {
      const theme = this.theme();
      if (this.isBrowser) {
        document.documentElement.setAttribute('data-theme', theme);
        localStorage.setItem(this.STORAGE_KEY, theme);
      }
    });

    if (this.isBrowser) {
      window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', (e) => {
        if (!localStorage.getItem(this.STORAGE_KEY)) {
          this.theme.set(e.matches ? 'dark' : 'light');
        }
      });
    }
  }

  private getInitialTheme(): Theme {
    if (!this.isBrowser) return 'light';
    const stored = localStorage.getItem(this.STORAGE_KEY) as Theme | null;
    if (stored) return stored;
    return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';
  }

  toggle(): void {
    this.theme.set(this.theme() === 'light' ? 'dark' : 'light');
  }
}
