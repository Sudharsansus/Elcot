'use strict';

// Strapi 5 core router for the Resource content type (REST at /api/resources).
const { factories } = require('@strapi/strapi');

module.exports = factories.createCoreRouter('api::resource.resource');
