import { Component, HostListener, inject, signal } from '@angular/core';
import { Meta, Title } from '@angular/platform-browser';
import { RouterLink } from '@angular/router';
import { PostsService, PostResponse } from '../../api';
import { AuthService } from '../../auth';

@Component({
  selector: 'app-home',
  imports: [RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class Home {
  private postsService = inject(PostsService);
  protected authService = inject(AuthService);

  posts = signal<PostResponse[]>([]);
  lightboxImage = signal<string | null>(null);
  loading = signal<boolean>(true);
  error = signal<string | null>(null);
  deleting = signal<string | null>(null);

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
    title.setTitle('JavaBlog.com - Coding Adventures with Claude AI');
    meta.addTags([
      { name: 'description', content: 'A blog about coding with Claude AI, Angular, and modern web development.' },
      { property: 'og:title', content: 'JavaBlog.com' },
      { property: 'og:description', content: 'A blog about coding with Claude AI, Angular, and modern web development.' },
      { property: 'og:type', content: 'website' },
      { name: 'twitter:card', content: 'summary' }
    ]);
  }

  private fetchPosts(): void {
    this.postsService.listPosts().subscribe({
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
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }

  getDateOnly(dateString: string): string {
    return dateString.split('T')[0];
  }
}
