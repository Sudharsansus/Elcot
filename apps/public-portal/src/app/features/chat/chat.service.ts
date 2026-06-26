import { Injectable, inject, signal, computed } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of, tap, catchError, map, timeout } from 'rxjs';
import { environment } from '../../../environments/environment';

/**
 * Chat message from the AI assistant (bilingual).
 */
export interface ChatMessage {
  id: string;
  role: 'USER' | 'ASSISTANT' | 'SYSTEM';
  content: string;
  language: 'en' | 'ta';
  modelUsed?: string;
  tokensUsed?: number;
  ragContextIds?: string[];
  createdAt: string;
  /** True while the assistant is still typing (set by client during streaming) */
  pending?: boolean;
}

export interface ChatTurnResponse {
  sessionId: string;
  sessionToken: string;
  language: 'en' | 'ta';
  userMessage: ChatMessage;
  assistantMessage: ChatMessage;
  ragSourceIds: string[];
  modelUsed: string;
  tokensUsed: number;
  totalLatencyMs: number;
}

export interface SendMessageRequest {
  message: string;
  sessionToken?: string;
  language?: 'en' | 'ta';
}

@Injectable({ providedIn: 'root' })
export class ChatService {
  private readonly http = inject(HttpClient);
  private readonly API_BASE = `${environment.apiUrl}/chat`;

  /** Persisted in localStorage; survives page reloads */
  private readonly STORAGE_KEY = 'avgcxr-chat-session';

  /** Reactive state for the widget */
  readonly sessionToken = signal<string | null>(this.loadSessionToken());
  readonly language = signal<'en' | 'ta'>(this.detectInitialLang());
  readonly messages = signal<ChatMessage[]>([]);
  readonly isOpen = signal<boolean>(false);
  readonly isLoading = signal<boolean>(false);
  readonly lastError = signal<string | null>(null);

  readonly canSend = computed(() => !this.isLoading());

  /** Open/close the chat panel */
  toggle(): void { this.isOpen.update(v => !v); }
  open(): void { this.isOpen.set(true); }
  close(): void { this.isOpen.set(false); }

  /** Switch language mid-conversation (also forces a hint to backend) */
  setLanguage(lang: 'en' | 'ta'): void {
    this.language.set(lang);
  }

  /**
   * Send a message and append both the user message and the assistant reply.
   */
  sendMessage(text: string): Observable<ChatTurnResponse> {
    const trimmed = text.trim();
    if (!trimmed) return of({} as ChatTurnResponse);

    // 1) Optimistically append the user message
    const userMsg: ChatMessage = {
      id: `local-${Date.now()}`,
      role: 'USER',
      content: trimmed,
      language: this.language(),
      createdAt: new Date().toISOString(),
    };
    this.messages.update(msgs => [...msgs, userMsg]);
    this.isLoading.set(true);
    this.lastError.set(null);

    // 2) Make the request
    const req: SendMessageRequest = {
      message: trimmed,
      sessionToken: this.sessionToken() ?? undefined,
      language: this.language(),
    };

    return this.http.post<ChatTurnResponse>(`${this.API_BASE}/send`, req).pipe(
      tap(resp => {
        // Persist session token for next visit
        if (resp.sessionToken && !this.sessionToken()) {
          this.sessionToken.set(resp.sessionToken);
          this.saveSessionToken(resp.sessionToken);
        }
        // Replace optimistic user msg + append assistant reply
        this.messages.update(msgs => [
          ...msgs.filter(m => m.id !== userMsg.id),
          resp.userMessage,
          resp.assistantMessage,
        ]);
        this.isLoading.set(false);
      }),
      catchError(err => {
        this.lastError.set(err?.error?.message || 'Chat service is unavailable. Please try again.');
        this.isLoading.set(false);
        // Remove the optimistic user message
        this.messages.update(msgs => msgs.filter(m => m.id !== userMsg.id));
        return of({} as ChatTurnResponse);
      })
    );
  }

  /** Lightweight ask: returns just the assistant reply text (or null on any
   *  failure/timeout). Does NOT mutate the message list — Mira owns that. */
  askBackend(text: string): Observable<string | null> {
    const headers = new HttpHeaders({ 'X-Silent-Error': '1' });
    const req: SendMessageRequest = {
      message: text,
      sessionToken: this.sessionToken() ?? undefined,
      language: this.language(),
    };
    return this.http.post<ChatTurnResponse>(`${this.API_BASE}/send`, req, { headers }).pipe(
      timeout(6000),
      tap(resp => {
        if (resp?.sessionToken && !this.sessionToken()) {
          this.sessionToken.set(resp.sessionToken);
          this.saveSessionToken(resp.sessionToken);
        }
      }),
      map(resp => resp?.assistantMessage?.content ?? null),
      catchError(() => of(null)),
    );
  }

  /** Append a locally-composed message (Mira's agentic actions / KB answers). */
  push(role: ChatMessage['role'], content: string): void {
    this.messages.update(msgs => [...msgs, {
      id: `${role.toLowerCase()}-${Date.now()}-${Math.random().toString(36).slice(2, 7)}`,
      role, content, language: this.language(), createdAt: new Date().toISOString(),
    }]);
  }

  /** Load message history for a session. */
  loadHistory(token: string): Observable<ChatMessage[]> {
    return this.http.get<{ data: { recentMessages: ChatMessage[] } }>(`${this.API_BASE}/sessions/${token}`).pipe(
      map(resp => resp.data?.recentMessages ?? []),
      tap(msgs => this.messages.set(msgs)),
      catchError(() => of([]))
    );
  }

  /** Clear local chat state + delete server session. */
  clear(): void {
    const token = this.sessionToken();
    this.messages.set([]);
    this.sessionToken.set(null);
    localStorage.removeItem(this.STORAGE_KEY);
    if (token) {
      this.http.delete(`${this.API_BASE}/sessions/${token}`).subscribe({
        error: () => {/* best-effort */},
      });
    }
  }

  // ---- persistence helpers ----

  private loadSessionToken(): string | null {
    if (typeof localStorage === 'undefined') return null;
    return localStorage.getItem(this.STORAGE_KEY);
  }

  private saveSessionToken(token: string): void {
    if (typeof localStorage !== 'undefined') {
      localStorage.setItem(this.STORAGE_KEY, token);
    }
  }

  private detectInitialLang(): 'en' | 'ta' {
    if (typeof navigator === 'undefined') return 'en';
    const nav = (navigator.language || 'en').toLowerCase();
    return nav.startsWith('ta') ? 'ta' : 'en';
  }
}
