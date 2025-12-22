import {Component} from '@angular/core';
import {Meta, Title} from '@angular/platform-browser';

@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class Home {
  readonly post = {
    title: 'How I Built This Blog with Claude AI (And the Exact Prompts I Used)',
    slug: 'building-javablog',
    date: '2025-12-22',
    description: 'How I built this blog using Angular, Azure Static Web Apps, and Antropic Claude.'
  };

  constructor(title: Title, meta: Meta) {
    title.setTitle(`${this.post.title} - JavaBlog.com`);
    meta.addTags([
      {name: 'description', content: this.post.description},
      {property: 'og:title', content: this.post.title},
      {property: 'og:description', content: this.post.description},
      {property: 'og:type', content: 'article'},
      {name: 'twitter:card', content: 'summary'}
    ]);
  }
}
