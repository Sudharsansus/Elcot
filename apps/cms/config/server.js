'use strict';

// Strapi 5 server config. `app.keys` must be an ARRAY of keys; the prior code
// comma-joined four copies of a single env value into a string, which is not the
// expected shape. APP_KEYS is a comma-separated list.
module.exports = {
  host: process.env.HOST || '0.0.0.0',
  port: parseInt(process.env.PORT || '1337', 10),
  app: {
    keys: (
      process.env.APP_KEYS ||
      'avgc-xr-cms-key-1-change-in-prod,avgc-xr-cms-key-2-change-in-prod'
    ).split(','),
  },
};
