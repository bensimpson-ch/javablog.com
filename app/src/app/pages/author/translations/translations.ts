import { Component, inject, OnInit, signal } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { PostsService, TranslationsService, PostResponse, TranslatedPost, LanguageCode } from '../../../api';
import { LANGUAGE_LABELS } from '../../../app';
import { forkJoin } from 'rxjs';

interface PostRow {
  id: string;
  slug: string;
  createdAt: string;
  selectedLanguages: Set<LanguageCode>;
  existingLanguages: Set<LanguageCode>;
  translateAll: boolean;
  updating: boolean;
}

@Component({
  selector: 'app-translations',
  imports: [
    MatCardModule,
    MatCheckboxModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
  ],
  templateUrl: './translations.html',
  styleUrl: './translations.scss'
})
export class Translations implements OnInit {
  private postsService = inject(PostsService);
  private translationsService = inject(TranslationsService);
  private snackBar = inject(MatSnackBar);

  loading = signal(true);
  rows = signal<PostRow[]>([]);

  languageLabels = LANGUAGE_LABELS;
  languages = Object.values(LanguageCode).filter(l => l !== LanguageCode.En);

  ngOnInit(): void {
    this.loadPosts();
  }

  private loadPosts(): void {
    this.loading.set(true);
    this.postsService.listPosts(LanguageCode.En).subscribe({
      next: (posts) => {
        const sorted = posts.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
        const rows: PostRow[] = sorted.map(p => ({
          id: p.id,
          slug: p.slug,
          createdAt: p.createdAt,
          selectedLanguages: new Set<LanguageCode>(),
          existingLanguages: new Set<LanguageCode>(),
          translateAll: false,
          updating: false,
        }));
        this.rows.set(rows);
        this.loadAllTranslations(rows);
      },
      error: () => {
        this.loading.set(false);
        this.snackBar.open('Failed to load posts', 'Dismiss', { duration: 5000 });
      }
    });
  }

  private loadAllTranslations(rows: PostRow[]): void {
    if (rows.length === 0) {
      this.loading.set(false);
      return;
    }

    const requests = rows.map(r => this.translationsService.listTranslations(r.id));
    forkJoin(requests).subscribe({
      next: (results) => {
        const updated = rows.map((row, i) => {
          const langs = new Set(results[i].map(t => t.language));
          return {
            ...row,
            existingLanguages: langs,
            selectedLanguages: new Set(langs),
            translateAll: langs.size === this.languages.length,
          };
        });
        this.rows.set(updated);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  toggleLanguage(row: PostRow, lang: LanguageCode): void {
    const selected = new Set(row.selectedLanguages);
    if (selected.has(lang)) {
      selected.delete(lang);
    } else {
      selected.add(lang);
    }
    row.selectedLanguages = selected;
    row.translateAll = selected.size === this.languages.length;
  }

  toggleAll(row: PostRow, checked: boolean): void {
    row.translateAll = checked;
    row.selectedLanguages = checked ? new Set(this.languages) : new Set<LanguageCode>();
  }

  update(row: PostRow): void {
    const languages = [...row.selectedLanguages];
    if (languages.length === 0) return;

    row.updating = true;
    this.translationsService.requestTranslation(row.id, { languages }).subscribe({
      next: () => {
        row.updating = false;
        row.existingLanguages = new Set(row.selectedLanguages);
        this.snackBar.open(`Translation requested for ${row.slug}`, 'Dismiss', { duration: 3000 });
      },
      error: () => {
        row.updating = false;
        this.snackBar.open(`Failed to request translations for ${row.slug}`, 'Dismiss', { duration: 5000 });
      }
    });
  }

  hasChanges(row: PostRow): boolean {
    if (row.selectedLanguages.size !== row.existingLanguages.size) return true;
    for (const lang of row.selectedLanguages) {
      if (!row.existingLanguages.has(lang)) return true;
    }
    return false;
  }
}
