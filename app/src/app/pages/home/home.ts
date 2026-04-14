import { DOCUMENT } from '@angular/common';
import { Component, HostListener, inject, LOCALE_ID, OnDestroy, signal } from '@angular/core';
import { Meta, Title } from '@angular/platform-browser';
import { RouterLink } from '@angular/router';
import { LanguageCode, PostsService, PostResponse } from '../../api';
import { AuthService } from '../../auth';

@Component({
  selector: 'app-home',
  imports: [RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class Home implements OnDestroy {
  private postsService = inject(PostsService);
  protected authService = inject(AuthService);
  private fullLocaleId = inject(LOCALE_ID);
  private locale = this.fullLocaleId.split('-')[0] as LanguageCode;
  private document = inject(DOCUMENT);

  posts = signal<PostResponse[]>([]);
  lightboxImage = signal<string | null>(null);
  loading = signal<boolean>(true);
  error = signal<string | null>(null);
  deleting = signal<string | null>(null);
  private jsonLdScript: HTMLScriptElement | null = null;

  @HostListener('click', ['$event'])
  onClick(event: MouseEvent): void {
    const target = event.target as HTMLElement;
    if (target.classList.contains('lightbox-trigger')) {
      const src = target.getAttribute('data-src') || (target as HTMLImageElement).src;
      if (src) {
        this.openLightbox(src);
      }
    }
  }

  openLightbox(src: string): void {
    this.lightboxImage.set(src);
  }

  closeLightbox(): void {
    this.lightboxImage.set(null);
  }

  constructor(title: Title, meta: Meta) {
    this.fetchPosts();
    const pageTitle = $localize`:@@home.pageTitle:JavaBlog.com - Coding Adventures with Claude AI`;
    const description = $localize`:@@home.metaDescription:A blog about coding with Claude AI, Angular, and modern web development.`;
    title.setTitle(pageTitle);
    const subPath = this.fullLocaleId === 'zh-TW' ? '/tw' : this.fullLocaleId === 'en' ? '' : `/${this.fullLocaleId.split('-')[0]}`;
    const ogUrl = `https://www.javablog.com${subPath}/`;
    meta.addTags([
      { name: 'description', content: description },
      { property: 'og:title', content: 'JavaBlog.com' },
      { property: 'og:description', content: description },
      { property: 'og:type', content: 'website' },
      { property: 'og:url', content: ogUrl },
      { property: 'og:image', content: 'https://www.javablog.com/og-image.png' },
      { name: 'twitter:card', content: 'summary_large_image' },
    ]);
    this.injectJsonLd(ogUrl, description);
  }

  ngOnDestroy(): void {
    if (this.jsonLdScript) {
      this.jsonLdScript.remove();
      this.jsonLdScript = null;
    }
  }

  private injectJsonLd(url: string, description: string): void {
    const jsonLd = [
      {
        '@context': 'https://schema.org',
        '@type': 'WebSite',
        'name': 'JavaBlog.com',
        'url': 'https://www.javablog.com/',
      },
      {
        '@context': 'https://schema.org',
        '@type': 'Blog',
        'name': 'JavaBlog.com',
        'url': url,
        'description': description,
        'author': { '@type': 'Person', 'name': 'Ben Simpson', 'url': 'https://www.javablog.com' },
        'publisher': { '@type': 'Organization', 'name': 'JavaBlog.com', 'url': 'https://www.javablog.com' },
      }
    ];
    this.jsonLdScript = this.document.createElement('script');
    this.jsonLdScript.type = 'application/ld+json';
    this.jsonLdScript.textContent = JSON.stringify(jsonLd);
    this.document.head.appendChild(this.jsonLdScript);
  }

  private fetchPosts(): void {
    const lang = Object.values(LanguageCode).includes(this.locale) ? this.locale : LanguageCode.En;
    this.postsService.listPosts(lang).subscribe({
      next: (posts) => {
        this.posts.set(posts);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Error loading posts');
        this.loading.set(false);
        console.error('Failed to fetch posts:', err);
      }
    });
  }

  confirmDelete(post: PostResponse): void {
    if (confirm(`Delete "${post.title}"? This cannot be undone.`)) {
      this.deletePost(post.id);
    }
  }

  private deletePost(postId: string): void {
    this.deleting.set(postId);
    this.postsService.deletePost(postId).subscribe({
      next: () => {
        this.posts.update(posts => posts.filter(p => p.id !== postId));
        this.deleting.set(null);
      },
      error: (err) => {
        this.deleting.set(null);
        alert('Failed to delete post');
        console.error('Failed to delete post:', err);
      }
    });
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    const locale = $localize`:@@locale.code:en-US`;
    return date.toLocaleDateString(locale, {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }

  getDateOnly(dateString: string): string {
    return dateString.split('T')[0];
  }
}
