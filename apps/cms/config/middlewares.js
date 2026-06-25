'use strict';

// Strapi 5 middleware chain. Restores the full default load order (the prior
// Strapi 4 config listed only logger/errors/security, omitting body, query,
// session, favicon and public, which Strapi requires) and moves CORS to the
// dedicated `strapi::cors` middleware (in Strapi 5 CORS is configured here, not
// nested inside `strapi::security`).
module.exports = [
  {
    name: 'strapi::logger',
    config: { level: process.env.LOG_LEVEL || 'info', exposeInContext: false },
  },
  {
    name: 'strapi::errors',
    config: { shouldRedirectToAdmin: true },
  },
  'strapi::security',
  {
    name: 'strapi::cors',
    config: {
      enabled: true,
      headers: '*',
      origin: (
        process.env.CORS_ORIGINS ||
        'http://localhost:4200,http://localhost:4300,http://localhost:4400'
      ).split(','),
    },
  },
  'strapi::poweredBy',
  'strapi::query',
  'strapi::body',
  'strapi::session',
  'strapi::favicon',
  'strapi::public',
];
