'use strict';

// Strapi 5 core service for the FAQ content type.
const { factories } = require('@strapi/strapi');

module.exports = factories.createCoreService('api::faq.faq');
