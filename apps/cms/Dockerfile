# ─── AVGC-XR CMS (Strapi-compatible) ───
# Non-root user (audit fix HIGH-022)
FROM node:22-alpine

RUN addgroup -S avgcxr && adduser -S avgcxr -G avgcxr
WORKDIR /app

COPY package*.json ./
RUN npm ci --production --ignore-scripts && chown -R avgcxr:avgcxr /app
USER avgcxr

COPY --chown=avgcxr:avgcxr . .
RUN npm run build

EXPOSE 1337
ENV NODE_ENV=production
ENV HOST=0.0.0.0
ENV PORT=1337
HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD wget -qO- http://localhost:1337/_health >/dev/null 2>&1 || exit 1
CMD ["npm","run","start"]
