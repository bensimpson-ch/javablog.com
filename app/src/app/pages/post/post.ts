import { DOCUMENT } from '@angular/common';
import { Component, HostListener, inject, LOCALE_ID, OnDestroy, OnInit, signal } from '@angular/core';
import { Meta, Title } from '@angular/platform-browser';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { LanguageCode, PostsService, CommentsService, PostResponse, CommentResponse, CreateCommentRequest } from '../../api';
import { AuthService } from '../../auth';

@Component({
  selector: 'app-post',
  imports: [RouterLink, FormsModule],
  templateUrl: './post.html',
  styleUrl: './post.scss'
})
export class Post implements OnInit, OnDestroy {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private postsService = inject(PostsService);
  private commentsService = inject(CommentsService);
  private document = inject(DOCUMENT);
  protected authService = inject(AuthService);
  private locale = inject(LOCALE_ID).split('-')[0] as LanguageCode;

  post = signal<PostResponse | null>(null);
  comments = signal<CommentResponse[]>([]);
  lightboxImage = signal<string | null>(null);
  loading = signal<boolean>(true);
  error = signal<string | null>(null);
  deleting = signal<boolean>(false);

  // Comment form
  commentAuthor = '';
  commentContent = '';
  submittingComment = signal<boolean>(false);
  deletingComment = signal<string | null>(null);

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

  constructor(private title: Title, private meta: Meta) {}

  ngOnInit(): void {
    const slug = this.route.snapshot.paramMap.get('slug');
    if (slug) {
      this.fetchPost(slug);
    } else {
      this.error.set('Post not found');
      this.loading.set(false);
    }
  }

  ngOnDestroy(): void {
    if (this.jsonLdScript) {
      this.jsonLdScript.remove();
      this.jsonLdScript = null;
    }
  }

  private fetchPost(slug: string): void {
    const lang = Object.values(LanguageCode).includes(this.locale) ? this.locale : LanguageCode.En;
    this.postsService.getPostBySlug(slug, lang).subscribe({
      next: (post) => {
        this.post.set(post);
        this.loading.set(false);
        this.setMetaTags(post);
        this.injectJsonLd(post);
        this.fetchComments(post.id);
      },
      error: (err) => {
        this.error.set('Post not found');
        this.loading.set(false);
        console.error('Failed to fetch post:', err);
      }
    });
  }

  private fetchComments(postId: string): void {
    this.commentsService.listComments(postId).subscribe({
      next: (comments) => {
        this.comments.set(comments);
      },
      error: (err) => {
        console.error('Failed to fetch comments:', err);
      }
    });
  }

  private setMetaTags(post: PostResponse): void {
    this.title.setTitle(`${post.title} - JavaBlog.com`);
    this.meta.updateTag({ name: 'description', content: post.summary });
    this.meta.updateTag({ property: 'og:title', content: post.title });
    this.meta.updateTag({ property: 'og:description', content: post.summary });
    this.meta.updateTag({ property: 'og:type', content: 'article' });
    this.meta.updateTag({ name: 'twitter:card', content: 'summary' });
  }

  private injectJsonLd(post: PostResponse): void {
    const language = $localize`:@@locale.code:en-US`;
    const jsonLd = {
      '@context': 'https://schema.org',
      '@type': 'BlogPosting',
      'headline': post.title,
      'datePublished': this.getDateOnly(post.createdAt),
      'inLanguage': language,
      'author': { '@type': 'Person', 'name': 'Ben Simpson' }
    };
    this.jsonLdScript = this.document.createElement('script');
    this.jsonLdScript.type = 'application/ld+json';
    this.jsonLdScript.textContent = JSON.stringify(jsonLd);
    this.document.head.appendChild(this.jsonLdScript);
  }

  submitComment(): void {
    if (!this.commentAuthor.trim() || !this.commentContent.trim() || this.submittingComment()) {
      return;
    }

    const post = this.post();
    if (!post) return;

    this.submittingComment.set(true);

    const request: CreateCommentRequest = {
      author: this.commentAuthor.trim(),
      content: this.commentContent.trim()
    };

    this.commentsService.createComment(post.id, request).subscribe({
      next: (comment) => {
        this.comments.update(comments => [comment, ...comments]);
        this.commentAuthor = '';
        this.commentContent = '';
        this.submittingComment.set(false);
      },
      error: (err) => {
        this.submittingComment.set(false);
        alert('Failed to submit comment');
        console.error('Failed to submit comment:', err);
      }
    });
  }

  confirmDelete(): void {
    const post = this.post();
    if (!post) return;

    if (confirm(`Delete "${post.title}"? This cannot be undone.`)) {
      this.deletePost(post.id);
    }
  }

  private deletePost(postId: string): void {
    this.deleting.set(true);
    this.postsService.deletePost(postId).subscribe({
      next: () => {
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.deleting.set(false);
        alert('Failed to delete post');
        console.error('Failed to delete post:', err);
      }
    });
  }

  confirmDeleteComment(comment: CommentResponse): void {
    if (confirm(`Delete comment by "${comment.author}"?`)) {
      this.deleteComment(comment);
    }
  }

  private deleteComment(comment: CommentResponse): void {
    const post = this.post();
    if (!post) return;

    this.deletingComment.set(comment.id);
    this.commentsService.deleteComment(post.id, comment.id).subscribe({
      next: () => {
        this.comments.update(comments => comments.filter(c => c.id !== comment.id));
        this.deletingComment.set(null);
      },
      error: (err) => {
        this.deletingComment.set(null);
        alert('Failed to delete comment');
        console.error('Failed to delete comment:', err);
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
