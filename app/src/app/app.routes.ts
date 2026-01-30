import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { PostEditor } from './pages/author/post-editor/post-editor';
import { authGuard } from './auth';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'author/posts/new', component: PostEditor, canActivate: [authGuard] },
  { path: 'author/posts/:postId/edit', component: PostEditor, canActivate: [authGuard] },
];
