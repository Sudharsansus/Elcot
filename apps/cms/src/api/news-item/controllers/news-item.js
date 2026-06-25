'use strict';

// Strapi 5 core controller for the News content type.
const { factories } = require('@strapi/strapi');

module.exports = factories.createCoreController('api::news-item.news-item');
