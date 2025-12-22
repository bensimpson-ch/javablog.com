# Claude Instructions for javablog.com

Angular v20 blog with SSG (Static Site Generation) hosted on Azure Static Web Apps.

## Post Preparation Protocol

All posts are authored by Ben.

### Content Guidelines

- Author: Ben (final say on all content)
- Tone: Technical but accessible, conversational
- Length: Concise, practical examples over theory
- Code: Working snippets, not pseudocode

### Post Frontmatter (in component)

Each post component stores metadata:

```typescript
readonly
post = {
  title: 'Post Title',
  slug: 'post-slug',
  date: '2025-01-15',
  description: 'Brief description for SEO and listings'
};
```

## Directory Structure

```
javablog.com/
├── app/                      # Angular application
│   ├── src/
│   │   ├── app/
│   │   │   ├── pages/        # Route components (blog posts, about, etc.)
│   │   │   ├── components/   # Shared UI components
│   │   │   ├── services/     # Data services
│   │   │   └── models/       # TypeScript interfaces
│   │   ├── assets/           # Static assets (images, etc.)
│   │   └── styles.scss       # Global styles
│   └── dist/app/browser/     # Build output (deployed to Azure)
└── .github/workflows/        # CI/CD pipeline
```

## SEO Requirements

Every page component MUST include:

### Meta Tags (via Angular Meta service)

```typescript
import {Meta, Title} from '@angular/platform-browser';

constructor(private
meta: Meta, private
title: Title
)
{
  this.title.setTitle('Post Title - javablog.com');
  this.meta.addTags([
    {name: 'description', content: 'Post description for search results'},
    {property: 'og:title', content: 'Post Title'},
    {property: 'og:description', content: 'Post description'},
    {property: 'og:type', content: 'article'},
    {name: 'twitter:card', content: 'summary'}
  ]);
}
```

### Semantic HTML

- Use `<article>`, `<header>`, `<main>`, `<nav>`, `<footer>`
- Proper heading hierarchy (h1 > h2 > h3)
- `<time datetime="YYYY-MM-DD">` for dates

### Structured Data (JSON-LD)

Include in page template for blog posts:

```html

<script type="application/ld+json">
  {
    "@context": "https://schema.org",
    "@type": "BlogPosting",
    "headline": "Post Title",
    "datePublished": "2025-01-15",
    "author": { "@type": "Person", "name": "Author Name" }
  }
</script>
```

## Prerendering

Routes are configured in `app.routes.server.ts` with `RenderMode.Prerender`.
All blog post routes must be discoverable at build time.

## Creating Blog Posts

1. Create route in `app.routes.ts`
2. Create page component in `src/app/pages/`
3. Add meta tags and structured data
4. Ensure route is prerendered

## Build & Deploy

- Push to `main` triggers GitHub Actions
- Angular builds with prerendering
- Static HTML deployed to Azure Static Web Apps

## Commands

```bash
cd app
npm run build      # Production build with prerendering
npm run serve      # Dev server (SSR mode)
```

## Do NOT

- Skip meta tags on any page
- Use client-only rendering for content pages
- Add tracking/analytics without explicit request
- Over-complicate the component structure
