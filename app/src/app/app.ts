import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { ThemeService } from './services/theme.service';
import { AuthService } from './auth';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit {
  protected themeService = inject(ThemeService);
  protected authService = inject(AuthService);
  private router = inject(Router);

  async ngOnInit(): Promise<void> {
    const hasCode = window.location.search.includes('code=');
    const loggedIn = await this.authService.tryLogin();

    if (loggedIn && hasCode) {
      this.router.navigate(['/'], { replaceUrl: true });
    }
  }
}
