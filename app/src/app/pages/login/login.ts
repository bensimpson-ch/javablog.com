import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../auth';

@Component({
  selector: 'app-login',
  template: `<p>Redirecting to login...</p>`,
  styles: `:host { display: block; padding: var(--spacing-xl); color: var(--color-text-muted); }`
})
export class Login implements OnInit {
  private authService = inject(AuthService);
  private router = inject(Router);

  ngOnInit(): void {
    if (this.authService.isAuthenticated) {
      this.router.navigate(['/']);
    } else {
      this.authService.login();
    }
  }
}
