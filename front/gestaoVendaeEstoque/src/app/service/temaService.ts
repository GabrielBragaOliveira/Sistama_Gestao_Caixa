import { Injectable, signal, Inject, effect } from '@angular/core';
import { DOCUMENT } from '@angular/common';

type Theme = 'light' | 'dark';

@Injectable({
  providedIn: 'root'
})
export class TemaService {

  theme = signal<Theme>('light');

  constructor(@Inject(DOCUMENT) private document: Document) {
    this.loadTheme();

    effect(() => {
      const newTheme = this.theme();
      localStorage.setItem('theme', newTheme);
      this.updateBodyClass(newTheme);
    });
  }

  private loadTheme() {
    const savedTheme = localStorage.getItem('theme') as Theme | null;
    const initialTheme = savedTheme || 'light';
    this.theme.set(initialTheme);
  }

  private updateBodyClass(theme: Theme) {
    const body = this.document.body;
    body.classList.remove('theme-light', 'theme-dark');

    if (theme === 'dark') {
      body.classList.add('theme-dark');
    } else {
      body.classList.add('theme-light');
    }
  }

  toggleTheme() {
    this.theme.update(current => (current === 'light' ? 'dark' : 'light'));
  }
}