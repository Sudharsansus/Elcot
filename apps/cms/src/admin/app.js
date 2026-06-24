// Admin customizations for AVGC-XR CMS
export default {
  config: {
    // Customize admin panel theme for Government of Tamil Nadu branding
    locales: ['en', 'ta'],
    tutorials: false,
  },
  bootstrap(app) {
    console.log('AVGC-XR CMS Admin Panel loaded');
  },
};