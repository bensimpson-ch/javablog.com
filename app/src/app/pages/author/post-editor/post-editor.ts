import { Component, computed, inject, LOCALE_ID, OnDestroy, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Location } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatExpansionModule } from '@angular/material/expansion';
import { PostsService, TranslationsService, TranslatedPost, CreatePostRequest, UpdatePostRequest, LanguageCode } from '../../../api';
import { LANGUAGE_LABELS } from '../../../app';
import { BlogEditor } from '../../../components/blog-editor/blog-editor';
import { AuthService } from '../../../auth';
import { debounceTime, Subscription } from 'rxjs';

const DRAFT_STORAGE_KEY = 'javablog-post-draft';

@Component({
  selector: 'app-post-editor',
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    MatSelectModule,
    MatCheckboxModule,
    MatExpansionModule,
    BlogEditor
  ],
  templateUrl: './post-editor.html',
  styleUrl: './post-editor.scss'
})
export class PostEditor implements OnInit, OnDestroy {
  private fb = inject(FormBuilder);
  private postsService = inject(PostsService);
  private translationsService = inject(TranslationsService);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private location = inject(Location);
  private locale = inject(LOCALE_ID).split('-')[0] as LanguageCode;
  private authService = inject(AuthService);
  private formSubscription?: Subscription;

  authorDisplay = computed(() => {
    const name = this.authService.userName;
    const email = this.authService.email;
    if (name && email) return `${name} <${email}>`;
    return name || email || 'Unknown';
  });

  languageLabels = LANGUAGE_LABELS;
  languages = Object.values(LanguageCode);

  postId = signal<string | null>(null);
  loading = signal(false);
  submitting = signal(false);
  hasDraft = signal(false);
  selectedLanguages = signal(new Set<LanguageCode>());
  existingTranslations = signal<TranslatedPost[]>([]);

  translateAll = signal(true);

  translationLanguages = computed(() => {
    const postLang = this.form.get('language')?.value;
    return this.languages.filter(l => l !== postLang);
  });

  hasTranslationChanges = computed(() => {
    const existingLangs = new Set(this.existingTranslations().map(t => t.language));
    const currentLangs = this.translateAll()
      ? new Set(this.translationLanguages())
      : this.selectedLanguages();
    if (currentLangs.size !== existingLangs.size) return true;
    for (const lang of currentLangs) {
      if (!existingLangs.has(lang)) return true;
    }
    return false;
  });

  form = this.fb.group({
    title: ['', [Validators.required, Validators.maxLength(200)]],
    slug: ['', [Validators.required, Validators.pattern(/^[a-z0-9-]+$/)]],
    summary: ['', [Validators.required, Validators.maxLength(500)]],
    content: ['', Validators.required],
    language: [this.defaultLanguage(), Validators.required]
  });

  get isEditMode(): boolean {
    return this.postId() !== null;
  }

  private defaultLanguage(): LanguageCode {
    return Object.values(LanguageCode).includes(this.locale) ? this.locale : LanguageCode.En;
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('postId');
    if (id) {
      this.postId.set(id);
      this.loadPost(id);
    } else {
      this.restoreDraft();
      this.setupAutoSave();
    }
  }

  ngOnDestroy(): void {
    this.formSubscription?.unsubscribe();
  }

  private restoreDraft(): void {
    const draft = localStorage.getItem(DRAFT_STORAGE_KEY);
    if (draft) {
      try {
        const parsed = JSON.parse(draft);
        this.form.patchValue(parsed);
        this.hasDraft.set(true);
        this.snackBar.open('Draft restored', 'Dismiss', { duration: 3000 });
      } catch {
        localStorage.removeItem(DRAFT_STORAGE_KEY);
      }
    }
  }

  private setupAutoSave(): void {
    this.formSubscription = this.form.valueChanges
      .pipe(debounceTime(1000))
      .subscribe(value => {
        if (value.title || value.slug || value.summary || value.content) {
          localStorage.setItem(DRAFT_STORAGE_KEY, JSON.stringify(value));
          this.hasDraft.set(true);
        }
      });
  }

  cancel(): void {
    this.location.back();
  }

