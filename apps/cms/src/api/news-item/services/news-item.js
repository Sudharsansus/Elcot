'use strict';

// Strapi 5 core service for the News content type.
const { factories } = require('@strapi/strapi');

module.exports = factories.createCoreService('api::news-item.news-item');
