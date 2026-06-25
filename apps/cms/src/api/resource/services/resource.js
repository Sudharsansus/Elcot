'use strict';

// Strapi 5 core service for the Resource content type.
const { factories } = require('@strapi/strapi');

module.exports = factories.createCoreService('api::resource.resource');
