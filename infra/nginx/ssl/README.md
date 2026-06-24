# SSL/TLS Certificates

## Certificate Management

This directory contains SSL/TLS certificates for the AVGC-XR Portal.

### Files Required

| File | Description |
|------|-------------|
| `cert.pem` | SSL certificate (full chain) |
| `key.pem` | Private key (RSA 4096 or ECDSA P-256) |
| `dhparam.pem` | Diffie-Hellman parameters (4096-bit) |

### Certificate Authority

Certificates must be issued by a CA trusted by Indian government browsers:
- NIC CA
- eMudhra
- IDRBT CA

### Renewal

Certificates are renewed automatically via certbot before expiry.
Minimum key size: RSA 2048 (recommended RSA 4096).
TLS 1.3 required for all external connections.

### Generating DH Parameters

```bash
openssl dhparam -out dhparam.pem 4096
```

### Self-Signed (Development Only)

```bash
openssl req -x509 -nodes -days 365 -newkey rsa:2048   -keyout key.pem -out cert.pem   -subj "/C=IN/ST=TamilNadu/L=Chennai/O=ELCOT/OU=AVGC-XR/CN=avgcxr.elcot.tn.gov.in"
```
