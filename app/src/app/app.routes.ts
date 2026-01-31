import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { Post } from './pages/post/post';
import { Login } from './pages/login/login';
import { Logout } from './pages/logout/logout';
import { PostEditor } from './pages/author/post-editor/post-editor';
import { authGuard } from './auth';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'posts/:slug', component: Post },
  { path: 'login', component: Login },
  { path: 'logout', component: Logout },
  { path: 'author/posts/new', component: PostEditor, canActivate: [authGuard] },
  { path: 'author/posts/:postId/edit', component: PostEditor, canActivate: [authGuard] },
];
