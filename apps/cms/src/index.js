'use strict';

module.exports = {
  register({ strapi }) {
    /* Register custom logic here. */
  },

  /**
   * Bootstrap: idempotently seed starter content so the admin panel is not empty
   * on first boot. Each content type is seeded only if it currently has no
   * entries, so re-running is safe. Seeding is wrapped per-type so a failure
   * never blocks Strapi startup.
   */
  async bootstrap({ strapi }) {
    await seedContent(strapi);
  },
};

const SEEDS = {
  'api::news-item.news-item': [
    {
      title: 'Tamil Nadu AVGC-XR Policy Portal goes live',
      slug: 'avgc-xr-portal-go-live',
      excerpt: 'The single-window portal for Animation, VFX, Gaming, Comics and XR is now open.',
      body: 'The Government of Tamil Nadu, through ELCOT, has launched the AVGC-XR portal to support the ecosystem with schemes, incentives and talent connect.',
      category: 'announcement',
      publishedDate: new Date().toISOString(),
      featured: true,
    },
  ],
  'api::event.event': [
    {
      title: 'AVGC-XR Industry Connect 2026',
      slug: 'avgc-xr-industry-connect-2026',
      description: 'A networking event for studios, startups, investors and academia.',
      eventDate: new Date(Date.now() + 30 * 24 * 60 * 60 * 1000).toISOString(),
      location: 'Chennai',
    },
  ],
  'api::faq.faq': [
    {
      question: 'Who can apply for AVGC-XR incentives?',
      answer: 'AVGC-XR companies, startups, MSMEs, freelancers and academic institutions registered in Tamil Nadu may apply, subject to scheme-specific eligibility.',
      category: 'eligibility',
      order: 1,
    },
  ],
  'api::testimonial.testimonial': [
    {
      name: 'Sample Studio',
      role: 'Founder',
      organization: 'AVGC-XR Startup',
      quote: 'The portal made it simple to discover schemes and apply in one place.',
      rating: 5,
    },
  ],
  // 'api::resource.resource' is intentionally NOT seeded: its `file` field is
  // required and seeding media needs an upload. Add resources via the admin UI.
};

async function seedContent(strapi) {
  for (const [uid, entries] of Object.entries(SEEDS)) {
    try {
      const existing = await strapi.documents(uid).findMany({ limit: 1 });
      if (Array.isArray(existing) && existing.length > 0) continue;
      for (const data of entries) {
        await strapi.documents(uid).create({ data, status: 'published' });
      }
      strapi.log.info(`[seed] created ${entries.length} ${uid} entr(y/ies)`);
    } catch (err) {
      strapi.log.warn(`[seed] skipped ${uid}: ${err && err.message ? err.message : err}`);
    }
  }
}