  clearDraft(): void {
    localStorage.removeItem(DRAFT_STORAGE_KEY);
    this.form.reset();
    this.hasDraft.set(false);
    this.snackBar.open('Draft cleared', 'Dismiss', { duration: 2000 });
  }

  private loadPost(id: string): void {
    this.loading.set(true);
    this.postsService.getPost(id).subscribe({
      next: (post) => {
        this.form.patchValue({
          title: post.title,
          slug: post.slug,
          summary: post.summary,
          content: post.content,
          language: post.language
        });
        this.loading.set(false);
        this.loadTranslations(id);
      },
      error: (err) => {
        this.loading.set(false);
        this.snackBar.open('Failed to load post', 'Dismiss', { duration: 5000 });
        console.error('Failed to load post:', err);
        this.router.navigate(['/author/posts/new']);
      }
    });
  }

  private loadTranslations(postId: string): void {
    this.translationsService.listTranslations(postId).subscribe({
      next: (translations) => {
        this.existingTranslations.set(translations);
        const translatedLangs = new Set(translations.map(t => t.language));
        this.selectedLanguages.set(translatedLangs);
        this.translateAll.set(translatedLangs.size === this.translationLanguages().length);
      },
      error: (err) => console.error('Failed to load translations:', err)
    });
  }

  generateSlug(): void {
    if (this.isEditMode) return;
    const title = this.form.get('title')?.value || '';
    const slug = title
      .toLowerCase()
      .replace(/[^a-z0-9\s-]/g, '')
      .replace(/\s+/g, '-')
      .replace(/-+/g, '-')
      .replace(/^-|-$/g, '');
    this.form.patchValue({ slug });
  }

  submit(): void {
    if (this.form.invalid || this.submitting()) {
      return;
    }

    this.submitting.set(true);

    if (this.isEditMode) {
      this.updatePost();
    } else {
      this.createPost();
    }
  }

  private createPost(): void {
    const request: CreatePostRequest = {
      title: this.form.value.title!,
      slug: this.form.value.slug!,
      summary: this.form.value.summary!,
      content: this.form.value.content!,
      language: this.form.value.language!
    };

    this.postsService.createPost(request).subscribe({
      next: () => {
        localStorage.removeItem(DRAFT_STORAGE_KEY);
        this.snackBar.open('Post created successfully', 'Dismiss', { duration: 3000 });
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.submitting.set(false);
        this.snackBar.open('Failed to create post: ' + (err.message || 'Unknown error'), 'Dismiss', {
          duration: 5000
        });
        console.error('Failed to create post:', err);
      }
    });
  }

  private updatePost(): void {
    const request: UpdatePostRequest = {
      title: this.form.value.title!,
      slug: this.form.value.slug!,
      summary: this.form.value.summary!,
      content: this.form.value.content!,
      language: this.form.value.language!
    };

    this.postsService.updatePost(this.postId()!, request).subscribe({
      next: () => {
        this.requestTranslations();
        this.snackBar.open('Post updated successfully', 'Dismiss', { duration: 3000 });
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.submitting.set(false);
        this.snackBar.open('Failed to update post: ' + (err.message || 'Unknown error'), 'Dismiss', {
          duration: 5000
        });
        console.error('Failed to update post:', err);
      }
    });
  }

  private requestTranslations(): void {
    const languages = this.translateAll() ? [...this.translationLanguages()] : [...this.selectedLanguages()];
    if (languages.length === 0) return;

    this.translationsService.requestTranslation(this.postId()!, { languages }).subscribe({
      error: (err) => console.error('Translation request failed:', err)
    });
  }

  toggleAllLanguages(checked: boolean): void {
    this.translateAll.set(checked);
    if (checked) {
      this.selectedLanguages.set(new Set(this.translationLanguages()));
    } else {
      this.selectedLanguages.set(new Set());
    }
  }

  toggleLanguage(lang: LanguageCode): void {
    const current = new Set(this.selectedLanguages());
    if (current.has(lang)) {
      current.delete(lang);
      this.translateAll.set(false);
    } else {
      current.add(lang);
      if (current.size === this.translationLanguages().length) {
        this.translateAll.set(true);
      }
    }
    this.selectedLanguages.set(current);
  }

}
