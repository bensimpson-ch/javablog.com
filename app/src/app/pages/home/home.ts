import {Component, inject, signal} from '@angular/core';
import {Meta, Title} from '@angular/platform-browser';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class Home {
  private http = inject(HttpClient);
  helloResponse = signal<string>('Loading...');
  lightboxImage = signal<string | null>(null);

  openLightbox(src: string): void {
    this.lightboxImage.set(src);
  }

  closeLightbox(): void {
    this.lightboxImage.set(null);
  }

  readonly posts = [
    {
      title: 'Domain Object Validation - Jakarta Validation Annotations in the domain vs. Clean Architecture',
      slug: 'domain-object-validation',
      date: '2025-01-30',
      description: 'Internal debate about the role of Jakarta Validation Annotations in the domain. ' +
        'Using the "everything is an object" philosophy where Title, Slug, Content, and Author each have their own record.'
    },
    {
      title: 'Implementing the hexagonal architecture, first pain point, Claude\'s cutoff date',
      slug: 'flyway-spring-boot-4-cutoff',
      date: '2025-01-29',
      description: 'In Spring Boot 4, dependencies like Flyaway are configured differently. ' +
        'Auto-configuration works without additional properties. ' +
        'If your Coding Assistant didn\'t configure it correctly, you will learn why here.'
    },
    {
      title: 'Hexagonal Architecture with Spring Boot, Flyway, and Dual Database Support',
      slug: 'hexagonal-architecture-springboot',
      date: '2025-01-28',
      description: 'Converting a monolithic Spring Boot application to multi-module hexagonal architecture with domain, application, adapter-rest, adapter-persistence, and bootstrap modules. Adding Flyway migrations and PostgreSQL for production with H2 for tests.'
    },
    {
      title: 'Claude Prompts to create and deploy Dockerized Springboot Backend',
      slug: 'springboot-docker-with-claude',
      date: '2025-01-27',
      description: 'Connecting the Angular frontend to the Java API backend.  Deployed via Github Action to create Dockerized container and then use SSH to copy container and deploy to api.javablog.com'
    },
    {
      title: 'Setting Up SEO with Claude, first steps',
      slug: 'seo-with-claude',
      date: '2025-01-18',
      description: 'First steps setting up SEO for javablog.com with Claude AI assistance.'
    },
    {
      title: 'How I Built This Blog with Claude AI (And the Exact Prompts I Used)',
      slug: 'building-javablog',
      date: '2025-12-22',
      description: 'How I built this blog using Angular, Azure Static Web Apps, and Anthropic Claude.'
    }
  ];

  constructor(title: Title, meta: Meta) {
    this.fetchHello();
    title.setTitle('JavaBlog.com - Coding Adventures with Claude AI');
    meta.addTags([
      {name: 'description', content: 'A blog about coding with Claude AI, Angular, and modern web development.'},
      {property: 'og:title', content: 'JavaBlog.com'},
      {property: 'og:description', content: 'A blog about coding with Claude AI, Angular, and modern web development.'},
      {property: 'og:type', content: 'website'},
      {name: 'twitter:card', content: 'summary'}
    ]);
  }

  private fetchHello(): void {
    this.http.get('https://api.javablog.com/hello', {responseType: 'text'}).subscribe({
      next: (response) => this.helloResponse.set(response),
      error: () => this.helloResponse.set('Error fetching from API')
    });
  }
}
