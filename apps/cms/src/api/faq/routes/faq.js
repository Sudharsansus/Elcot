'use strict';

// Strapi 5 core router for the FAQ content type (REST at /api/faqs).
const { factories } = require('@strapi/strapi');

module.exports = factories.createCoreRouter('api::faq.faq');
