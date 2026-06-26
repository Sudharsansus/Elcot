import { HttpInterceptorFn, HttpResponse } from '@angular/common/http';
import { map } from 'rxjs';

/**
 * The API wraps every JSON response in an envelope:
 *   { success, data, message, error, timestamp }
 * Components and services expect the raw payload, so unwrap `{ success, data }`
 * → `data`. Responses without BOTH a `success` flag and a `data` field (e.g.
 * Strapi's `{ data, meta }`, blobs, plain bodies) pass through untouched.
 */
export const unwrapInterceptor: HttpInterceptorFn = (req, next) =>
  next(req).pipe(
    map((event) => {
      if (
        event instanceof HttpResponse &&
        event.body &&
        typeof event.body === 'object' &&
        'success' in event.body &&
        'data' in event.body
      ) {
        return event.clone({ body: (event.body as { data: unknown }).data });
      }
      return event;
    }),
  );
