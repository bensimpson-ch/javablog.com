# Claude Instructions for javablog.com

**When Ben asks a question, answer the question. Do not change code. Only change code when explicitly asked to make a change.**

Angular v20 blog hosted on Azure Static Web Apps.

## Post Preparation Protocol

All posts are authored by Ben.

### Content Guidelines

- Author: Ben (final say on all content)
- Do NOT write post body content - Ben writes all content himself
- Only create the post structure: title, slug, date, description in the posts array, and a placeholder article section
- Tone: Technical but accessible, conversational
- Length: Concise, practical examples over theory
- Code: Working snippets, not pseudocode

### Post Front matter (in component)

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
├── app/                          # Angular application
│   ├── src/
│   │   ├── app/
│   │   │   ├── pages/            # Route components (blog posts, about, etc.)
│   │   │   ├── components/       # Shared UI components
│   │   │   ├── services/         # Data services
│   │   │   └── models/           # TypeScript interfaces
│   │   ├── assets/               # Static assets (images, etc.)
│   │   └── styles.scss           # Global styles
│   └── dist/app/browser/         # Build output (deployed to Azure)
├── services/                     # Java backend (hexagonal architecture)
│   ├── domain/                   # Pure Java domain layer (no framework deps)
│   │   └── com.javablog.domain/
│   │       ├── (root)            # Shared value objects (Title, Slug, Author, etc.)
│   │       ├── blog/             # Blog post aggregate (Post, Comment, translations)
│   │       └── article/          # Article aggregate (Article, translations)
│   ├── application/              # Application services (use cases)
│   ├── api/                      # OpenAPI spec (generates DTOs)
│   ├── adapter-rest/             # REST endpoints
│   ├── adapter-persistence/      # JPA entities and repositories
│   ├── adapter-kafka/            # Kafka message adapters
│   └── bootstrap/                # Spring Boot application
└── .github/workflows/            # CI/CD pipeline
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

## Creating Blog Posts

1. Create route in `app.routes.ts`
2. Create page component in `src/app/pages/`
3. Add meta tags and structured data
4. **Update `app/public/sitemap.xml`** with the new URL

## Sitemap Maintenance

**IMPORTANT:** When site structure changes (new pages, routes, or posts), automatically update `app/public/sitemap.xml`.

Each URL entry follows this format:
```xml
<url>
  <loc>https://javablog.com/path</loc>
  <lastmod>YYYY-MM-DD</lastmod>
  <changefreq>weekly</changefreq>
  <priority>0.8</priority>
</url>
```

Priority guidelines:
- Homepage `/`: 1.0
- Blog posts: 0.8
- Static pages (about, etc.): 0.6

## Build & Deploy

- Push to `main` triggers GitHub Actions
- Angular builds and deploys to Azure Static Web Apps

## Commands

```bash
cd app
npm run build      # Production build
npm run serve      # Dev server
```

## Java Domain Objects

Domain objects are pure Java records with Guard-based validation. See `services/domain/CLAUDE.md` for detailed conventions.

### Guard Pattern

Validation uses the `Guard` class which throws `ConstraintViolationException` on failure:

```java
public record Title(String value) {

    public Title {
        Guard.againstBlank(value, "Title.value");
    }
}
```

### Available Guards

- `Guard.againstNull(Object, String)` - must not be null
- `Guard.againstEmpty(String, String)` - must not be null or empty
- `Guard.againstEmpty(Collection<?>, String)` - collection must not be null or empty
- `Guard.againstBlank(String, String)` - must not be null, empty, or whitespace
- `Guard.againstInvalidMaxLength(String, String, int)` - must not exceed max length

### Everything is an Object

Each domain concept gets its own record (Title, Slug, Content, Author, Summary, etc.). Identifiers get their own records with `generate()` factory methods (PostId, ArticleId, CommentId, TranslationJobId). Collections use plural wrapper records (Posts, Articles, Comments, Languages, TranslatedPosts, TranslatedArticles).

## Working Agreement (Java/Maven Services)

Claude writes code; Ben executes Maven commands. Do not run `mvn` commands directly. When verification is needed, ask Ben to run a specific command (e.g., "Please run `mvn compile` in the services directory").

## Do NOT

- Skip meta tags on any page
- Add tracking/analytics without explicit request
- Over-complicate the component structure
- State the obvious (e.g., "you'll need to rebuild and redeploy", "you need PostgreSQL running to test locally")
- Execute Maven commands (`mvn compile`, `mvn test`, etc.) - ask Ben to run them instead
- Waste tokens on obvious setup/run instructions - Ben knows his own dev environment
