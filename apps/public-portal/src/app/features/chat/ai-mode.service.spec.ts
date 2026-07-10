import { TestBed, fakeAsync, tick } from '@angular/core/testing';
import { signal } from '@angular/core';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { AiModeService } from './ai-mode.service';
import { ChatService } from './chat.service';
import { SmoothScrollService } from '../../core/services/smooth-scroll.service';
import { I18nService } from '../../core/services/i18n.service';
import { FormAssistService } from './form-assist.service';

describe('AiModeService', () => {
  let service: AiModeService;
  let messages: ReturnType<typeof signal<Array<{ id: string; role: string; content: string }>>>;
  let chat: {
    messages: typeof messages;
    push: jasmine.Spy;
    askBackend: jasmine.Spy;
  };
  let router: { url: string; events: ReturnType<typeof of>; navigateByUrl: jasmine.Spy };
  let smooth: { scrollTo: jasmine.Spy };
  let formAssist: { begin: jasmine.Spy };

  beforeEach(() => {
    messages = signal<Array<{ id: string; role: string; content: string }>>([]);
    chat = {
      messages,
      push: jasmine.createSpy('push').and.callFake((role: string, content: string) =>
        messages.update((m) => [...m, { id: 'm' + m.length, role, content }])),
      askBackend: jasmine.createSpy('askBackend').and.returnValue(of({ reply: null, action: null, sources: [], ok: true })),
    };
    router = { url: '/', events: of(), navigateByUrl: jasmine.createSpy('navigateByUrl').and.returnValue(Promise.resolve(true)) };
    smooth = { scrollTo: jasmine.createSpy('scrollTo') };
    formAssist = { begin: jasmine.createSpy('begin') };

    TestBed.configureTestingModule({
      providers: [
        AiModeService,
        { provide: ChatService, useValue: chat },
        { provide: Router, useValue: router },
        { provide: SmoothScrollService, useValue: smooth },
        { provide: I18nService, useValue: { currentLanguage: signal('en') } },
        { provide: FormAssistService, useValue: formAssist },
      ],
    });
    service = TestBed.inject(AiModeService);
  });

  // ---- allow-list validation (mirrors the server-side guard) ----
  describe('isValid()', () => {
    it('accepts navigate to an internal route', () => {
      expect(service.isValid({ tool: 'navigate', args: { route: '/schemes' } })).toBeTrue();
    });
    it('rejects navigate to an external/absolute URL', () => {
      expect(service.isValid({ tool: 'navigate', args: { route: 'https://evil.example' } })).toBeFalse();
    });
    it('accepts fillForm only for known forms', () => {
      expect(service.isValid({ tool: 'fillForm', args: { form: 'register' } })).toBeTrue();
      expect(service.isValid({ tool: 'fillForm', args: { form: 'contact' } })).toBeTrue();
      expect(service.isValid({ tool: 'fillForm', args: { form: 'admin' } })).toBeFalse();
    });
    it('accepts read-only tools', () => {
      expect(service.isValid({ tool: 'listSchemes' })).toBeTrue();
      expect(service.isValid({ tool: 'findScheme', args: { category: 'training' } })).toBeTrue();
      expect(service.isValid({ tool: 'openSchemeFinder' })).toBeTrue();
    });
    it('rejects unknown tools and null', () => {
      expect(service.isValid({ tool: 'deleteEverything' })).toBeFalse();
      expect(service.isValid({ tool: 'approveApplication', args: { id: '1' } })).toBeFalse();
      expect(service.isValid(null)).toBeFalse();
    });
  });

  // ---- per-tool dispatch ----
  describe('dispatch()', () => {
    it('navigate → routes internally', () => {
      service.dispatch({ tool: 'navigate', args: { route: '/talent' } });
      expect(router.navigateByUrl).toHaveBeenCalledWith('/talent');
    });
    it('ignores an invalid navigate (never routes off-site)', () => {
      service.dispatch({ tool: 'navigate', args: { route: 'https://evil.example' } });
      expect(router.navigateByUrl).not.toHaveBeenCalled();
    });
    it('ignores an unknown/disallowed tool entirely', () => {
      service.dispatch({ tool: 'approveApplication', args: { id: '1' } });
      expect(router.navigateByUrl).not.toHaveBeenCalled();
      expect(chat.push).not.toHaveBeenCalled();
    });
    it('listSchemes → renders a scheme list in the transcript', () => {
      service.dispatch({ tool: 'listSchemes' });
      expect(chat.push).toHaveBeenCalled();
      const [, content] = chat.push.calls.mostRecent().args;
      expect(content).toContain('•');
    });
    it('findScheme → renders schemes in the transcript', () => {
      service.dispatch({ tool: 'findScheme', args: { category: 'training' } });
      expect(chat.push).toHaveBeenCalled();
    });
    it('openSchemeFinder → scrolls to the finder', fakeAsync(() => {
      router.url = '/';
      service.dispatch({ tool: 'openSchemeFinder' });
      tick(500);
      expect(smooth.scrollTo).toHaveBeenCalledWith('#scheme-finder', -90);
    }));
    it('fillForm → opens the register form and starts the guided fill', fakeAsync(() => {
      router.url = '/auth/register';
      service.dispatch({ tool: 'fillForm', args: { form: 'register' } });
      tick(600);
      expect(formAssist.begin).toHaveBeenCalled();
    }));
  });

  // ---- confirmation guard ----
  describe('confirmation guard', () => {
    it('run() defers a state-changing action for confirmation (does NOT auto-execute)', fakeAsync(() => {
      service.run('take me to business connect');
      tick(50);
      expect(router.navigateByUrl).not.toHaveBeenCalled();
      const p = service.pending();
      expect(p).toBeTruthy();
      expect(p!.action.tool).toBe('navigate');
      expect(p!.action.args!['route']).toBe('/companies');
    }));

    it('confirm() executes the pending action', fakeAsync(() => {
      service.run('take me to business connect');
      tick(50);
      service.confirm();
      expect(router.navigateByUrl).toHaveBeenCalledWith('/companies');
      expect(service.pending()).toBeNull();
    }));

    it('cancel() discards the pending action without executing', fakeAsync(() => {
      service.run('take me to business connect');
      tick(50);
      service.cancel();
      expect(router.navigateByUrl).not.toHaveBeenCalled();
      expect(service.pending()).toBeNull();
    }));

    it('read-only actions run immediately (no confirmation needed)', fakeAsync(() => {
      service.run('list all schemes');
      tick(50);
      expect(service.pending()).toBeNull();
      const [, content] = chat.push.calls.mostRecent().args;
      expect(content).toContain('•');
    }));
  });

  // ---- graceful offline fallback ----
  it('flags AI Mode unavailable when the backend errors (local intent still works)', fakeAsync(() => {
    chat.askBackend.and.returnValue(of({ reply: null, action: null, sources: [], ok: false }));
    service.run('take me to talent connect');
    tick(50);
    expect(service.unavailable()).toBeTrue();
    // local intent still produced a state-changing action for confirmation
    expect(service.pending()?.action.tool).toBe('navigate');
  }));
});
