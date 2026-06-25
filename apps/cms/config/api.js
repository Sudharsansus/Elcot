'use strict';

// Strapi 5 REST API config. `rest.withCount` (returns pagination counts) is the
// Strapi 5 default and is made explicit here; defaultLimit/maxLimit bound list
// responses.
module.exports = {
  rest: {
    defaultLimit: 25,
    maxLimit: 100,
    withCount: true,
  },
  responseTime: true,
};
