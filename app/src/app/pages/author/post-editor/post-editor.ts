import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { PostsService, CreatePostRequest, UpdatePostRequest } from '../../../api';

@Component({
  selector: 'app-post-editor',
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatSnackBarModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './post-editor.html',
  styleUrl: './post-editor.scss'
})
export class PostEditor implements OnInit {
  private fb = inject(FormBuilder);
  private postsService = inject(PostsService);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  postId = signal<string | null>(null);
  loading = signal(false);
  submitting = signal(false);

  form = this.fb.group({
    title: ['', [Validators.required, Validators.maxLength(200)]],
    slug: ['', [Validators.required, Validators.pattern(/^[a-z0-9-]+$/)]],
    content: ['', Validators.required]
  });

  get isEditMode(): boolean {
    return this.postId() !== null;
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('postId');
    if (id) {
      this.postId.set(id);
      this.loadPost(id);
    }
  }

  private loadPost(id: string): void {
    this.loading.set(true);
    this.postsService.getPost(id).subscribe({
      next: (post) => {
        this.form.patchValue({
          title: post.title,
          slug: post.slug,
          content: post.content
        });
        this.loading.set(false);
      },
      error: (err) => {
        this.loading.set(false);
        this.snackBar.open('Failed to load post', 'Dismiss', { duration: 5000 });
        console.error('Failed to load post:', err);
        this.router.navigate(['/author/posts/new']);
      }
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
      content: this.form.value.content!
    };

    this.postsService.createPost(request).subscribe({
      next: () => {
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
      content: this.form.value.content!
    };

    this.postsService.updatePost(this.postId()!, request).subscribe({
      next: () => {
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
}
