module.exports = [
  'strapi::logger',
  'strapi::errors',
  {
    name: 'strapi::security',
    config: {
      contentSecurityPolicy: {
        directives: {
          'default-src': ["'self'"],
          'img-src': ["'self'", 'data:', 'blob:', 'https://avgcxr.elcot.tn.gov.in'],
          'media-src': ["'self'", 'blob:', 'https://avgcxr.elcot.tn.gov.in'],
          'script-src': ["'self'", "'unsafe-inline'", "'unsafe-eval'"],
          'style-src': ["'self'", "'unsafe-inline'", 'https://fonts.googleapis.com'],
          'font-src': ["'self'", 'https://fonts.gstatic.com'],
          'connect-src': ["'self'", 'https://api.elcot.tn.gov.in'],
          'frame-ancestors': ["'none'"],
        },
      },
      xss: true,
      cors: {
        enabled: true,
        headers: ['Content-Type', 'Authorization', 'X-Request-ID', 'Accept-Language'],
        origin: ['http://localhost:4200', 'http://localhost:4300', 'http://localhost:4400',
                 'https://avgcxr.elcot.tn.gov.in', 'https://api.avgcxr.elcot.tn.gov.in'],
      },
      p3p: true,
      hsts: true,
      xfo: true,
    },
  },
  {
    name: 'strapi::cors',
    config: {
      enabled: true,
      headers: ['Content-Type', 'Authorization', 'X-Request-ID', 'Accept-Language'],
      origin: ['http://localhost:4200', 'http://localhost:4300', 'http://localhost:4400',
               'https://avgcxr.elcot.tn.gov.in'],
    },
  },
  'strapi::poweredBy',
  'strapi::query',
  'strapi::body',
  'strapi::session',
  'strapi::favicon',
  'strapi::public',
];
