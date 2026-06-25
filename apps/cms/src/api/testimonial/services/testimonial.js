'use strict';

// Strapi 5 core service for the Testimonial content type.
const { factories } = require('@strapi/strapi');

module.exports = factories.createCoreService('api::testimonial.testimonial');
