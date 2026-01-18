import {Component} from '@angular/core';
import {Meta, Title} from '@angular/platform-browser';

@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class Home {
  readonly posts = [
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
    title.setTitle('JavaBlog.com - Coding Adventures with Claude AI');
    meta.addTags([
      {name: 'description', content: 'A blog about coding with Claude AI, Angular, and modern web development.'},
      {property: 'og:title', content: 'JavaBlog.com'},
      {property: 'og:description', content: 'A blog about coding with Claude AI, Angular, and modern web development.'},
      {property: 'og:type', content: 'website'},
      {name: 'twitter:card', content: 'summary'}
    ]);
  }
}
