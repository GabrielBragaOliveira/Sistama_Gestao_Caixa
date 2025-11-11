import { Injectable, signal, Inject, effect } from '@angular/core';
import { DOCUMENT } from '@angular/common';

type Theme = 'light' | 'dark';

@Injectable({
  providedIn: 'root'
})

export class ThemeService {

    theme = signal<Theme>('light');

    constructor(@Inject(DOCUMENT) private document: Document) {
        this.loadTheme();
        effect(() => {
            const newTheme = this.theme();
            localStorage.setItem('theme', newTheme);
            this.switchLinkTag(newTheme);
        });
    }

  
    private loadTheme() {
        const savedTheme = localStorage.getItem('theme') as Theme | null;
        this.theme.set(savedTheme || 'light');
    }

    private switchLinkTag(theme: Theme) {
        const themeLink = this.document.getElementById('app-theme') as HTMLLinkElement;
        if (themeLink) {
        const newHref = `assets/themes/lara-${theme}/theme.css`;
        themeLink.href = newHref;
        }
    }


    toggleTheme() {
        this.theme.update(current => (current === 'light' ? 'dark' : 'light'));
    }
}