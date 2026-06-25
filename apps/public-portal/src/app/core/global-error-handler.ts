import { ErrorHandler, Injectable } from '@angular/core';

/**
 * Application-wide safety net. An unhandled error in one component must never
 * blank the whole portal for a citizen — we swallow it here (logged for
 * diagnostics) so the rest of the app keeps running. In production this is the
 * single place to forward errors to an observability sink (e.g. Sentry / OTLP).
 *
 * Chunk-load failures (stale deploy + long-lived tab) are recovered by a
 * one-time hard reload so users always land on the current build.
 */
@Injectable()
export class GlobalErrorHandler implements ErrorHandler {
  private static readonly RELOAD_FLAG = 'avgcxr-chunk-reloaded';

  handleError(error: unknown): void {
    const message = (error as { message?: string })?.message ?? String(error);

    const isChunkError = /ChunkLoadError|Loading chunk [\d]+ failed|dynamically imported module/i.test(message);
    if (isChunkError && typeof window !== 'undefined') {
      // Avoid an infinite reload loop: only auto-reload once per stale session.
      if (!sessionStorage.getItem(GlobalErrorHandler.RELOAD_FLAG)) {
        sessionStorage.setItem(GlobalErrorHandler.RELOAD_FLAG, '1');
        window.location.reload();
        return;
      }
    }

    // eslint-disable-next-line no-console
    console.error('[AVGCXR] Unhandled error:', error);
  }
}
