module.exports = ({ env }) => ({
  auth: {
    secret: env('ADMIN_JWT_SECRET', 'avgcxr-strapi-admin-jwt-secret-change-in-production'),
  },
  apiToken: {
    salt: env('API_TOKEN_SALT', 'avgcxr-strapi-api-token-salt-change-me'),
  },
  transfer: {
    token: {
      salt: env('TRANSFER_TOKEN_SALT', 'avgcxr-strapi-transfer-salt-change-me'),
    },
  },
  flags: {
    nps: env.bool('FLAG_NPS', true),
    promoteEE: env.bool('FLAG_PROMOTE_EE', true),
  },
});
