import { RenderMode, ServerRoute } from '@angular/ssr';

export const serverRoutes: ServerRoute[] = [
  {
    path: '',
    renderMode: RenderMode.Prerender,
  },
  {
    path: 'posts/:slug',
    renderMode: RenderMode.Prerender,
    async getPrerenderParams() {
      try {
        const res = await fetch('https://api.javablog.com/v1/posts?language=en');
        const posts: Array<{ slug: string }> = await res.json();
        return posts.map((p) => ({ slug: p.slug }));
      } catch {
        return [];
      }
    },
  },
  {
    path: 'login',
    renderMode: RenderMode.Client,
  },
  {
    path: 'logout',
    renderMode: RenderMode.Client,
  },
  {
    path: 'author/**',
    renderMode: RenderMode.Client,
  },
];
