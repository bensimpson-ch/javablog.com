/// <reference types="@angular/localize" />

import { registerLocaleData } from '@angular/common';
import localeZhHant from '@angular/common/locales/zh-Hant';
import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';

registerLocaleData(localeZhHant, 'tw');

bootstrapApplication(App, appConfig)
  .catch((err) => console.error(err));
