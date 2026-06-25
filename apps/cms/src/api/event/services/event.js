'use strict';

// Strapi 5 core service for the Event content type.
const { factories } = require('@strapi/strapi');

module.exports = factories.createCoreService('api::event.event');
