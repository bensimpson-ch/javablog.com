import { Component, inject, OnInit } from '@angular/core';
import { Meta } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { AuthService } from '../../auth';

@Component({
  selector: 'app-login',
  template: '',
  styles: ''
})
export class Login implements OnInit {
  private authService = inject(AuthService);
  private router = inject(Router);

  constructor(meta: Meta) {
    meta.addTag({ name: 'robots', content: 'noindex' });
  }

  ngOnInit(): void {
    if (this.authService.isAuthenticated) {
      this.router.navigate(['/']);
    } else {
      this.authService.login();
    }
  }
}
