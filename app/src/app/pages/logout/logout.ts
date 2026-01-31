import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../auth';

@Component({
  selector: 'app-logout',
  template: `<p>Logging out...</p>`,
  styles: `:host { display: block; padding: var(--spacing-xl); color: var(--color-text-muted); }`
})
export class Logout implements OnInit {
  private authService = inject(AuthService);
  private router = inject(Router);

  ngOnInit(): void {
    if (this.authService.isAuthenticated) {
      this.authService.logout();
    } else {
      this.router.navigate(['/']);
    }
  }
}
