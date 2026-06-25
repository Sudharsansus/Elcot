'use strict';

// Strapi 5 core router for the News content type.
// Exposes REST endpoints at /api/news-items (find, findOne) and the
// authenticated CRUD routes for content managers.
const { factories } = require('@strapi/strapi');

module.exports = factories.createCoreRouter('api::news-item.news-item');
