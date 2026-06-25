'use strict';

// Strapi 5 core router for the Event content type (REST at /api/events).
const { factories } = require('@strapi/strapi');

module.exports = factories.createCoreRouter('api::event.event');
