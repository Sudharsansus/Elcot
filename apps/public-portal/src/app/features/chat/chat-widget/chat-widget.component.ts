import { Component, inject, signal, computed, ViewChild, ElementRef, AfterViewChecked } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChatService } from '../chat.service';

/**
 * Floating chat widget for the AVGC-XR Portal AI assistant (Mira).
 *
 * <p>Bilingual (English + Tamil). Renders a launcher button in the bottom-right
 * corner; clicking it opens a chat panel with message history, language toggle,
 * and an input box. Connects to the backend at /api/v1/chat.</p>
 */
@Component({
  selector: 'avgcxr-chat-widget',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chat-widget.component.html',
  styleUrl: './chat-widget.component.scss',
})
export class ChatWidgetComponent implements AfterViewChecked {
  readonly chat = inject(ChatService);

  @ViewChild('scrollContainer') private scrollContainer?: ElementRef<HTMLElement>;
  private shouldScrollDown = true;

  readonly userInput = signal<string>('');

  readonly placeholderText = computed(() =>
    this.chat.language() === 'ta'
      ? 'ஏதாவது கேளுங்கள்... (Ask anything...)'
      : 'Ask about schemes, applications, status...'
  );

  readonly headerTitle = computed(() =>
    this.chat.language() === 'ta' ? 'மிரா - AI உதவியாளர்' : 'Mira -- AI Assistant'
  );

  readonly headerSubtitle = computed(() =>
    this.chat.language() === 'ta'
      ? 'தமிழ் நாடு AVGC-XR Portal'
      : 'Tamil Nadu AVGC-XR Portal'
  );

  readonly sendButtonText = computed(() =>
    this.chat.language() === 'ta' ? 'அனுப்பு' : 'Send'
  );

  readonly emptyStateText = computed(() =>
    this.chat.language() === 'ta'
      ? 'வணக்கம்! நான் மிரா. திட்டங்கள், விண்ணப்பங்கள், தகுதி பற்றி கேளுங்கள்.'
      : "Hi! I'm Mira. Ask me about schemes, applications, eligibility -- or write in Tamil."
  );

  readonly suggestedQuestions = [
    { en: 'What schemes are available?', ta: 'என்ன திட்டங்கள் உள்ளன?' },
    { en: 'How do I apply?',           ta: 'எப்படி விண்ணப்பிப்பது?' },
    { en: 'Eligibility criteria?',     ta: 'தகுதி நிபந்தனைகள்?' },
    { en: 'Required documents?',      ta: 'தேவையான ஆவணங்கள்?' }
  ];

  ngAfterViewChecked(): void {
    if (this.shouldScrollDown && this.scrollContainer) {
      const el = this.scrollContainer.nativeElement;
      el.scrollTop = el.scrollHeight;
      this.shouldScrollDown = false;
    }
  }

  send(): void {
    const text = this.userInput();
    if (!text.trim()) return;
    this.userInput.set('');
    this.shouldScrollDown = true;
    this.chat.sendMessage(text).subscribe();
  }

  sendSuggestion(text: string): void {
    this.userInput.set(text);
    this.send();
  }

  onKeydown(ev: KeyboardEvent): void {
    if (ev.key === 'Enter' && !ev.shiftKey) {
      ev.preventDefault();
      this.send();
    }
  }

  toggleLanguage(): void {
    this.chat.setLanguage(this.chat.language() === 'en' ? 'ta' : 'en');
  }

  trackByIdx(i: number): number { return i; }
}
