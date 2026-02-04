import { RenderMode, ServerRoute } from '@angular/ssr';

export const serverRoutes: ServerRoute[] = [
  {
    path: '',
    renderMode: RenderMode.Prerender
  },
  {
    path: 'posts/:slug',
    renderMode: RenderMode.Client
  },
  {
    path: 'login',
    renderMode: RenderMode.Client
  },
  {
    path: 'logout',
    renderMode: RenderMode.Client
  },
  {
    path: 'author/**',
    renderMode: RenderMode.Client
  }
];
