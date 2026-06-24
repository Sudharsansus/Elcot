module.exports = ({ env }) => ({
  host: env('HOST', '0.0.0.0'),
  port: env.int('PORT', 1337),
  app: {
    keys: env.array('APP_KEYS', ['avgcxr-strapi-key-1', 'avgcxr-strapi-key-2']),
  },
  url: env('PUBLIC_URL', 'http://localhost:1337'),
  proxy: true,
  cron: {
    enabled: false,
  },
});
