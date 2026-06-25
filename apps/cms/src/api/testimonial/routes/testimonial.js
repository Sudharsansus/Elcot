'use strict';

// Strapi 5 core router for the Testimonial content type (REST at /api/testimonials).
const { factories } = require('@strapi/strapi');

module.exports = factories.createCoreRouter('api::testimonial.testimonial');
